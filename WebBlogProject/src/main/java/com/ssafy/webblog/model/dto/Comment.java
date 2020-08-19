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
public class Comment {
	/*
	 * commentId articleId commentContent commentor commentorNickname
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "commentid")
	private int commentid;

	private int articleid;
	private String commentcontent;
	private String commentornickname;
	@Column(insertable = false, updatable = false)
	private LocalDateTime commentdate;
	private int commentorid;
}
