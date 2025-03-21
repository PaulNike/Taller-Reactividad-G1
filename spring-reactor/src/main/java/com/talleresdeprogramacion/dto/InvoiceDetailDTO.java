package com.talleresdeprogramacion.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.talleresdeprogramacion.model.Dish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceDetailDTO {
    private int quantity;
    private DishDTO dish;
}
