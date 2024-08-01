package com.example.hoidanit.model;

import com.example.hoidanit.util.SecurityUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;
    private String address;
    private String logo;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createAt;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updateAt;

    private String createBy;
    private String updateBy;

    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Job> jobs;


    @PrePersist
    public void handleBeforeCreate(){
        this.createBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "" ;
        this.createAt = Instant.now();
    }
    @PreUpdate
    public void handleBeforeUpdate(){
        this.updateAt = Instant.now();
        this.updateBy = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "" ;
    }
}
