package com.example.hoidanit.dto.request.resume;

import com.example.hoidanit.util.constant.StatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResumeCreateRequestDTO implements Serializable {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String url;
    private StatusEnum status;
    private User user;
    private Job job;


    @Getter
    public static class User{
        private long id;
    }

    @Getter
    public static class Job{
        private long id;
    }
}
