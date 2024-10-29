package com.tdarquier.tfg.generation_service.authentication;

import org.springframework.stereotype.Component;

@Component
public class RequestContext {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    public void initialize(String jwt) {
        CONTEXT.set(jwt);
    }

    public String getJwt() {
        return CONTEXT.get();
    }

    public void clear() {
        CONTEXT.remove();
    }
}
