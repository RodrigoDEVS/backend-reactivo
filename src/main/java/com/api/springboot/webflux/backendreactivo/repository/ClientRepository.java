package com.api.springboot.webflux.backendreactivo.repository;

import com.api.springboot.webflux.backendreactivo.models.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ClientRepository extends ReactiveMongoRepository<Client, String> {
}
