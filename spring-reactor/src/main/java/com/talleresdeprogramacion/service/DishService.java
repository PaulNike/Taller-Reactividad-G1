package com.talleresdeprogramacion.service;

import com.talleresdeprogramacion.model.Dish;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface DishService extends GenericCrud<Dish,String>{

    Flux<Dish> findAllBy(Pageable pageable);
    /*
    Mono<Dish> save(Dish dish);
    Mono<Dish> update(String id, Dish dish);
    Flux<Dish> findAll();
    Mono<Dish> findById(String id);
    Mono<Boolean> delete(String id);*/

}
