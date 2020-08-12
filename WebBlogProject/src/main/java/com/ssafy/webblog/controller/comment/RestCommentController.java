package com.ssafy.webblog.controller.comment;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.webblog.controller.handler.ResultHandler;
import com.ssafy.webblog.model.dto.Comment;
import com.ssafy.webblog.model.service.CommentService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/comment")
public class RestCommentController {

	static Logger logger = LoggerFactory.getLogger(RestCommentController.class);
	
	@Autowired
	CommentService cService;
	@Autowired
	ResultHandler resultHandler;
	static final Class CLASSNAME = RestCommentController.class;
	
	
	//삽입 삭제 업데이트
	@PostMapping("/regist")
	@ApiOperation(value = "게시글에 댓글 등록")
	public ResponseEntity<Map<String, Object>> commentRegist(HttpServletRequest req, HttpServletResponse res, @RequestBody Comment comment)
			throws IOException {
		logger.debug("comment regist comment: " + comment.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Comment result = cService.insertComment(comment);
			entity = resultHandler.handleSuccess(result,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	

	@DeleteMapping("/delete/{commentId}")
	@ApiOperation(value = "댓글 삭제")
	public ResponseEntity<Map<String, Object>> commentDelete(HttpServletRequest req, HttpServletResponse res, @PathVariable String commentId)
			throws IOException {
		logger.debug("delete comment by commentId: " + commentId);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			cService.deleteComment(commentId);
			entity = resultHandler.handleSuccess("success",CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "댓글 수정")
	public ResponseEntity<Map<String, Object>> commentUpdate(HttpServletRequest req, HttpServletResponse res, @RequestBody Comment comment)
			throws  IOException {
		logger.debug("update comment before : " + cService.getCommentByCommentid(Integer.toString(comment.getCommentid())));
		logger.debug("update comment after : " + comment.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Comment result = cService.updateComment(comment);
			entity = resultHandler.handleSuccess(result,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}

	// 조회
	@GetMapping("/article/{articleId}")
	@ApiOperation(value = "게시글에 달린 댓글 목록 검색 ")
	public ResponseEntity<Map<String, Object>> getCommentListByArticleId(HttpServletRequest req, HttpServletResponse res, @PathVariable String articleId)
			throws IOException {
		logger.debug("Searching Comment by articleId: " + articleId);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Comment> result = cService.getCommentListByArticleid(articleId);
			entity = resultHandler.handleSuccess(result,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	
	@GetMapping("/{commentid}")
	@ApiOperation(value = "댓글 한개 조회")
	public ResponseEntity<Map<String, Object>> getComment(HttpServletRequest req, HttpServletResponse res, @PathVariable String commentid)
			throws JsonProcessingException, IOException {
		logger.debug("get comment info by commentid: " + commentid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Comment result = cService.getCommentByCommentid(commentid);
			entity = resultHandler.handleSuccess(result,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	
}