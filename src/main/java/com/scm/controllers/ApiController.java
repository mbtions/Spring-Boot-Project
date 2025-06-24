package com.scm.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.Contact;
import com.scm.services.ContactService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    // get contact
    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        return contactService.getById(contactId);
    }

    // get all contacts

}
