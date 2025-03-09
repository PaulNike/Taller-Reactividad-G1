package com.talleresdeprogramacion.config;

import com.talleresdeprogramacion.dto.ClientDTO;
import com.talleresdeprogramacion.dto.DishDTO;
import com.talleresdeprogramacion.model.Client;
import com.talleresdeprogramacion.model.Dish;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class MapperConfig {

    @Bean(name = "defaultMapper")
    public ModelMapper defaultMapper(){
        return new ModelMapper();
    }

    @Bean(name = "clientMapper")
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        //Definiendo una estrategia de Mapeo
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //Customizacion de mapeo ESCRITURA
        modelMapper.createTypeMap(ClientDTO.class, Client.class)
                .addMapping(ClientDTO::getName, (dest, v) -> dest.setFirstName((String) v))
                .addMapping(ClientDTO::getSurname, (dest, v) -> dest.setLastName((String) v))
                .addMapping(ClientDTO::getBirthDateClient, (dest, v) -> dest.setBirthDate((LocalDate) v))
                .addMapping(ClientDTO::getPicture, (dest, v) -> dest.setUrlPhoto((String) v));

        //Customizacion de mapeo LECTURA
        modelMapper.createTypeMap(Client.class, ClientDTO.class)
                .addMapping(Client::getFirstName, (dest, v) -> dest.setName((String) v))
                .addMapping(Client::getLastName, (dest, v) -> dest.setSurname((String) v))
                .addMapping(Client::getBirthDate, (dest, v) -> dest.setBirthDateClient((LocalDate) v))
                .addMapping(Client::getUrlPhoto, (dest, v) -> dest.setPicture((String) v));

        return modelMapper;
    }
}
