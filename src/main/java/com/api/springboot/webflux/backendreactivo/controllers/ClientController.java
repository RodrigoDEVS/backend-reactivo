package com.api.springboot.webflux.backendreactivo.controllers;

import com.api.springboot.webflux.backendreactivo.models.Client;
import com.api.springboot.webflux.backendreactivo.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.print.attribute.standard.Media;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @PostMapping("/registro")
    public Mono<ResponseEntity<Client>> registrarCliente(Client client) {
        return service.save(client)
                .map(element -> ResponseEntity.created(URI.create("/api/clients".concat(element.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(element));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<Client>>> listarClientes(){
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Client>> verDetallesCliente(@PathVariable String id){
        return service.findByid(id).map(element -> ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(element))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> guardarCliente( @RequestBody Mono<Client> monoClient){
        Map<String, Object> resp = new HashMap<>();

        return monoClient.flatMap(client -> {
            return service.save(client).map(element -> {
                resp.put("client", element);
                resp.put("mensaje", "Cliente guardado con éxito");
                resp.put("timestamp", new Date());
                return ResponseEntity
                        .created(URI.create(("/api/clients".concat(element.getId()))))
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
    public Mono<ResponseEntity<Client>>editarCliente(@RequestBody Client client,@PathVariable String id){
        return service.findByid(id).flatMap(element -> {
            element.setNombre(client.getNombre());
            element.setApellido(client.getApellido());
            element.setEdad(client.getEdad());
            element.setSueldo(client.getSueldo());

            return service.save(element);
        }).map(element -> ResponseEntity.created(URI.create("/api/clients".concat(element.getId())))
                .contentType(MediaType.APPLICATION_JSON)
                .body(element))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>>eliminarCliente(@PathVariable String id){
        return service.findByid(id).flatMap(element ->{
            return service.delete(element).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
        }).defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }
}
