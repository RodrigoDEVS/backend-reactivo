package com.api.springboot.webflux.backendreactivo.services;

import com.api.springboot.webflux.backendreactivo.models.Client;
import com.api.springboot.webflux.backendreactivo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository ClientRepo;

    @Override
    public Flux<Client> findAll() {
        return ClientRepo.findAll();
    }

    @Override
    public Mono<Client> findByid(String id) {
        return ClientRepo.findById(id);
    }

    @Override
    public Mono<Client> save(Client client) {
        return ClientRepo.save(client);
    }

    @Override
    public Mono<Void> delete(Client client) {
        return ClientRepo.delete(client);
    }
}
