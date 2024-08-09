package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.subscriber.SubscriberCreateRequestDTO;
import com.example.hoidanit.dto.request.subscriber.SubscriberUpdateRequestDTO;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Skill;
import com.example.hoidanit.model.Subscriber;
import com.example.hoidanit.repository.SkillRepository;
import com.example.hoidanit.repository.SubscriberRepository;
import com.example.hoidanit.repository.UserRepository;
import com.example.hoidanit.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Override
    public Subscriber createSubscriber(SubscriberCreateRequestDTO subscriberCreateRequestDTO) {

//        if (!userRepository.existsByEmail(subscriberCreateRequestDTO.getEmail())) {
//            throw new ResourceNotFoundException("User not found with email: " + subscriberCreateRequestDTO.getEmail());
//        }
        List<Skill> skills = new ArrayList<>();
        for(SubscriberCreateRequestDTO.Skill i : subscriberCreateRequestDTO.getSkills()){
            if(skillRepository.existsById(i.getId())){
                skills.add(skillRepository.findById(i.getId()).orElse(null));
            }
        }

        return subscriberRepository.save(Subscriber.builder()
                .email(subscriberCreateRequestDTO.getEmail())
                .name(subscriberCreateRequestDTO.getName())
                .skills(skills)
                .build());
    }

    @Override
    public Subscriber updateSubscriber(SubscriberUpdateRequestDTO subscriberUpdateRequestDTO) {

        Subscriber dbSubscriber = getSubscriber(subscriberUpdateRequestDTO.getId());

        List<Skill> skills = new ArrayList<>();
        for(SubscriberCreateRequestDTO.Skill i : subscriberUpdateRequestDTO.getSkills()){
            if(skillRepository.existsById(i.getId())){
                skills.add(skillRepository.findById(i.getId()).orElse(null));
            }
        }
        dbSubscriber.setSkills(skills);
        return subscriberRepository.save(dbSubscriber);
    }

    @Override
    public Subscriber getSubscriber(long id) {
        return subscriberRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subscriber not found"));
    }
}
