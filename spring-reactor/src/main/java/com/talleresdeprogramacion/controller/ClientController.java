package com.talleresdeprogramacion.controller;

import com.talleresdeprogramacion.model.Client;
import com.talleresdeprogramacion.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    //ServerHtppRequest

    @GetMapping
    public Mono<ResponseEntity<Flux<Client>>> findAll(){
        Flux<Client> fx = clientService.findAll()
                ;
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fx)
        ).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Client>> findById(@PathVariable("id") String id){
        return clientService.findById(id)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/data/{name}")
    public Mono<ResponseEntity<Client>> findByName(@PathVariable("name") String name){
        return clientService.findByName(name)
                .map(e -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<EntityModel<Client>>> save(@RequestBody Client client,
                                                         ServerWebExchange serverWebExchange){
        /*return clientService.save(client)
                .map(e -> ResponseEntity.created(
                        URI.create(request.getURI().toString().concat("/").concat(e.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(e))
                .defaultIfEmpty(ResponseEntity.notFound().build());*/

        return clientService.save(client)
                .map(savedClient -> {
                    EntityModel<Client> resource = EntityModel.of(savedClient,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class)
                                    .findById(savedClient.getId()))
                                    .withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController.class)
                                    .delete(savedClient.getId()))
                                    .withRel("delete"));
                    return ResponseEntity.created(
                            URI.create(serverWebExchange.getRequest().getURI().toString().concat("/")
                                    .concat(savedClient.getId())))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(resource);
                }).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<Client> update(@PathVariable("id") String id,
                               @RequestBody Client client){
        return Mono.just(client)
                .map(e -> {
                    e.setId(id);
                    return e;
                })
                .flatMap(e -> clientService.update(id,client));
    }

    @DeleteMapping("/{id}")
    public Mono<Boolean> delete(@PathVariable("id") String id){
        return clientService.delete(id);
    }


}
