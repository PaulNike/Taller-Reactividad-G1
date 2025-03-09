package com.talleresdeprogramacion.repository;

import com.talleresdeprogramacion.model.Client;
import reactor.core.publisher.Mono;

public interface ClientRepository extends GenericRepository<Client, String> {

    Mono<Client> findByFirstName(String name);
}

