package org.serverapp.presentation.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws Exception {
        String googleLoginUrl = "/oauth2/authorization/google";
        response.sendRedirect(googleLoginUrl);
    }
}
