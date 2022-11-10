package com.api.rest.reactiva.controller;

import com.api.rest.reactiva.documents.Contacto;
import com.api.rest.reactiva.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class ContactoController {

    @Autowired
    private ContactoRepository contactoRepository;

    public Flux<Contacto> getContactos(){
        return contactoRepository.findAll();
    }

    @GetMapping(value = "/contactos/{id}")
    public Mono<ResponseEntity<Contacto>> getContacto(@PathVariable String id){
        return contactoRepository.findById(id)
                .map(contacto -> new ResponseEntity<>(contacto, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @GetMapping(value = "/contactos/email/{email}")
    public Mono<ResponseEntity<Contacto>> getContactoByEmail(@PathVariable String email){
        return contactoRepository.findFirstByEmail(email)
                .map(contacto -> new ResponseEntity<>(contacto, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(value = "/contactos")
    public Mono<ResponseEntity<Contacto>> saveContacto(@RequestBody Contacto contacto){
        return contactoRepository.insert(contacto)
                .map(contactoSave -> new ResponseEntity<>(contactoSave, HttpStatus.CREATED ))
                .defaultIfEmpty(new ResponseEntity<>(contacto, HttpStatus.NOT_ACCEPTABLE));
    }

    @PutMapping(value = "/contactos/{id}")
    public Mono<ResponseEntity<Contacto>> updateContacto(
            @RequestBody Contacto contacto,
            @PathVariable String id){
        return contactoRepository.findById(id)
                .flatMap(contactoUpdated ->{
                    contacto.setId(id);
                    return contactoRepository.save(contacto)
                            .map(c->new ResponseEntity<>(c, HttpStatus.ACCEPTED));
                })
                .defaultIfEmpty(new ResponseEntity<>( HttpStatus.NOT_FOUND));
    }


    public Mono<Void> deleteContacto(@PathVariable String id){
        return contactoRepository.deleteById(id);

    }
}
