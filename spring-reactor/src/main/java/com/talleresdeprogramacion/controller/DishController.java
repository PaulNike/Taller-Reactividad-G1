package com.talleresdeprogramacion.controller;

import com.talleresdeprogramacion.dto.DishDTO;
import com.talleresdeprogramacion.model.Dish;
import com.talleresdeprogramacion.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @GetMapping
    public Mono<ResponseEntity<Flux<DishDTO>>> findAll(){
        Flux<DishDTO> fx = dishService.findAll()
                .map( this::convertToDto);
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<DishDTO>> findById(
            @PathVariable("id") String id){

        return dishService.findById(id)
                .map(this::convertToDto)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<ResponseEntity<DishDTO>> save(@Valid @RequestBody DishDTO dishDTO,
                                           final ServerHttpRequest request){
        return dishService.save(convertToDocument(dishDTO))
                .map(this::convertToDto)
                .map(e -> ResponseEntity.created(
                        URI.create(request.getURI().toString()
                                .concat("/")
                                .concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e)
                ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<DishDTO>> update(
            @PathVariable("id") String id,
            @RequestBody DishDTO dishDTO){
        return Mono.just(dishDTO)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> dishService.update(id, convertToDocument(e)))
                .map(this::convertToDto)
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

    private DishDTO convertToDto(Dish model){
        return modelMapper.map(model, DishDTO.class);
    }

    private Dish convertToDocument(DishDTO dto){
        return modelMapper.map(dto, Dish.class);
    }

}
