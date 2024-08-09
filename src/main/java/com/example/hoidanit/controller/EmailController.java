package com.example.hoidanit.controller;

import com.example.hoidanit.service.EmailService;
import com.example.hoidanit.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;
    @GetMapping
    public String sendSimpleEmail() {
//        emailService.sendSimpleEmail();
//        emailService.sendEmailSync("justahribaka@gmail.com", "Subject", "<h1><b>HELLO</b></h1>", false, true);
        subscriberService.sendSubscribersEmailJobs();
        return "Hello World";
    }
}
