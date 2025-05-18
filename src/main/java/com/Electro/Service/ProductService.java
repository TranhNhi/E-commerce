package com.Electro.Service;

import com.Electro.Entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public List<Product> getAllProducts();

    public Product addNewProduct(Product product);
    public Optional<Product> getProduct(Long id);
    public Product getProductById(Long id);
}
