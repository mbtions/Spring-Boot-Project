package com.scm.controllers;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/dashboard")
    public String userDashboard() {
        return "user/dashboard";
    }

    // @PostMapping("/update")
    // public String updateProfile(
    // // @PathVariable("userId") String userId,
    // @Valid @ModelAttribute("userForm") UserForm userForm,
    // BindingResult bindingResult,
    // Authentication authentication,
    // Model model,
    // HttpSession session) {
    //
    // if (bindingResult.hasErrors()) {
    // model.addAttribute("user", Helper.getEmailOfLoggedInUser(null));
    // return "user/profile";
    // }
    //
    // User user =
    // userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));
    //
    // if (userForm.getProfileImage() != null &&
    // !userForm.getProfileImage().isEmpty()) {
    // logger.info("Image file not empty");
    // String fileName = UUID.randomUUID().toString();
    // String imageUrl = imageService.uploadImage(userForm.getProfileImage(),
    // fileName);
    // user.setCloudinaryImagePublicId(imageUrl);
    // user.setProfilePic(imageUrl);
    // userForm.setPicture(imageUrl);
    // } else {
    // logger.info("empty file");
    // }
    //
    // var updatedUser = userService.updateUser(user);
    // logger.info("Updated User: " + updatedUser);
    //
    // session.setAttribute("message", Message.builder()
    // .content("Profile updated successfully!")
    // .type(MessageType.green)
    // .build());
    //
    // User loggedInUser = (User) model.getAttribute("loggedInUser");
    //
    // model.addAttribute("userForm", UserForm.builder()
    // .name(loggedInUser.getName())
    // .email(loggedInUser.getEmail())
    // .about(loggedInUser.getAbout())
    // .phoneNumber(loggedInUser.getPhoneNumber())
    // .build());
    //
    // return "redirect:/user/profile";
    // }
    //
    // @PostMapping("/update")
    // public String updateProfile(
    // @Valid @ModelAttribute("userForm") UserForm userForm,
    // BindingResult bindingResult,
    // Authentication authentication,
    // Model model,
    // HttpSession session) {
    //
    // // Return to profile if validation fails
    // if (bindingResult.hasErrors()) {
    // return "user/profile";
    // }
    //
    // // Fetch currently logged-in user
    // String email = Helper.getEmailOfLoggedInUser(authentication);
    // User user = userService.getUserByEmail(email);
    //
    // // Update fields
    // user.setName(userForm.getName());
    // user.setEmail(userForm.getEmail());
    // user.setAbout(userForm.getAbout());
    // user.setPhoneNumber(userForm.getPhoneNumber());
    //
    // // Handle profile image upload if a new file is selected
    // MultipartFile profileImage = userForm.getProfileImage();
    // if (profileImage != null && !profileImage.isEmpty()) {
    // logger.info("Uploading new profile image...");
    //
    // String fileName = UUID.randomUUID().toString();
    // String imageUrl = imageService.uploadImage(profileImage, fileName);
    //
    // user.setProfilePic(imageUrl);
    // user.setCloudinaryImagePublicId(imageUrl);
    // }
    //
    // // Save updated user
    // userService.updateUser(user);
    //
    // // Add success message
    // session.setAttribute("message", Message.builder()
    // .content("Profile updated successfully!")
    // .type(MessageType.green)
    // .build());
    //
    // logger.info("Updated User Information");
    //
    // return "redirect:/user/profile";
    // }

    @GetMapping("/profile")
    public String userProfile(Authentication authentication, Model model) {

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        UserForm userForm = UserForm.builder()
                .name(user.getName())
                .email(user.getEmail())
                .about(user.getAbout())
                .phoneNumber(user.getPhoneNumber())
                .picture(user.getProfilePic())
                .build();

        model.addAttribute("userForm", userForm);

        return "user/profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            Authentication authentication,
            HttpSession session) {

        if (bindingResult.hasErrors()) {
            session.setAttribute("message",
                    Message.builder().content("Something went wrong!").type(MessageType.red).build());
            return "/user/profile";
        }

        User user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());

        if (userForm.getProfileImage() != null && !userForm.getProfileImage().isEmpty()) {
            logger.info("Uploading new profile image...");
            String fileName = UUID.randomUUID().toString();
            String imageUrl = imageService.uploadImage(userForm.getProfileImage(), fileName);
            user.setProfilePic(imageUrl);
            user.setCloudinaryImagePublicId(imageUrl);
            userForm.setPicture(imageUrl);
        }

        Optional<User> optionalUpdatedUser = userService.updateUser(user);

        if (optionalUpdatedUser.isPresent()) {
            session.setAttribute("message", Message.builder()
                    .content("Profile updated successfully!")
                    .type(MessageType.green)
                    .build());
        } else {
            session.setAttribute("message", Message.builder()
                    .content("Failed to update profile!")
                    .type(MessageType.red)
                    .build());
        }

        return "redirect:/user/profile";
    }

}
