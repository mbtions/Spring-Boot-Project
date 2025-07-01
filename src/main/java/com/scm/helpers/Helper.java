package com.scm.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

        // oauth2 providers: google and github
        if (authentication instanceof OAuth2AuthenticationToken) {

            var oAuth2Token = (OAuth2AuthenticationToken) authentication;
            var clientId = oAuth2Token.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User) authentication.getPrincipal();
            String username = "";

            if (clientId.equalsIgnoreCase("google")) {
                // google
                username = oauth2User.getAttribute("email");

            } else if (clientId.equalsIgnoreCase("github")) {
                // github
                username = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email")
                        : oauth2User.getAttribute("login");
            }
            return username;

        } else {
            // self provider: from local database
            return authentication.getName();
        }
    }

    public static String getLinkForEmailVerification(String emailToken) {

        String link = "http://localhost:5050/auth/verify-email?token=" + emailToken;

        return link;
    }

}
