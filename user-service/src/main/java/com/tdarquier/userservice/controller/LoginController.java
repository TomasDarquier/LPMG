package com.tdarquier.userservice.controller;

import com.tdarquier.userservice.model.User;
import com.tdarquier.userservice.service.JwtService;
import com.tdarquier.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/oauth2")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @GetMapping("/loginSuccess")
    public ResponseEntity<String> grantCode(@AuthenticationPrincipal OAuth2User oauth2User) {
        User user = new User();
        user.setEmail(oauth2User.getAttribute("email"));
        user.setName(oauth2User.getAttribute("name"));
        user.setImageUrl(oauth2User.getAttribute("picture"));
        user.setEmailVerified(oauth2User.getAttribute("email_verified"));

        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(jwtService.generateToken(user));
    }

}