package com.ssafy.webblog.model.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.dto.Comment;
import com.ssafy.webblog.model.repo.CommentDao;

@Service
@Transactional
public class CommentService {

	@Autowired
	CommentDao cDao;
	
	
	//게시글에 달린 댓글리스트 추출
	public List<Comment> getCommentListByArticleid(String articleid) {
		List<Comment> result = null;
		result = cDao.getCommentByArticleid(Integer.parseInt(articleid));

		return result;
	}
	//한개의 댓글 정보 추출
	public Comment getCommentByCommentid(String commentid) {
		return cDao.getCommentByCommentid(Integer.parseInt(commentid));
	}
	
	// 삽입
	public Comment insertComment(Comment comment) {
		Comment result = cDao.save(comment);
		return result;
	}

	// 삭제
	public void deleteComment(String commentid) {
		Comment deleteComment = cDao.getCommentByCommentid(Integer.parseInt(commentid));
		cDao.delete(deleteComment);
	}

	// 업데이트
	public Comment updateComment(Comment comment) {
		Comment input = new Comment();
		input.setArticleid(comment.getArticleid());
		input.setCommentcontent(comment.getCommentcontent());
		input.setCommentid(comment.getCommentid());
		input.setCommentornickname(comment.getCommentornickname());
		Comment result = cDao.save(input);
		return result;
	}


}
