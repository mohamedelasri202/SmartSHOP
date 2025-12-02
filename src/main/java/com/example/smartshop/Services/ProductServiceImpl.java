package com.example.smartshop.Services;

import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Exceptions.ResourceNotFoundException;
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
       Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);

    }
    @Override
    public ProductDto updateProduct(ProductDto productDto , Long id){
//         Product product = productMapper.toEntity(productDto);
         Product product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found"));

          productMapper.updateProduct(productDto,product);
          Product savedProduct =  productRepository.save(product);
            return productMapper.toDto(savedProduct);

    }
    @Override
    public void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found"));
             product.setIsDeleted(true);
                productRepository.save(product);

    }

    public Page<ProductDto> getAllProducts(Pageable pageable, String nameFilter) {

        Page<Product> productPage;

        if (nameFilter != null && !nameFilter.trim().isEmpty()) {

            productPage = productRepository.findByNameContainingIgnoreCaseAndIsDeletedFalse(nameFilter, pageable);
        } else {

            productPage = productRepository.findAllByIsDeletedFalse(pageable);
        }

        return productPage.map(productMapper::toDto);
    }
}
