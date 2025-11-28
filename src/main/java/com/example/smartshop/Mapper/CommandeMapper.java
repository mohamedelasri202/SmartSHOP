package com.example.smartshop.Mapper;

import com.example.smartshop.DTO.CommandeDto;
import com.example.smartshop.Models.Commande;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Tells MapStruct to find and use the other mappers for nested collections
@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class})
public interface CommandeMapper {

    // --- 1. Entity to DTO (Output Mapping) ---

    // Maps the ID from the nested 'client' object to the flat 'clientId' field in the DTO
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "vat", target = "vat")

    // CRITICAL: Map the calculated fields where the Entity name differs from the DTO name
    @Mapping(source = "sousTotalHT", target = "subtotalHT") // Maps French entity field to English DTO field
    @Mapping(source = "totalWithTax", target = "totalTTC") // Maps Entity total to DTO totalTTC

    // MapStruct automatically handles: date, promoCode, orderStatus, remainingAmount,
    // totalDiscountAmount, amountHTAfterDiscount, vat (assuming DTO names match)

    // Maps the nested lists (delegates to OrderItemMapper and PaymentMapper)
    @Mapping(source = "items", target = "items")
    @Mapping(source = "payments", target = "payments")

    CommandeDto toDto(Commande commande);


    // --- 2. DTO to Entity (Input Mapping for Creation) ---

    // Ignores are necessary as all these fields are calculated or set by the Service Layer
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true) // Set manually in Service
    @Mapping(target = "orderStatus", ignore = true) // Set manually to PENDING/REJECTED
    @Mapping(target = "remainingAmount", ignore = true) // Calculated in Service
    @Mapping(target = "date", ignore = true) // Set by Service to new Date()

    // Ignore all calculated financials
    @Mapping(target = "sousTotalHT", ignore = true)
    @Mapping(target = "totalDiscountAmount", ignore = true)
    @Mapping(target = "amountHTAfterDiscount", ignore = true)
    @Mapping(target = "vat", ignore = true)
    @Mapping(target = "totalWithTax", ignore = true)

    @Mapping(target = "items", ignore = true) // Items are processed and linked separately
    @Mapping(target = "payments", ignore = true) // Payments are linked later
    Commande toEntity(CommandeDto dto);
}