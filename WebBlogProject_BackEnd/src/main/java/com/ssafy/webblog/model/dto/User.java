package com.ssafy.webblog.model.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @Column(name ="id")
    private int id;   
    private String email;   
    
    private String nickname;
    private String password;    

    @Column(insertable = false, updatable = false)
    private LocalDateTime createdate;
    
    private String picture;
    private String introduce;
    private String giturl;
    private int loginkind;
}
