package org.serverapp.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.serverapp.application.AccountService;
import org.serverapp.domain.entity.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AccountService accountService;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();

            accountService.findByEmail(email).ifPresentOrElse(user -> {
                DefaultOAuth2User newUser = new DefaultOAuth2User(
                        List.of(new SimpleGrantedAuthority(user.getRole().name())),
                        attributes,
                        "email"
                );
                Authentication securityAuth = new OAuth2AuthenticationToken(newUser,
                        List.of(new SimpleGrantedAuthority(user.getRole().name())),
                        oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            }, () -> {
                Account account = Account.builder()
                        .email(email)
                        .name(name)
                        .role(UserRole.ROLE_USER)
                        .source(RegistrationSource.GOOGLE)
                        .build();
                accountService.save(account);

                Authentication securityAuth = getAuthentication(attributes, account, oAuth2AuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(securityAuth);
            });
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private static Authentication getAuthentication(Map<String, Object> attributes, Account account, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        Map<String, Object> updatedAttributes = new HashMap<>(attributes);
        updatedAttributes.put("id", account.getId());

        DefaultOAuth2User newUser = new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(account.getRole().name())),
                updatedAttributes,
                "email"
        );
        Authentication securityAuth = new OAuth2AuthenticationToken(newUser,
                List.of(new SimpleGrantedAuthority(account.getRole().name())),
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        return securityAuth;
    }
}
