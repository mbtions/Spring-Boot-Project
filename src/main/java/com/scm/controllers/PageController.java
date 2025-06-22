package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");

        // sending data to view
        model.addAttribute("name", "Smart Contract Manager App");
        model.addAttribute("githubRepo", "https://github.com/mbtions");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        System.out.println("About Page loading");
        model.addAttribute("isLogin", false);
        return "about";
    }

    @RequestMapping("/services")
    public String services() {
        System.out.println("Services Page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    // login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // sign up
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        // userForm.setName("Meenakshi");
        // userForm.setEmail("meenakshibharadwaj747@gmail.com");
        // userForm.setPassword("Kiet@1234");
        // userForm.setPhoneNumber("8218524026");
        // userForm.setAbout("My best friend is mother nature🌻 ...");
        return "register";
    }

    // Signing up user
    @PostMapping("/do-register")
    public String processRegistration(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        System.out.println(userForm);

        // validate data
        if (rBindingResult.hasErrors()) {
            return "register";
        }

        // user service
        // UserForm -> User [default values are not added]
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .phoneNumber(userForm.getPhoneNumber())
        // .about(userForm.getAbout())
        // .password(userForm.getPassword())
        // .profilePic(
        // "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png?20150327203541")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic(
                "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png?20150327203541");

        // save to database
        User savedUser = userService.saveUser(user);

        Message message = Message.builder()
                .content("You have successfully signed up!")
                .type(MessageType.green)
                .build();

        // add success message using session
        session.setAttribute("message", message);

        System.out.println("User saved");
        return "redirect:/register";
    }

}
