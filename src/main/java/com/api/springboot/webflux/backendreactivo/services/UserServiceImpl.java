package com.api.springboot.webflux.backendreactivo.services;

import com.api.springboot.webflux.backendreactivo.models.User;
import com.api.springboot.webflux.backendreactivo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public Flux<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public Mono<User> save(User user) {
        return userRepo.save(user);
    }

    @Override
    public Mono<Void> delete(User user) {
        return userRepo.delete(user);
    }
}
