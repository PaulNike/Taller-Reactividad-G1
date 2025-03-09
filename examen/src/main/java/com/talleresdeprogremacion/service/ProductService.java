package com.talleresdeprogremacion.service;

import com.talleresdeprogremacion.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductService {
    Flux<Product> getAllProducts(Double minPrice);
    Mono<Product> getProductById(Long id);
    Mono<Product> createProduct(Product product);
    Flux<String> delayMessage();
}
