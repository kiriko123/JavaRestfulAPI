package com.example.hoidanit.service.impl;

import com.example.hoidanit.dto.request.subscriber.SubscriberCreateRequestDTO;
import com.example.hoidanit.dto.request.subscriber.SubscriberUpdateRequestDTO;
import com.example.hoidanit.dto.response.ResEmailJob;
import com.example.hoidanit.exception.ResourceNotFoundException;
import com.example.hoidanit.model.Job;
import com.example.hoidanit.model.Skill;
import com.example.hoidanit.model.Subscriber;
import com.example.hoidanit.repository.JobRepository;
import com.example.hoidanit.repository.SkillRepository;
import com.example.hoidanit.repository.SubscriberRepository;
import com.example.hoidanit.repository.UserRepository;
import com.example.hoidanit.service.EmailService;
import com.example.hoidanit.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

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

    @Override
    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (!listSubs.isEmpty()) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && !listSkills.isEmpty()) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && !listJobs.isEmpty()) {
                         List<ResEmailJob> arr = listJobs.stream().map(
                            job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                listJobs);
                    }
                }
            }
        }
    }

    @Override
    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new
                        ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }
}
