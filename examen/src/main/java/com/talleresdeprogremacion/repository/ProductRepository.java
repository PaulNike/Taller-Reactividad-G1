package com.talleresdeprogremacion.repository;

import com.talleresdeprogremacion.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    Flux<Product> findByPriceGreaterThan(Double minPrice);
}
