package com.api.springboot.webflux.backendreactivo.repository;

import com.api.springboot.webflux.backendreactivo.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUsuario(String usuario);
}
