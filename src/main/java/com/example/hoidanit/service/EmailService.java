package com.example.hoidanit.service;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    void sendSimpleEmail();
    void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml);
    void sendEmailFromTemplateSync(String to, String subject, String templateName, String username, Object value);
}
