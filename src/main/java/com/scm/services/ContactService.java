package com.scm.services;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

        Contact save(Contact contact);

        Contact update(Contact contact);

        List<Contact> getAll();

        Contact getById(String id);

        void delete(String id);

        Page<Contact> searchByName(String nameKeyword, int page, int size, String sortField, String sortDirection,
                        User user);

        Page<Contact> searchByEmail(String emailKeyword, int page, int size, String sortField,
                        String sortDirection, User user);

        Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortField,
                        String sortDirection, User user);

        List<Contact> getByUserId(String userId);

        Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);

        Page<Contact> getAllFavoriteContacts(User user, int page, int size, String sortField,
                        String sortDirection);

        Page<Contact> searchFavoriteByName(String nameKeyword, int page, int size, String sortField,
                        String sortDirection,
                        User user);

        Page<Contact> searchFavoriteByEmail(String emailKeyword, int page, int size, String sortField,
                        String sortDirection, User user);

        Page<Contact> searchFavoriteByPhoneNumber(String phoneNumberKeyword, int page, int size, String sortField,
                        String sortDirection, User user);

        long countContactsByUser(User user);

        long countFavoriteContactsByUser(User user);

}
