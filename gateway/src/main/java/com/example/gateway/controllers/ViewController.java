package com.example.gateway.controllers;

import com.example.gateway.clients.RequestServiceClient;
import com.example.gateway.clients.UserClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        return new ModelAndView("home");
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal OidcUser user) {
        return null;
    }

    @GetMapping("/documentation")
    public String documentation(@AuthenticationPrincipal OidcUser user) {
        return null;
    }

    @GetMapping("/guide")
    public String guide(@AuthenticationPrincipal OidcUser user) {
        return null;
    }
}
