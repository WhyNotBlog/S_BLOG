package com.ssafy.webblog.controller.article;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.ssafy.webblog.model.dto.Articletemp;
import com.ssafy.webblog.model.service.ArticleService;
import com.ssafy.webblog.model.service.ArticletempService;
import com.ssafy.webblog.model.service.TagService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/articletemp")
public class RestArticletempController {

	static Logger logger = LoggerFactory.getLogger(RestArticletempController.class);

	@Autowired
	ArticletempService artiTempService;

	@Autowired
	TagService tService;
	
	@GetMapping("/{articleId}")
	@ApiOperation(value = "임시 게시글 조회")
	public ResponseEntity<Map<String, Object>> getArticle(HttpServletResponse res, @PathVariable int articleid)
			throws JsonProcessingException, IOException {
		logger.debug("Articletemp select by article id: " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Articletemp result = artiTempService.getArticletempByArticletempid(articleid);
			logger.debug(result.toString());
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@PostMapping("/regist")
	@ApiOperation(value = "임시 게시글 등록")
	public ResponseEntity<Map<String, Object>> articleRegist(HttpServletResponse res, @RequestBody Articletemp articletemp)
			throws IOException {
		logger.debug("Articletemp regist : " + articletemp.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Articletemp result = artiTempService.insertArticletemp(articletemp);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	

	@DeleteMapping("/delete/{articleid}")
	@ApiOperation(value = "임시 게시글 삭제")
	public ResponseEntity<Map<String, Object>> articleDelete(HttpServletResponse res, @PathVariable String articleid)
			throws IOException {
		logger.debug("delete articletemp by articleid: " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			artiTempService.deleteArticletemp(articleid);
			entity = handleSuccess("success");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
//	
//	@PutMapping("/update")
//	@ApiOperation(value = "게시글 수정")
//	public ResponseEntity<Map<String, Object>> articleUpdate(HttpServletResponse res, @RequestBody Articletemp articletemp)
//			throws  IOException {
//		logger.debug("update article temp before : " + artiTempService.getArticletempByArticletempid(articletemp.getArticleid())
//		logger.debug("update article temp after : " + articletemp.toString());
//		ResponseEntity<Map<String, Object>> entity = null;
//		try {
//			Articletemp result = artiTempService.updateArticle(articletemp);
//			entity = handleSuccess(result);
//		} catch (RuntimeException e) {
//			entity = handleException(e);
//		}
//		return entity;
//	}


	
	
	private ResponseEntity<Map<String, Object>> handleSuccess(Object data) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", true);
		resultMap.put("data", data);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	private ResponseEntity<Map<String, Object>> handleException(Exception e) {
		logger.error("예외 발생 : ", e);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", false);
		resultMap.put("data", e.getMessage());
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}