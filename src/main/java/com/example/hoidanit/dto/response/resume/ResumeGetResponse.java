package com.example.hoidanit.dto.response.resume;

import com.example.hoidanit.model.Resume;
import com.example.hoidanit.util.constant.StatusEnum;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@Builder
public class ResumeGetResponse implements Serializable {
    private long id;
    private String email;
    private String url;
    private StatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;
    private String updatedBy;
    private String createdBy;

    private User user;

    private Job job;

    public static ResumeGetResponse fromResumeToResumeGetResponse(Resume resume) {
        return ResumeGetResponse.builder()
                .id(resume.getId())
                .email(resume.getEmail())
                .url(resume.getUrl())
                .status(resume.getStatus())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .updatedBy(resume.getUpdatedBy())
                .createdBy(resume.getCreatedBy())
                .user(User.builder()
                        .id(resume.getUser().getId())
                        .name(resume.getUser().getUsername())
                        .build())
                .job(Job.builder()
                        .id(resume.getJob().getId())
                        .name(resume.getJob().getName())
                        .build())
                .build();
    }


    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User{
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Job{
        private long id;
        private String name;
    }
}
