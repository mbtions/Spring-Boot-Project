package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helpers.AppConstants;
import com.scm.repositories.UserRepository;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");

        // Identify the provider
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        logger.info(authorizedClientRegistrationId);

        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

        oAuth2User.getAttributes().forEach((key, value) -> {
            logger.info("{} => {}", key, value);
        });

        User user1 = new User();
        user1.setUserId(UUID.randomUUID().toString());
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        user1.setEmailVerified(true);
        user1.setEnabled(true);
        user1.setPassword("dummy");

        // google
        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            // google attributes
            user1.setProvider(Providers.GOOGLE);
            user1.setProviderUserId(oAuth2User.getName());
            user1.setEmail(oAuth2User.getAttribute("email").toString());
            user1.setName(oAuth2User.getAttribute("name").toString());
            user1.setProfilePic(oAuth2User.getAttribute("picture").toString());
            user1.setAbout("This account is created with google account.");

        }
        // github
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            // github properties
            String email = oAuth2User.getAttribute("email").toString() != null
                    ? oAuth2User.getAttribute("email").toString()
                    : oAuth2User.getAttribute("login").toString() + "@gmail.com";

            user1.setProvider(Providers.GITHUB);
            user1.setProviderUserId(oAuth2User.getName());
            user1.setEmail(email);
            user1.setName(oAuth2User.getAttribute("login"));
            user1.setProfilePic(oAuth2User.getAttribute("avatar_url").toString());
            user1.setAbout("This account is created with github account.");

        }
        // unknown provider
        else {
            logger.info("Unknown Provider");
        }
        // DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();

        // logger.info(user.getName());

        // user.getAttributes().forEach((key, value) -> {
        // logger.info("{} => {}", key, value);
        // });

        // logger.info(user.getAuthorities().toString());

        // // save data to database
        // User user1 = new User();
        // user1.setEmail(user.getAttribute("email").toString());
        // user1.setName(user.getAttribute("name").toString());
        // user1.setProfilePic(user.getAttribute("picture").toString());
        // user1.setPassword(user.getAttribute("password"));
        // user1.setUserId(UUID.randomUUID().toString());
        // user1.setProvider(Providers.GOOGLE);
        // user1.setEnabled(true);
        // user1.setEmailVerified(true);
        // user1.setProviderUserId(user.getName());
        // user1.setRoleList(List.of(AppConstants.ROLE_USER));
        // user1.setAbout("This account is created with google account.");

        // save the user to database
        User user2 = userRepository.findByEmail(user1.getEmail()).orElse(null);

        // new user
        if (user2 == null) {
            userRepository.save(user1);

            logger.info("New User saved");
        }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");

    }

}
