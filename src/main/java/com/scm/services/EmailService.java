package com.scm.services;

public interface EmailService {

    // method to send email
    void sendEmail(String to, String subject, String body);

    void sendEmailWithHtml(String to, String subject, String body);

    void sendEmailWithAttachment(String to, String subject, String body);
}
