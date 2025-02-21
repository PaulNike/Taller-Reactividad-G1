package com.talleresdeprogramacion.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "dishes")
public class Dish {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Field(name = "dish_name")
    private String name;
    @Field
    private double price;
    @Field
    private boolean status;

}
