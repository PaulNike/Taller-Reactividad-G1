package com.talleresdeprogramacion.service.impl;

import com.talleresdeprogramacion.model.Client;
import com.talleresdeprogramacion.repository.ClientRepository;
import com.talleresdeprogramacion.repository.GenericRepository;
import com.talleresdeprogramacion.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class ClientServiceImpl extends GenericCrudImpl<Client, String> implements ClientService{

    private final ClientRepository clientRepository;

    @Override
    protected GenericRepository<Client, String> getRepository() {
        return clientRepository;
    }
    @Override
    public Mono<Client> findByName(String string) {
        return clientRepository.findByFirstName(string);
    }
}
