package com.example.hoidanit.dto.request;

import com.example.hoidanit.util.constant.LevelEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Getter
public class JobRequestDTO implements Serializable {
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    @NotNull
    @Min(0)
    private double salary;
    @NotNull
    @Min(0)
    private int quantity;
    @NotNull
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    @NotNull
    private boolean active;

    private List<Skill> skills;

    @Getter
    @Setter
    //@NoArgsConstructor
    public static class Skill{
        private Long id;
    }
}
