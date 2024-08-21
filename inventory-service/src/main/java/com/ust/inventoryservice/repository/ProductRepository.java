package com.ust.inventoryservice.repository;

import com.ust.inventoryservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    // Check if the product exists by its SKU code
    Optional<Product> findBySkuCode(String skuCode);

    // Check if the requested quantity of the product is available
    boolean existsBySkuCodeAndQuantityGreaterThanEqual(String skuCode, int quantity);

    // Return quantity of the product by its SKU code
    int findQuantityBySkuCode(String skuCode);

}
