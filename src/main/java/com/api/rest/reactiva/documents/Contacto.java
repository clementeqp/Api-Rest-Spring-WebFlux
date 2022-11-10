package com.api.rest.reactiva.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "contacto")
public class Contacto {

    private String id;
    private String name;
    private String email;
    private String phone;
}
