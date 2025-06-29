package com.scm.services.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepository;
import com.scm.services.ContactService;
import com.scm.entities.User;

@Service
public class ContactServicesImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        var oldContact = contactRepository.findById(contact.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found"));

        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setPhoneNumber(contact.getPhoneNumber());
        oldContact.setFavorite(contact.isFavorite());
        oldContact.setAddress(contact.getAddress());
        oldContact.setDescription(contact.getDescription());
        oldContact.setPicture(contact.getPicture());
        oldContact.setSocialLink(contact.getSocialLink());
        oldContact.setWebsiteLink(contact.getWebsiteLink());
        oldContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());

        return contactRepository.save(oldContact);
    }

    @Override
    public List<Contact> getAll() {
        return contactRepository.findAll();
    }

    @Override
    public void delete(String id) {
        var contact = contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
        contactRepository.delete(contact);
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepository.findByUserId(userId);
    }

    @Override
    public Contact getById(String id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with id: " + id));
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection) {
        // sorting
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        // pageable object
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, int page, int size, String sortField,
            String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, int page, int size, String sortField,
            String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortField,
            String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

    @Override
    public Page<Contact> getAllFavoriteContacts(User user, int page, int size, String sortField,
            String sortDirection) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndFavoriteTrue(user, pageable);
    }

    @Override
    public Page<Contact> searchFavoriteByName(String nameKeyword, int page, int size, String sortField,
            String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndFavoriteTrueAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchFavoriteByEmail(String emailKeyword, int page, int size, String sortField,
            String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndFavoriteTrueAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchFavoriteByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortField,
            String sortDirection, User user) {
        Sort sort = sortDirection.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return contactRepository.findByUserAndFavoriteTrueAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }

}
