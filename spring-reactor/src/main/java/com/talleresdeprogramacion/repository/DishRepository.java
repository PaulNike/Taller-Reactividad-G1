package com.talleresdeprogramacion.repository;

import com.talleresdeprogramacion.model.Dish;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DishRepository extends GenericRepository<Dish, String> {
}
