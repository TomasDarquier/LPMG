package com.example.gateway.controllers;

import com.example.gateway.clients.UserActivitiesClient;
import com.example.gateway.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.stream.Collectors;

import static com.example.gateway.dtos.OauthProvider.*;

@RestController
@RequiredArgsConstructor
public class UserBehaviorController {

    private final UserActivitiesClient userActivitiesClient;

    @GetMapping("/access-control")
    public ModelAndView accessControl(@AuthenticationPrincipal OidcUser user) {

        String subject = user.getSubject();

        UserDto newUser = new UserDto(
                        subject.startsWith("google-oauth2") ? GOOGLE : USER_AND_PASSWORD,
                        (subject.substring(subject.indexOf("|") + 1)),
                        user.getEmail(),
                        user.getFullName()
        );

        //TODO arreglar excepciones de cuando se repite el mail y hay error
        //
        //
        userActivitiesClient.accessActivities(newUser);

        return new ModelAndView("redirect:/");
    }

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal OidcUser user) {
        //cuando este el front reemplazar esto por una llamada la front
        return new ModelAndView("home", Collections.singletonMap("claims", user.getClaims()));
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin(@AuthenticationPrincipal OidcUser user) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        return "Hello, Admin!<br/><br/>User: " + user.getFullName() + "!<br/><br/>Authorities: " + authorities;
    }


}