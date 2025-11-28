package com.example.smartshop.Services;

import com.example.smartshop.DTO.CommandeDto;
import com.example.smartshop.DTO.OrderItemDto;
import com.example.smartshop.Exceptions.BusinessValidationException;
import com.example.smartshop.Exceptions.ResourceNotFoundException;
import com.example.smartshop.Mapper.CommandeMapper;
import com.example.smartshop.Mapper.OrderItemMapper;
import com.example.smartshop.Models.Client;
import com.example.smartshop.Models.Commande;
import com.example.smartshop.Models.Enums.LoyaltyLevel;
import com.example.smartshop.Models.Enums.OrderStatus;
import com.example.smartshop.Models.OrderItem;
import com.example.smartshop.Models.Product;
import com.example.smartshop.Repositories.ClientRepository;
import com.example.smartshop.Repositories.CommandeRepository;
import com.example.smartshop.Repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandeServiceImpl implements CommandeServiceInterface {


   private final CommandeRepository commandeRepository;
   private final ClientRepository clientRepository;
   private final  CommandeMapper commandeMapper;
   private final ProductRepository productRepository;
   private final OrderItemMapper orderItemMapper;

   public CommandeServiceImpl(CommandeRepository commandeRepository, ClientRepository clientRepository,CommandeMapper commandeMapper, ProductRepository productRepository, OrderItemMapper orderItemMapper) {
       this.commandeRepository = commandeRepository;
       this.clientRepository = clientRepository;
       this.commandeMapper = commandeMapper;
       this.productRepository = productRepository;
       this.orderItemMapper = orderItemMapper;

   }





    public CommandeDto createCommande(CommandeDto commandeDto) {

       Client client = clientRepository.findById(commandeDto.getClientId()).get();
       if(client == null) {
           throw new ResourceNotFoundException("Client not found");
       }

       if(commandeDto.getItems() == null && commandeDto.getItems().isEmpty()) {
           throw new ResourceNotFoundException("Items not found");
       }
       Commande newCommande = commandeMapper.toEntity(commandeDto);
       newCommande.setClient(client);


        BigDecimal subtotalHT = BigDecimal.ZERO;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        List<OrderItem> itemsToSave = new ArrayList<>();

        for(OrderItemDto dto : commandeDto.getItems()){
            Product product =productRepository.findById(dto.getProductId()).orElseThrow(()->new ResourceNotFoundException("Product not found"));

            if(dto.getQuantity() == null || dto.getQuantity() <= 0 ) {
                throw new BusinessValidationException("Quantity must be greater than zero");
            }

            if(dto.getQuantity()>product.getAvailableStock()){
                newCommande.setOrderStatus(OrderStatus.REJECTED);
                commandeRepository.save(newCommande);
                throw  new BusinessValidationException("StoCK is not enough for this product"+product.getName());
            }

            OrderItem orderItem = orderItemMapper.toEntity(dto);
             orderItem.setCommande(newCommande);
             orderItem.setProduct(product);
             orderItem.setUnitPrice(product.getUnitPrice());
             orderItem.setUnitPrice(product.getUnitPrice());
             orderItem.setLineTotal(product.getUnitPrice().multiply(BigDecimal.valueOf(dto.getQuantity())));
             subtotalHT = subtotalHT.add(orderItem.getLineTotal());
             itemsToSave.add(orderItem);
        }
        newCommande.setSousTotalHT(subtotalHT);
        LoyaltyLevel tier = client.getLoyaltyLevel();


        if (commandeDto.getPromoCode() != null && commandeDto.getPromoCode().matches("PROMO-[A-Z0-9]{4}")) {
            totalDiscountAmount = totalDiscountAmount.add(subtotalHT.multiply(new BigDecimal("0.5")));
        }

        newCommande.setTotalDiscountAmount(totalDiscountAmount);

        BigDecimal amountHtAfterDiscount = subtotalHT.subtract(totalDiscountAmount);

        newCommande.setAmountHTAfterDiscount(amountHtAfterDiscount);

        BigDecimal vatAmount = amountHtAfterDiscount.multiply(new BigDecimal("0.20"));
        newCommande.setVat(vatAmount);

        BigDecimal  totalHTTC = amountHtAfterDiscount.add(vatAmount);
        newCommande.setTotalWithTax(totalHTTC);

        newCommande.setItems(itemsToSave);
        newCommande.setOrderStatus(OrderStatus.PENDING);
        newCommande.setRemainingAmount(totalHTTC);
        newCommande.setDate(new Date());

        Commande savedCommande = commandeRepository.save(newCommande);
        return commandeMapper.toDto(savedCommande);

    }
}
