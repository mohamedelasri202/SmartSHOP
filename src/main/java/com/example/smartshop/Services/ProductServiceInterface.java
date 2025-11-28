package com.example.smartshop.Services;

import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductServiceInterface {
    ProductDto createProduct(ProductDto product);
    ProductDto updateProduct(ProductDto product,Long id);
    void deleteProduct(Long id);
//    Page<Product> getProducts(Pageable pageable);
}
