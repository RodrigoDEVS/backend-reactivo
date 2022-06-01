package com.api.springboot.webflux.backendreactivo.services;

import com.api.springboot.webflux.backendreactivo.models.Client;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientService {

    public Flux<Client> findAll();

    public Mono<Client> findByid(String id);

    public Mono<Client> save(Client client);

    public Mono<Void> delete(Client client);
}
