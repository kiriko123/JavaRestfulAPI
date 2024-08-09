package com.example.hoidanit.service;

import com.example.hoidanit.dto.request.subscriber.SubscriberCreateRequestDTO;
import com.example.hoidanit.dto.request.subscriber.SubscriberUpdateRequestDTO;
import com.example.hoidanit.model.Subscriber;

public interface SubscriberService {
    Subscriber createSubscriber(SubscriberCreateRequestDTO subscriberCreateRequestDTO);
    Subscriber updateSubscriber(SubscriberUpdateRequestDTO subscriberUpdateRequestDTO);
    Subscriber getSubscriber(long id);
}
