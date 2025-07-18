package com.scm.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

import java.util.*;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    Page<Contact> findByUser(User user, Pageable pageable);

    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(String userId);

    Page<Contact> findByUserAndNameContaining(User user, String nameKeyword, Pageable pageable);

    Page<Contact> findByUserAndEmailContaining(User user, String emailKeyword, Pageable pageable);

    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneKeyword, Pageable pageable);

    Page<Contact> findByUserAndFavoriteTrue(User user, Pageable pageable);

    Page<Contact> findByUserAndFavoriteTrueAndNameContaining(User user, String nameKeyword, Pageable pageable);

    Page<Contact> findByUserAndFavoriteTrueAndPhoneNumberContaining(User user, String phoneKeyword, Pageable pageable);

    Page<Contact> findByUserAndFavoriteTrueAndEmailContaining(User user, String emailKeyword, Pageable pageable);

    long countByUser(User user);

    long countByUserAndFavoriteTrue(User user);

}
