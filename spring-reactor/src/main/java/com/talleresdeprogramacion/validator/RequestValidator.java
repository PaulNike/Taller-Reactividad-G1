package com.talleresdeprogramacion.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class RequestValidator {
    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validate(T t){

        if (t == null){
            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        }

        Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
        if(constraintViolations == null || constraintViolations.isEmpty()){
            return Mono.just(t);
        }
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }
}
