package com.ssafy.webblog.model.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
/*임시로 일단 만들었습니다*/
//글번호	제목	내용	작성자id	작성자nickname	카테고리	작성일자	수정여부
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="articleid")
    private int articleid;
    
    private String title;
    
    private String content;
    
    private String editornickname;
    
    private String category;
    
    @Column(insertable = false, updatable = false)
    private LocalDateTime editdate;
    
    private int modify;
    private int hits;
    
    private int writerid;
}
