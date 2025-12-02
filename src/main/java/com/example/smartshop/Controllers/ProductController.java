package com.example.smartshop.Controllers;

import com.example.smartshop.DTO.ProductDto;
import com.example.smartshop.Exceptions.ForbiddenAccessException;
import com.example.smartshop.Mapper.ProductMapper;
import com.example.smartshop.Repositories.ProductRepository;
import com.example.smartshop.Services.ProductServiceInterface;
import com.example.smartshop.Util.AuthUtil;
import jakarta.servlet.http.HttpSession;
//import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

            throw new ForbiddenAccessException("You don't have the privileges to create a product.");
        }

        ProductDto savedProductDto = productService.createProduct(creationDto);

        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
    }
@PutMapping("/{id}/updateProduct")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable Long id,HttpSession session) {
        if(!authUtil.isAdmin(session)) {
            throw new ForbiddenAccessException("You don't have the privileges to update a product.");
        }
        ProductDto savedProductDto = productService.updateProduct(productDto, id);
        return new ResponseEntity<>(savedProductDto, HttpStatus.CREATED);
}

@DeleteMapping("/{id}/deleteProduct")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id, HttpSession session) {
        if(!authUtil.isAdmin(session)) {
            throw  new ForbiddenAccessException("You don't have the privileges to delete a product.");
        }
        productService.deleteProduct(id);
        return  ResponseEntity.ok("the product has been deleted");
}
    @GetMapping

    public ResponseEntity<Page<ProductDto>> getProducts(
            Pageable pageable,
            @RequestParam(required = false) String nameFilter,
            HttpSession session) {

        System.out.println("Session ID: " + (session != null ? session.getId() : "null"));
        System.out.println("User ID from session: " + authUtil.getLoggedInUserId(session));
        System.out.println("Session attributes: " + (session != null ? session.getAttributeNames() : "no session"));

        if (authUtil.getLoggedInUserId(session) == null) {
            throw new ForbiddenAccessException("Authentication required to view catalog.");
        }

        Page<ProductDto> productPage = productService.getAllProducts(pageable, nameFilter);

        return ResponseEntity.ok(productPage);
    }
}
