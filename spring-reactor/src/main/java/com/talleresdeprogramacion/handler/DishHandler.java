package com.talleresdeprogramacion.handler;

import com.talleresdeprogramacion.dto.DishDTO;
import com.talleresdeprogramacion.model.Dish;
import com.talleresdeprogramacion.service.DishService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.function.ServerRequest;
//import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class DishHandler {

    private final DishService service;

    private final ModelMapper modelMapper;

    public DishHandler(DishService service, @Qualifier("defaultMapper") ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertToDto), DishDTO.class);
    }

    private DishDTO convertToDto(Dish model){
        return modelMapper.map(model, DishDTO.class);
    }
    private Dish convertToDocument(DishDTO dto){
        return modelMapper.map(dto, Dish.class);
    }
}
