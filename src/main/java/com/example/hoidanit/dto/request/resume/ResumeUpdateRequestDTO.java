package com.example.hoidanit.dto.request.resume;

import com.example.hoidanit.util.constant.StatusEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class ResumeUpdateRequestDTO implements Serializable {
    @NotNull
    @Min(1)
    private long id;

    private StatusEnum status;
}
