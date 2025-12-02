package com.example.smartshop.Services;

import com.example.smartshop.DTO.CommandeDto;
import com.example.smartshop.DTO.OrderItemDto;
import com.example.smartshop.Exceptions.BusinessValidationException;
import com.example.smartshop.Exceptions.ResourceNotFoundException;
import com.example.smartshop.Mapper.CommandeMapper;
import com.example.smartshop.Mapper.OrderItemMapper;
import com.example.smartshop.Models.*;
import com.example.smartshop.Models.Enums.LoyaltyLevel;
import com.example.smartshop.Models.Enums.OrderStatus;
import com.example.smartshop.Repositories.ClientRepository;
import com.example.smartshop.Repositories.CommandeRepository;
import com.example.smartshop.Repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommandeServiceImpl implements CommandeServiceInterface {

    private final CommandeRepository commandeRepository;
    private final ClientRepository clientRepository;
    private final CommandeMapper commandeMapper;
    private final ProductRepository productRepository;
    private final OrderItemMapper orderItemMapper;

    // --- Loyalty Constants (Defined as BigDecimal for safe comparison) ---
    private final BigDecimal DISCOUNT_RATE_PLATINUM = new BigDecimal("0.15");
    private final BigDecimal DISCOUNT_RATE_GOLD = new BigDecimal("0.10");
    private final BigDecimal DISCOUNT_RATE_SILVER = new BigDecimal("0.05");

    private final BigDecimal THRESHOLD_PLATINUM = new BigDecimal("1200.00");
    private final int ORDERS_THRESHOLD_PLATINUM = 20;
    private final BigDecimal THRESHOLD_GOLD = new BigDecimal("800.00");
    private final int ORDERS_THRESHOLD_GOLD = 10;
    private final BigDecimal THRESHOLD_SILVER = new BigDecimal("500.00");
    private final int ORDERS_THRESHOLD_SILVER = 3;
    private final BigDecimal PROMO_RATE = new BigDecimal("0.05");


    public CommandeServiceImpl(CommandeRepository commandeRepository, ClientRepository clientRepository, CommandeMapper commandeMapper, ProductRepository productRepository, OrderItemMapper orderItemMapper) {
        this.commandeRepository = commandeRepository;
        this.clientRepository = clientRepository;
        this.commandeMapper = commandeMapper;
        this.productRepository = productRepository;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    @Transactional
    public CommandeDto createCommande(CommandeDto commandeDto) {

        Client client = clientRepository.findById(commandeDto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with ID: " + commandeDto.getClientId()));


        if (commandeDto.getItems() == null || commandeDto.getItems().isEmpty()) {
            throw new BusinessValidationException("Order must contain at least one item.");
        }

        // C. Map DTO to Entity for initial data
        Commande newCommande = commandeMapper.toEntity(commandeDto);
        newCommande.setClient(client);

        // Initialization variables
        BigDecimal subtotalHT = BigDecimal.ZERO;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        List<OrderItem> itemsToSave = new ArrayList<>();

        // --- 2. STOCK VALIDATION & ITEM PROCESSING ---

        for (OrderItemDto itemDto : commandeDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDto.getProductId()));

            // A. Quantity Check (must be > 0)
            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new BusinessValidationException("Quantity must be greater than zero for product: " + product.getName());
            }

            // B. Stock Check (CRITICAL FAIL POINT)
            if (itemDto.getQuantity() > product.getAvailableStock()) {
                // Set status to REJECTED before saving and throwing the error
                newCommande.setOrderStatus(OrderStatus.REJECTED);
                commandeRepository.save(newCommande);
                throw new BusinessValidationException("Stock insufficient for product: " + product.getName() +
                        ". Available: " + product.getAvailableStock());
            }

            // C. Prepare OrderItem (Price Freezing and Calculation)
            OrderItem orderItem = orderItemMapper.toEntity(itemDto);
            orderItem.setCommande(newCommande);
            orderItem.setProduct(product);

            // Set Unit Price (Price Freeze)
            orderItem.setUnitPrice(product.getUnitPrice());

            // Calculate Line Total
            orderItem.setLineTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            subtotalHT = subtotalHT.add(orderItem.getLineTotal());
            itemsToSave.add(orderItem);
        }

        // Set the calculated Subtotal HT
        newCommande.setSousTotalHT(subtotalHT);

        // --- 3. LOYALTY AND PROMO DISCOUNT CALCULATION ---

        BigDecimal currentSubtotalHT = newCommande.getSousTotalHT();
        LoyaltyLevel tier = client.getLoyaltyLevel();
        BigDecimal loyaltyDiscountAmount = BigDecimal.ZERO;
        BigDecimal discountRate = BigDecimal.ZERO;

        // A. Loyalty Check (using  BigDecimal comparison logic: >= 0)
        if (tier.equals(LoyaltyLevel.PLATINUM) &&
                currentSubtotalHT.compareTo(THRESHOLD_PLATINUM) >= 0) {

            discountRate = DISCOUNT_RATE_PLATINUM;

        } else if (tier.equals(LoyaltyLevel.GOLD) &&
                currentSubtotalHT.compareTo(THRESHOLD_GOLD) >= 0) {

            discountRate = DISCOUNT_RATE_GOLD;

        } else if (tier.equals(LoyaltyLevel.SILVER) &&
                currentSubtotalHT.compareTo(THRESHOLD_SILVER) >= 0) {

            discountRate = DISCOUNT_RATE_SILVER;

        }

        // Calculate and add loyalty discount
        loyaltyDiscountAmount = currentSubtotalHT.multiply(discountRate);
        totalDiscountAmount = totalDiscountAmount.add(loyaltyDiscountAmount);

        // B. Promo Code Discount Check (5%)
        if (newCommande.getPromoCode() != null && newCommande.getPromoCode().matches("PROMO-[A-Z0-9]{4}")) {
            BigDecimal promoDiscount = subtotalHT.multiply(PROMO_RATE); // 5%
            totalDiscountAmount = totalDiscountAmount.add(promoDiscount);
        }

        // Set the final total discount amount on the entity
        newCommande.setTotalDiscountAmount(totalDiscountAmount);

        // --- 4. FINAL FINANCIAL CALCULATION ---

        // C. Amount HT After Discount (Taxable Base)
        BigDecimal amountHtAfterDiscount = subtotalHT.subtract(totalDiscountAmount);
        newCommande.setAmountHTAfterDiscount(amountHtAfterDiscount);

        // D. VAT (20% on the discounted amount)
        BigDecimal vatAmount = amountHtAfterDiscount.multiply(new BigDecimal("0.20"));
        newCommande.setVat(vatAmount);

        // E. Total TTC
        BigDecimal totalHTTC = amountHtAfterDiscount.add(vatAmount);
        newCommande.setTotalWithTax(totalHTTC);

        // --- 5. FINAL SAVE (PENDING State) ---

        // Set final attributes
        newCommande.setItems(itemsToSave);
        newCommande.setOrderStatus(OrderStatus.PENDING);
        newCommande.setRemainingAmount(totalHTTC); // Client owes the full amount initially
        newCommande.setDate(new Date());

        // Save the Commande and its linked OrderItems
        Commande savedCommande = commandeRepository.save(newCommande);

        // Return the final DTO
        return commandeMapper.toDto(savedCommande);
    }


    public CommandeDto confirmCommande(Long id) {
        Commande order = commandeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        if(order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BusinessValidationException("Order status is NOT PENDING");
        }
        Client client = order.getClient();
        if(order.getRemainingAmount().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessValidationException("Order cannot be confirmed. Remaining amount due is: " + order.getRemainingAmount());
        }
        for(OrderItem orderItem : order.getItems()) {
            Product product = orderItem.getProduct();
            if(product.getAvailableStock() < orderItem.getQuantity()) {
                throw new BusinessValidationException("Product quantity less than available stock: " + product.getName());
            }
            product.setAvailableStock(product.getAvailableStock() - orderItem.getQuantity());
            productRepository.save(product);
        }
        client.setTotalOrders(client.getTotalOrders() + 1);
        client.setTotalSpent(client.getTotalSpent().add(order.getTotalWithTax()));

        // Update last order date
        client.setLastOrderDate(new Date());

        // Set first order date if null
        if (client.getFirstOrderDate() == null) {
            client.setFirstOrderDate(new Date());
        }

        // C. Recalculate Loyalty Tier (Tier Update Logic)
        LoyaltyLevel newTier = calculateNewLoyaltyTier(client.getTotalOrders(), client.getTotalSpent());
        client.setLoyaltyLevel(newTier);

        // Save modified client entity (persists stat and tier changes)
        clientRepository.save(client);

        // --- 4. FINAL STATUS AND RETURN ---

        order.setOrderStatus(OrderStatus.RECEIVED);

        // Save modified command entity
        Commande savedCommande = commandeRepository.save(order);

        return commandeMapper.toDto(savedCommande);




    }


    private LoyaltyLevel calculateNewLoyaltyTier(Integer totalOrders, BigDecimal totalSpent) {


        if (totalOrders >= ORDERS_THRESHOLD_PLATINUM || totalSpent.compareTo(THRESHOLD_PLATINUM) >= 0) {
            return LoyaltyLevel.PLATINUM;
        }


        if (totalOrders >= ORDERS_THRESHOLD_GOLD || totalSpent.compareTo(THRESHOLD_GOLD) >= 0) {
            return LoyaltyLevel.GOLD;
        }


        if (totalOrders >= ORDERS_THRESHOLD_SILVER || totalSpent.compareTo(THRESHOLD_SILVER) >= 0) {
            return LoyaltyLevel.SILVER;
        }


        return LoyaltyLevel.BASIC;
    }


    public List<CommandeDto> getAllOrders() {

        List<Commande> orders = commandeRepository.findAll();

        return orders.stream()
                .map(commandeMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<CommandeDto> getOrdersByClient(Long clientId) {


        List<Commande> orders = commandeRepository.findAllByClientId(clientId);


        return orders.stream()
                .map(commandeMapper::toDto)
                .collect(Collectors.toList());
    }


}