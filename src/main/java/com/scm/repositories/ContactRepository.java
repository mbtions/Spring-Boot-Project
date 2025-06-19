package com.scm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

import com.scm.entities.Contact;
import com.scm.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {

    List<Contact> findByUser(User user);

    @Query("SELECT c FROM CONTACT c WHERE c.user.id = :userId")
    List<Contact> findByUserId(String userId);

}
