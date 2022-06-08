package com.api.springboot.webflux.backendreactivo.repository;

import com.api.springboot.webflux.backendreactivo.models.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
