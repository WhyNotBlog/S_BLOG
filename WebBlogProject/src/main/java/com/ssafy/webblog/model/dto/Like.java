package com.ssafy.webblog.model.dto;

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
/*임시로 일단 만들었습니다*/
//글번호	제목	내용	작성자id	작성자nickname	카테고리	작성일자	수정여부
public class Like {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="likekey")
    private int likekey;
    
    private int userid;
    
    private int articleid;
    
}
