package com.example.smartshop.Mapper;


import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);




}
