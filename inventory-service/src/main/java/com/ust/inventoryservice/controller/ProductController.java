package com.ust.inventoryservice.controller;

import com.ust.inventoryservice.domain.Product;
import com.ust.inventoryservice.payload.ProductDto;
import com.ust.inventoryservice.payload.ResponseDto;
import com.ust.inventoryservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // GET /products?skuCode=P01&quantity=2
    @GetMapping
    public ResponseEntity<ResponseDto> isProductAvailable(@RequestParam String skuCode, @RequestParam int quantity) {
        var status =  productService.isProductAvailable(skuCode, quantity);
        var response = new ResponseDto(skuCode, status, status ? HttpStatus.OK: HttpStatus.BAD_REQUEST);
//        if (status) {
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body(response);
//        }
        return ResponseEntity.ok(response);
    }

    // GET /products/{skuCode}
    @GetMapping("/quantity/{skuCode}")
    public ResponseEntity<Product> getProductQuantity(@PathVariable String skuCode) {
        var response = productService.getProductBySkuCode(skuCode).orElseThrow();
        return ResponseEntity.ok(response);
    }

    // GET /products?skuCode=P01&quantity=2
    @PutMapping("/update")
    public ResponseEntity<Void> updateProductQuantity(@RequestParam String skuCode, @RequestParam int quantity){
        productService.updateProductQuantity(skuCode, quantity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/add")
    public ResponseEntity<Void> addProductQuantity(@RequestParam String skuCode, @RequestParam int quantity){
        productService.addProductQuantity(skuCode,quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{skucode}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String skucode){
        Product product = productService.getProductBySkuCode(skucode).orElseThrow();
        return ResponseEntity.ok(new ProductDto(product.getSkuCode(),product.getName(),product.getPrice()));
    }
}
