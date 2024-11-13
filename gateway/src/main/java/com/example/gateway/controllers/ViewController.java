package com.example.gateway.controllers;

import com.example.gateway.clients.RequestServiceClient;
import com.example.gateway.clients.UserClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.stream.Collectors;

@RestController
public class ViewController {

    final
    RequestServiceClient requestServiceClient;

    final
    UserClient userClient;

    public ViewController(RequestServiceClient requestServiceClient, UserClient userClient) {
        this.requestServiceClient = requestServiceClient;
        this.userClient = userClient;
    }

    @GetMapping("")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        Long userId = userClient.getIdByEmail(user.getEmail());
        return new ModelAndView("index", Collections.singletonMap("modelId",userId));
    }

    @PostMapping("/submit/{id}")
    public ResponseEntity<Void> test(@PathVariable String id, @RequestBody String jsonValue){
        requestServiceClient.requestCodeGeneration(jsonValue, Long.valueOf(id));
        // como devuelvo 204, el form no redirecciona
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OidcUser user) {
        return null;
    }

    @GetMapping("/documentation")
    public ModelAndView documentation(@AuthenticationPrincipal OidcUser user) {
        return new ModelAndView("documentation");
    }

    @GetMapping("/guide")
    public String guide(@AuthenticationPrincipal OidcUser user) {
        return null;
    }
}
