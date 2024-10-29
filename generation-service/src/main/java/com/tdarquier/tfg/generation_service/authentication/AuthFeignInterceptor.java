package com.tdarquier.tfg.generation_service.authentication;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

// esta clase agrega el JWT enviado en el header de kafka al request
// que se esta haciendo via feign. De esta manera se hace un request seguro
@Component
public class AuthFeignInterceptor implements RequestInterceptor {

    private final RequestContext requestContext;

    public AuthFeignInterceptor(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Override
    public void apply(RequestTemplate template) {
        String jwt = requestContext.getJwt();
        if(jwt != null) {
            template.header(HttpHeaders.AUTHORIZATION, jwt);
        }
    }
}
