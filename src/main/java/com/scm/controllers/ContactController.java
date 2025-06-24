package com.scm.controllers;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.forms.ContactForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    Logger logger = LoggerFactory.getLogger(ContactController.class);

    @RequestMapping("/add")
    public String addContact(Model model) {
        ContactForm contactForm = new ContactForm();
        contactForm.setFavorite(true);
        model.addAttribute("contactForm", contactForm);

        return "user/add_contact";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult rBindingResult,
            Authentication authentication, HttpSession session) {
        if (rBindingResult.hasErrors()) {
            session.setAttribute("message",
                    Message.builder()
                            .content("Please enter valid contact details")
                            .type(MessageType.red)
                            .build());

            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        String filename = UUID.randomUUID().toString();
        String fileURL = imageService.uploadImage(contactForm.getContactImage(), filename);

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setEmail(contactForm.getEmail());
        contact.setUser(user);
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setSocialLink(contactForm.getLinkedInLink());
        contact.setFavorite(contactForm.isFavorite());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setPicture(fileURL);
        contact.setCloudinaryImagePublicId(filename);

        contactService.save(contact);
        System.out.println(contactForm);

        session.setAttribute("message",
                Message.builder()
                        .content("Contact has been saved successfully.")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contacts/add";
    }

    @RequestMapping
    public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        // get all contacts
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "/user/contacts";
    }

    // search handler
    @GetMapping("/search")
    public String searchHandler(@RequestParam("searchBy") String searchField,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam("keyword") String fieldValue, Model model, Authentication authentication) {

        Page<Contact> pageContact = null;

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        if (searchField.equalsIgnoreCase("name")) {
            pageContact = contactService.searchByName(fieldValue, page, size, sortBy, direction, user);
        } else if (searchField.equalsIgnoreCase("email")) {
            pageContact = contactService.searchByEmail(fieldValue, page, size, sortBy, direction, user);
        } else if (searchField.equalsIgnoreCase("phoneNumber")) {
            pageContact = contactService.searchByPhoneNumber(fieldValue, page, size, sortBy, direction, user);
        }

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("searchBy", searchField);
        model.addAttribute("keyword", fieldValue);
        return "user/search";
    }

}
