package com.talleresdeprogramacion.controller;

import com.talleresdeprogramacion.model.Dish;
import com.talleresdeprogramacion.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Dish>>> findAll(){
        Flux<Dish> fx = dishService.findAll();
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Dish>> findById(
            @PathVariable("id") String id){

        return dishService.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<Dish>> save(@RequestBody Dish dish,
                                           final ServerHttpRequest request){
        return dishService.save(dish)
                .map(e -> ResponseEntity.created(
                        URI.create(request.getURI().toString()
                                .concat("/")
                                .concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Dish>> update(
            @PathVariable("id") String id,
            @RequestBody Dish dish){
        return Mono.just(dish)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> dishService.update(id, e))
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(
            @PathVariable("id") String id){
        return dishService.delete(id)
                .flatMap(result -> {
                    if (result){
                        return Mono.just(ResponseEntity.noContent().build());
                    }else{
                        return Mono.just(ResponseEntity.notFound().build());
                    }
                });
    }

}
