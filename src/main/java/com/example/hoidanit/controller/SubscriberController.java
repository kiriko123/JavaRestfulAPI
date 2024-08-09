package com.example.hoidanit.controller;

import com.example.hoidanit.dto.request.subscriber.SubscriberCreateRequestDTO;
import com.example.hoidanit.dto.request.subscriber.SubscriberUpdateRequestDTO;
import com.example.hoidanit.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1/subscribers")
public class SubscriberController {

    private final SubscriberService subscriberService;

    @PostMapping
    public ResponseEntity<?> createSub(@Validated @RequestBody SubscriberCreateRequestDTO subscriberCreateRequestDTO) {
        log.info("Create subscriber : {}", subscriberCreateRequestDTO);
        return ResponseEntity.ok().body(subscriberService.createSubscriber(subscriberCreateRequestDTO));
    }

    @PutMapping
    public ResponseEntity<?> updateSub(@Validated @RequestBody SubscriberUpdateRequestDTO subscriberUpdateRequestDTO) {
        log.info("Update subscriber : {}", subscriberUpdateRequestDTO);
        return ResponseEntity.ok().body(subscriberService.updateSubscriber(subscriberUpdateRequestDTO));
    }

}
