package com.talleresdeprogramacion.service.impl;

import com.talleresdeprogramacion.model.Dish;
import com.talleresdeprogramacion.repository.DishRepository;
import com.talleresdeprogramacion.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    //Inyeccion por Constructor
    /*public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }*/

    //Inyeccion por Constructor usando Autowired
    /*@Autowired
    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }*/
    //Inyeccion directa con Autowired
    /*@Autowired
    private DishRepository dishRepository2;*/

    //Inyeccion directa con Autowired en Setter
    /*private DishRepository dishRepository2;

    @Autowired
    public void setDishRepository2(DishRepository dishRepository2) {
        this.dishRepository2 = dishRepository2;
    }*/

    @Override
    public Mono<Dish> save(Dish dish) {
        return dishRepository.save(dish);
    }

    @Override
    public Mono<Dish> update(String id, Dish dish) {
        return dishRepository.findById(id)        //-> Mono<Dish>
                .flatMap(e -> dishRepository.save(dish));
                /*.flatMap(dishExist -> {
                    dishExist.setName(dish.getName());
                    dishExist.setPrice(dish.getPrice());
                    dishExist.setStatus(dish.isStatus());
                    return dishRepository.save(dishExist);
                });*/

        //usando MAP -> Mono<Mono<Dish>>
        // Usando FlatMap -> Mono<Dish>
    }

    @Override
    public Flux<Dish> findAll() {
        return dishRepository.findAll();
    }

    @Override
    public Mono<Dish> findById(String id) {
        return dishRepository.findById(id);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return dishRepository.findById(id)
                .hasElement()// Verificar si el elemento del Mono buscado existe o no
                .flatMap(result -> {
                    if(result){
                        return dishRepository.deleteById(id).thenReturn(true);
                    } else {
                        return Mono.just(false);
                    }
                });
    }
}
