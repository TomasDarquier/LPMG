package com.example.gateway.controllers;

import com.example.gateway.clients.UserActivitiesClient;
import com.example.gateway.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

import static com.example.gateway.dtos.OauthProvider.*;

@RequestMapping("/behavior")
@RestController
@RequiredArgsConstructor
public class UserBehaviorController {

    private final UserActivitiesClient userActivitiesClient;

    // Once the user logs in, this method send the info to register their data in
    // the application DB, or if he already exists, to register only the login activity
    @GetMapping("/access-control")
    public ModelAndView accessControl(@AuthenticationPrincipal OidcUser user) {
        String subject = user.getSubject();

        UserDto newUser = new UserDto(
                        subject.startsWith("google-oauth2") ? GOOGLE : USER_AND_PASSWORD,
                        (subject.substring(subject.indexOf("|") + 1)),
                        user.getEmail(),
                        user.getFullName()
        );

        userActivitiesClient.registerAccessActivities(newUser);
        return new ModelAndView("redirect:/");
    }

    // This method is to verify manually if your user is an Admin
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String admin(@AuthenticationPrincipal OidcUser user) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        return "Hello, Admin!<br/><br/>User: " + user.getFullName() + "!<br/><br/>Authorities: " + authorities;
    }


}