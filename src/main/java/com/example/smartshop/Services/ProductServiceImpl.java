package com.example.smartshop.Services;

import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Mapper.ProductMapper;
import com.example.smartshop.Models.Product;
import com.example.smartshop.Repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductServiceInterface {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }



    @Override
    public ProductDto createProduct(ProductDto productDto){
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);

    }
//    @Override
//    public ProductDto updateProduct(ProductDto productDto){
//
//    }
//    @Override
//    public void deleteProduct(ProductDto productDto){
//
//    }
//    @Override
//    public Page<Product> getProducts(Pageable pageable) {}
}
