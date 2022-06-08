package com.api.springboot.webflux.backendreactivo.controllers;

import com.api.springboot.webflux.backendreactivo.models.Client;
import com.api.springboot.webflux.backendreactivo.models.User;
import com.api.springboot.webflux.backendreactivo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService service;

    @PostMapping("/registro")
    public Mono<ResponseEntity<User>> registrarUsuario(User user) {
        return service.save(user)
                .map(element -> ResponseEntity.created(URI.create("/api/users".concat(element.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(element));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<User>>> listarUsuarios(){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> verDetallesUsuarios(@PathVariable String id){
        return service.findById(id).map(element -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(element))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> guardarCliente(@RequestBody Mono<User> monoClient){
        Map<String, Object> resp = new HashMap<>();

        return monoClient.flatMap(user -> {
            return service.save(user).map(element -> {
                resp.put("user", element);
                resp.put("mensaje", "Usuario guardado con éxito");
                resp.put("timestamp", new Date());
                return ResponseEntity
                        .created(URI.create(("/api/users".concat(element.getId()))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(resp);
            });
        }).onErrorResume(t -> {
            return Mono.just(t).cast(WebExchangeBindException.class)
                    .flatMap(e -> Mono.just(e.getFieldErrors()))
                    .flatMapMany(Flux::fromIterable)
                    .map(fieldErrors -> "El campo: " + fieldErrors.getField() + fieldErrors.getDefaultMessage())
                    .collectList()
                    .flatMap(list -> {
                        resp.put("errors", list);
                        resp.put("timestamp", new Date());
                        resp.put("status", HttpStatus.BAD_REQUEST.value());

                        return Mono.just(ResponseEntity.badRequest().body(resp));
                    });
        });
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>>editarCliente(@RequestBody User user,@PathVariable String id){
        return service.findById(id).flatMap(element -> {
                    element.setUsuario(user.getUsuario());
                    element.setPassword(user.getPassword());

                    return service.save(element);
                }).map(element -> ResponseEntity.created(URI.create("/api/users".concat(element.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(element))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>>eliminarUsuario(@PathVariable String id){
        return service.findById(id).flatMap(element ->{
            return service.delete(element).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
