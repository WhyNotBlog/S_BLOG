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
import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.service.ArticleService;
import com.ssafy.webblog.model.service.TagService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/article")
public class RestArticleController {

	static Logger logger = LoggerFactory.getLogger(RestArticleController.class);

	@Autowired
	ArticleService artiService;

	@Autowired
	TagService tService;
	
	@GetMapping("/{articleId}")
	@ApiOperation(value = "게시글 조회")
	public ResponseEntity<Map<String, Object>> getArticle(HttpServletResponse res, @PathVariable int articleId)
			throws JsonProcessingException, IOException {
		logger.debug("select by article id: " + articleId);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Article result = artiService.getArticleByArticleId(articleId);
			logger.debug(result.toString());
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	@PostMapping("/regist")
	@ApiOperation(value = "게시글 등록")
	public ResponseEntity<Map<String, Object>> articleRegist(HttpServletResponse res, @RequestBody Article article)
			throws IOException {
		logger.debug("Article regist : " + article.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Article result = artiService.insertArticle(article);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	

	@DeleteMapping("/delete/{articleid}")
	@ApiOperation(value = "게시글 삭제")
	public ResponseEntity<Map<String, Object>> articleDelete(HttpServletResponse res, @PathVariable String articleid)
			throws IOException {
		logger.debug("delete article by articleId: " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			artiService.deleteArticle(articleid);
			entity = handleSuccess("success");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@PutMapping("/update")
	@ApiOperation(value = "게시글 수정")
	public ResponseEntity<Map<String, Object>> articleUpdate(HttpServletResponse res, @RequestBody Article article)
			throws  IOException {
		logger.debug("update article before : " + artiService.getArticleByArticleId(article.getArticleid()));
		logger.debug("update article after : " + article.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Article result = artiService.updateArticle(article);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}


	@GetMapping("/searchBy/nickname/{nickname}")
	@ApiOperation(value = "게시글 목록 검색 - 닉네임으로 검색")
	public ResponseEntity<Map<String, Object>> getArticleListByNickname(HttpServletResponse res, @PathVariable String nickname)
			throws IOException {
		logger.debug("Searching article by nickname : " + nickname);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Article> result = artiService.searchBy(nickname, 1);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	@GetMapping("/searchBy/category/{category}")
	@ApiOperation(value = "게시글 목록 검색 - 카테고리별로 검색")
	public ResponseEntity<Map<String, Object>> getArticleListByCategory(HttpServletResponse res, @PathVariable String category)
			throws IOException {
		logger.debug("Searching article by nickname : " + category);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Article> result = artiService.searchBy(category, 2);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	@GetMapping("/searchBy/title/{title}")
	@ApiOperation(value = "게시글 목록 검색 - 타이틀 검색")
	public ResponseEntity<Map<String, Object>> getArticleListByTitle(HttpServletResponse res, @PathVariable String title)
			throws IOException {
		logger.debug("Searching article by title: " + title);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Article> result = artiService.searchBy(title, 0);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@GetMapping("/searchby/tag/{tagname}")
	@ApiOperation(value = "태그로 아티클검색")
	public ResponseEntity<Map<String, Object>> getArticleListByTagname(HttpServletResponse res, @PathVariable String tagname)
			throws JsonProcessingException, IOException {
		logger.debug("Searching article by tagname : " + tagname);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Article> result = tService.getArticleListByTagname(tagname);
			logger.info("article size : " + result.size());
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}


	
	@GetMapping("/searchBy/allarticle")
	@ApiOperation(value = "전체 게시글 조회")
	public ResponseEntity<Map<String, Object>> getAllArticleList(HttpServletResponse res)
			throws IOException {
		logger.debug("Searching all article ");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Article> result = artiService.searchAll();
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
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