package com.scm.services.implementation;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.helpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepository;
import com.scm.services.ContactService;
import com.scm.entities.User;

@Service
public class ContactServicesImplementation implements ContactService {

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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
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

}
