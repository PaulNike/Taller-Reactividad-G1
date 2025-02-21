package com.talleresdeprogramacion.service;

import com.talleresdeprogramacion.model.Client;
import reactor.core.publisher.Mono;


public interface ClientService extends GenericCrud<Client, String>{

    Mono<Client> findByName(String string);
}
