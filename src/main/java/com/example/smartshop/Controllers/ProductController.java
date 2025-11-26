package com.example.smartshop.Controllers;

import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Mapper.ProductMapper;
import com.example.smartshop.Repositories.ProductRepository;
import com.example.smartshop.Services.ProductServiceInterface;
import com.example.smartshop.Util.AuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/product")

public class ProductController {


    private final ProductServiceInterface productService;
    private final AuthUtil authUtil;

    public ProductController(ProductServiceInterface productService, AuthUtil authUtil) {
        this.productService = productService;
        this.authUtil = authUtil;
    }

    @PostMapping
    public ResponseEntity<ProductDto>createProduct(@RequestBody ProductDto productDto){
        if(!authUtil.isAdmin(session))
    }


}
