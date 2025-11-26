package com.example.smartshop.Controllers;

import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Exceptions.ForbiddenAccessException;
import com.example.smartshop.Mapper.ProductMapper;
import com.example.smartshop.Repositories.ProductRepository;
import com.example.smartshop.Services.ProductServiceInterface;
import com.example.smartshop.Util.AuthUtil;
import jakarta.servlet.http.HttpSession;
//import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.web.client.HttpClientErrorException;


@RestController
@RequestMapping("api/product")

public class ProductController {


    private final ProductServiceInterface productService;
    private final AuthUtil authUtil;

    public ProductController(ProductServiceInterface productService, AuthUtil authUtil) {
        this.productService = productService;
        this.authUtil = authUtil;
    }

    @PostMapping("createProduct")
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto creationDto,
            HttpSession session) {
        System.out.println(session.getAttribute("userRole"));

        if (!authUtil.isAdmin(session)) {

            throw new ForbiddenAccessException("You must have ADMIN privileges to create a product.");
        }

        ProductDto savedProductDto = productService.createProduct(creationDto);

        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
    }


}
