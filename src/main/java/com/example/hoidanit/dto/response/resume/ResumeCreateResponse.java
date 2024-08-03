package com.example.hoidanit.dto.response.resume;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ResumeCreateResponse {
    private long id;
    private Instant createdAt;
    private String createdBy;
}
