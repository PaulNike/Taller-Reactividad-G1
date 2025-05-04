package com.talleresdeprogramacion.handler;

import com.talleresdeprogramacion.dto.DishDTO;
import com.talleresdeprogramacion.model.Dish;
import com.talleresdeprogramacion.service.DishService;
import com.talleresdeprogramacion.validator.RequestValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.function.ServerRequest;
//import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class DishHandler {

    private final DishService service;
    private final ModelMapper modelMapper;
    //private final Validator validator;

    private final RequestValidator validator;

    public DishHandler(DishService service, @Qualifier("defaultMapper") ModelMapper modelMapper, RequestValidator validator) {
        this.service = service;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }


    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll().map(this::convertToDto), DishDTO.class);
    }
    public Mono<ServerResponse> findById(ServerRequest request){
        String id = request.pathVariable("id");
        return service.findById(id)
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(e))
                )
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request){
        Mono<DishDTO> dishDTOMono = request.bodyToMono(DishDTO.class);

       /* return dishDTOMono
                .flatMap(e -> {
                    Errors errors = new BeanPropertyBindingResult(e, DishDTO.class.getName());
                    validator.validate(e, errors);

                    if(errors.hasErrors()){
                        return Flux.fromIterable(errors.getFieldErrors())
                                .map(error -> new ValidationDTO(error.getField(), error.getDefaultMessage()))
                                .collectList()
                                .flatMap(list -> ServerResponse
                                        .badRequest()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(list))
                                );
                    }else{
                        return service.save(this.convertToDocument(e))
                                .map(this::convertToDto)
                                .flatMap( dto -> ServerResponse
                                        .created(URI.create(request.uri().toString().concat("/")
                                                .concat(e.getId())))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(BodyInserters.fromValue(dto))
                                );
                    }
                });*/
        
        return dishDTOMono
                .flatMap(validator::validate)
                .flatMap(e -> service.save(convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap( e -> ServerResponse
                        .created(URI.create(request.uri().toString().concat("/")
                                .concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(e))
                );

    }

    public Mono<ServerResponse> update(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<DishDTO> monoDishDTO = request.bodyToMono(DishDTO.class);

        return monoDishDTO
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(validator::validate)
                .flatMap(e -> service.update(id, convertToDocument(e)))
                .map(this::convertToDto)
                .flatMap(e -> ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(e)));
    }

    private DishDTO convertToDto(Dish model){
        return modelMapper.map(model, DishDTO.class);
    }
    private Dish convertToDocument(DishDTO dto){
        return modelMapper.map(dto, Dish.class);
    }
}
