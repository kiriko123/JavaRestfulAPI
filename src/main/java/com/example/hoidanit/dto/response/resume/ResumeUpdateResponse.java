package com.example.hoidanit.dto.response.resume;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class ResumeUpdateResponse {
    private Instant updatedAt;
    private String updatedBy;
}
