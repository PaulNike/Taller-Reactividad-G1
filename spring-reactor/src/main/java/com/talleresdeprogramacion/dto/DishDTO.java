package com.talleresdeprogramacion.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {

    private String id;
    @NotNull(message = "El atributo nameDishes NO puede ser nulo")
    @Size(min = 2, max = 20)
    @Pattern(regexp = "^[A-Za-záéíóúñÁÉÍÓÚÑ ]+$", message = "Debe cumplir con el pattern")
    private String nameDishes;
    @NotNull
    //@Min(value = 1)
    //@Max(value = 999, message = "El valor debe ser inferior o igual a 999")
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "999.99")
    private double priceDishes;
    @NotNull
    private boolean statusDishes;
    @NotNull
    @Email
    private String email;
}
