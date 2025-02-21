package com.talleresdeprogramacion.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
@Order(-1)
public class WebExceptionHandler extends AbstractErrorWebExceptionHandler {

    public WebExceptionHandler(ErrorAttributes errorAttributes,
                               WebProperties.Resources resources,
                               ApplicationContext applicationContext,
                               ServerCodecConfigurer configurer) {
        super(errorAttributes, resources, applicationContext);
        setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

        Map<String, Object> generalError = getErrorAttributes(request,
                ErrorAttributeOptions.defaults());
        /* INICIO CUSTOMIZADO*/
        Map<String, Object> customError = new HashMap<>();

        int statusCode = Integer.parseInt(String.valueOf(generalError.get("status").toString()));

        //EXTRAYENDO EL MENSAJE DE ERROR DE LA EXEPCION CAPTURADA
        Throwable err = getError(request);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        switch (statusCode){
            case 400, 422 -> {
                customError.put("message",err.getMessage());
                customError.put("status", 400);
                httpStatus = httpStatus.BAD_REQUEST;
            }
            case 401, 403 -> {
                customError.put("message",err.getMessage());
                customError.put("status", 401);
                httpStatus = httpStatus.UNAUTHORIZED;
            }
            case 404 -> {
                customError.put("message",err.getMessage());
                customError.put("status", 404);
                httpStatus = httpStatus.NOT_FOUND;
            }
            case 500 -> {
                customError.put("message",err.getMessage());
                customError.put("status", 500);
                //httpStatus = httpStatus.NOT_FOUND;
            }
            default -> {
                customError.put("message",err.getMessage());
                customError.put("status", 409);
                httpStatus = httpStatus.CONFLICT;
            }
        }
        /* FIN CUSTOMIZADO*/

        return ServerResponse.status(httpStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(customError));
    }
}
