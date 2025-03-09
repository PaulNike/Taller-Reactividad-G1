package com.talleresdeprogremacion.service.impl;

import com.talleresdeprogremacion.exception.ProductNotFoundException;
import com.talleresdeprogremacion.model.Product;
import com.talleresdeprogremacion.repository.ProductRepository;
import com.talleresdeprogremacion.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Flux<Product> getAllProducts(Double minPrice) {
        return productRepository.findByPriceGreaterThan(minPrice)
                .filter(product -> product.getPrice() > 0)
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                });
    }

    @Override
    public Mono<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                })
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Producto con ID : "+ id
                        + " no fue encontrado")))
                .flatMap(Mono::just);
    }

    @Override
    public Mono<Product> createProduct(Product product) {
        if (product.getPrice() <= 0 ){
            return Mono.error(new ProductNotFoundException("El precio debe ser mayor que 0."));
        }
        return Mono.just(product)
                //Mono<T> O Flux<T>
                .map(prod -> {
                    prod.setName(prod.getName().toUpperCase());
                    return prod;
                })
                //Mono<Publisher<T>> O Flux<Publisher<T>>
                .flatMap(productRepository::save);
    }

    @Override
    public Flux<String> delayMessage() {
        return Flux.concat(
                Mono.just("Hola despues de 3 segundos")
                        .delayElement(java.time.Duration.ofSeconds(3))
                        .doOnSubscribe(sub -> log.info("Mensaje 1 en proceso. . . "))
                        .doOnNext(message -> log.info("EMitiendo: {}", message)),

                Mono.just("Este es uns egundo mensaje")
                        .delayElement(java.time.Duration.ofSeconds(2))
                        .doOnSubscribe(sub -> log.info("Mensaje 2 en proceso. . . "))
                        .doOnNext(message -> log.info("Emitiendo: {} ", message))
        );
    }
}
