package com.talleresdeprogramacion.repository;

import com.talleresdeprogramacion.model.Dish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface DishRepository extends GenericRepository<Dish, String> {

    Flux<Dish> findAllBy(Pageable pageable);
}
