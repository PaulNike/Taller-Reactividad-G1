package com.talleresdeprogramacion.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.talleresdeprogramacion.dto.ClientDTO;
import com.talleresdeprogramacion.model.Client;
import com.talleresdeprogramacion.service.ClientService;
import org.cloudinary.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    
    private final ModelMapper modelMapper;

    private final Cloudinary cloudinary;

    public ClientController(ClientService clientService, @Qualifier("clientMapper") ModelMapper modelMapper, Cloudinary cloudinary) {
        this.clientService = clientService;
        this.modelMapper = modelMapper;
        this.cloudinary = cloudinary;
    }

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



    @PostMapping("/{id}")
    public Mono<ResponseEntity<ClientDTO>> uploadV1(@PathVariable("id") String id,
                                                    @RequestPart("file")FilePart filePart){
        return clientService.findById(id)
                .flatMap(client -> {
                    try {
                        File f = Files.createTempFile("temp",filePart.filename()).toFile();
                        filePart.transferTo(f).block();

                        Map<String, Object> response = cloudinary.uploader().upload(f,
                                ObjectUtils.asMap("resource_type","auto"));

                        JSONObject jsonObject = new JSONObject(response);
                        String url = jsonObject.getString("url");

                        client.setUrlPhoto(url);

                        return clientService.update(id,client)
                                .map(this::convertToDto)
                                .map( e -> ResponseEntity.ok().body(e));

                    }catch (IOException e){
                        return Mono.just(ResponseEntity.badRequest().build());
                    }
                });

    }
    private ClientDTO convertToDto(Client model){
        return modelMapper.map(model, ClientDTO.class);
    }

    private Client convertToDocument(ClientDTO dto){
        return modelMapper.map(dto, Client.class);
    }

}
