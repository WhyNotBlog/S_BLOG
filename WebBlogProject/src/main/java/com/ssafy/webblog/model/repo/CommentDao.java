package com.ssafy.webblog.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Comment;
import com.ssafy.webblog.model.dto.User;

public interface CommentDao extends JpaRepository<Comment, String> {
	// ------
	
	
	// 댓글 번호로 댓글 하나 가져오기
	Comment getCommentByCommentid(int commentid);

	// 글에 해당하는 댓글 리스트 가져오기
	List<Comment> getCommentByArticleid(int articleid);
	
	
	// ------
}
