package com.example.smartshop.Mapper;


import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);

        @Mapping(target ="id" ,ignore=true)
        @Mapping(target ="isDeleted",ignore=true)
        void updateProduct(ProductDto productDto, @MappingTarget Product product);
                }
