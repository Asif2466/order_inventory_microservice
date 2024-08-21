package com.ust.inventoryservice.service;

import com.ust.inventoryservice.domain.Product;
import com.ust.inventoryservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public boolean isProductAvailable(String skuCode, int quantity) {
        return productRepository.existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity);
    }

    public int getProductQuantity(String skuCode) {
        return productRepository.findQuantityBySkuCode(skuCode);
    }

    @Transactional
    public void updateProductQuantity(String skuCode, int quantity) {
        int currentQuantity = productRepository.findQuantityBySkuCode(skuCode);
        productRepository.findBySkuCode(skuCode).ifPresent(product -> {
            product.setQuantity(currentQuantity - quantity);
            productRepository.save(product);
        });
    }

    @Transactional
    public void addProductQuantity(String skuCode, int quantity) {
        int currentQuantity = productRepository.findQuantityBySkuCode(skuCode);
        productRepository.findBySkuCode(skuCode).ifPresent(product -> {
            product.setQuantity(currentQuantity + quantity);
            productRepository.save(product);
        });
    }

    public Optional<Product> getProductBySkuCode(String skuCode) {
        return productRepository.findBySkuCode(skuCode);
    }
}
