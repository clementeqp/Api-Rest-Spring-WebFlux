package com.api.rest.reactiva.functional;

import com.api.rest.reactiva.documents.Contacto;
import com.api.rest.reactiva.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ContactoHandler {

    @Autowired
    private ContactoRepository contactoRepository;

    private final Mono<ServerResponse> response404 = ServerResponse.notFound().build();
    private final Mono<ServerResponse> response406 = ServerResponse.status(HttpStatus.NOT_ACCEPTABLE).build();

    // Listar Contactos
    public Mono<ServerResponse> listContactos(ServerRequest request) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(contactoRepository.findAll(), Contacto.class);
    }

    //Listar Contacto
    public Mono<ServerResponse> getContactoById(ServerRequest request) {
        String id = request.pathVariable("id");
        return contactoRepository.findById(id)
                .flatMap(c ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(c)))
                .switchIfEmpty(response404);
    }


    //Listar Contacto
    public Mono<ServerResponse> getContactoByEmail(ServerRequest request) {
        return contactoRepository.findFirstByEmail(request.pathVariable("email"))
                .flatMap(c ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(c)))
                .switchIfEmpty(response404);
    }


    //Insertar un contacto
    public Mono<ServerResponse> insertContacto(ServerRequest request){
        Mono<Contacto> contactoMono = request.bodyToMono(Contacto.class);

        return contactoMono.flatMap(c->
                contactoRepository.save(c)
                        .flatMap(cSave->ServerResponse.accepted()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(fromValue(cSave))))
                .switchIfEmpty(response406);
    }


    public Mono<ServerResponse> updateContacto (ServerRequest request){

        Mono<Contacto> contactoMono = request.bodyToMono(Contacto.class);
        String id = request.pathVariable("id");

        Mono<Contacto> contactoMonoUpdated = contactoMono.flatMap(c->
                contactoRepository.findById(id)
                        .flatMap(c1 ->contactoRepository.save(
                                Contacto.builder()
                                        .id(c1.getId())
                                        .phone(c.getPhone())
                                        .name(c.getName())
                                        .email(c.getEmail())
                                        .build())));
        return contactoMonoUpdated.flatMap(c->
                ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(c)));
    }


    public Mono<ServerResponse> deleteContacto(ServerRequest request){
        String id = request.pathVariable("id");
        Mono<Void> cDelete = contactoRepository.deleteById(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(cDelete, Void.class);
    }

}
