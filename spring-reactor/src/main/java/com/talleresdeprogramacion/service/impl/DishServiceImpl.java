package com.talleresdeprogramacion.service.impl;

import com.talleresdeprogramacion.model.Dish;
import com.talleresdeprogramacion.repository.DishRepository;
import com.talleresdeprogramacion.repository.GenericRepository;
import com.talleresdeprogramacion.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DishServiceImpl extends GenericCrudImpl<Dish,String> implements DishService {

    private final DishRepository dishRepository;

    @Override
    protected GenericRepository<Dish, String> getRepository() {
        return dishRepository;
    }

    @Override
    public Flux<Dish> findAllBy(Pageable pageable) {
        return dishRepository.findAllBy(pageable);
    }
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


}
