package com.api.springboot.webflux.backendreactivo.services;

import com.api.springboot.webflux.backendreactivo.models.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

    public Flux<User> findAll();

    public Mono<User> findById(String id);

    public Mono<User> save(User user);

    public Mono<Void> delete(User user);

    public Mono<User> findByUsuario(String usuario);
}
