package com.talleresdeprogramacion.config;

import com.talleresdeprogramacion.dto.DishDTO;
import com.talleresdeprogramacion.model.Dish;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        //Definiendo una estrategia de Mapeo
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        //Costumizacion de mapeo
        modelMapper.addMappings(new PropertyMap<Dish, DishDTO>() {

            @Override
            protected void configure() {
                map().setNameDishes(source.getName());
                map().setPriceDishes(source.getPrice());
                map().setStatusDishes(source.isStatus());
            }
        });

        return modelMapper;
    }
}
