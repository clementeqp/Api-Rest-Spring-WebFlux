package com.api.rest.reactiva.functional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ContactoRouter {

    @Bean
    public RouterFunction<ServerResponse> routerContacto(ContactoHandler cHandler){

        return RouterFunctions
                .route(POST("/functional/contactos"), cHandler::insertContacto)
                .andRoute(PUT("/functional/contactos/{id}"), cHandler::updateContacto)
                .andRoute(DELETE("/functional/contactos/{id}"), cHandler::deleteContacto)
                .andRoute(GET("/functional/contactos"), cHandler::listContactos)
                .andRoute(GET("/functional/contactos/{id}"), cHandler::getContactoById)
                .andRoute(GET("fuctional/contactos/email/{email}"), cHandler::getContactoByEmail);
    }
}
