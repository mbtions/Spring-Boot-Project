package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import com.scm.entities.User;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, HttpSession session) {
        User user = userService.getUserByEmailToken(token);

        if (user != null) {
            // user is fetched from db
            if (user.getEmailToken().equals(token)) {
                user.setEmailVerified(true);
                user.setEnabled(true);
                userService.updateUser(user);

                session.setAttribute("message", Message.builder().content(
                        "Your email has been verified successfully! You are now enabled to use the application.")
                        .type(MessageType.green).build());
                return "success_page";
            }

            // return "error_page";

        }

        session.setAttribute("message", Message.builder().content("Oops! Something went wrong. Email not verified.")
                .type(MessageType.red).build());
        return "error_page";
    }
}
