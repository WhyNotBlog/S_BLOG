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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.webblog.controller.handler.ResultHandler;
import com.ssafy.webblog.model.dto.Likearticle;
import com.ssafy.webblog.model.service.LikeService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/like")
public class RestLikeController {

	static Logger logger = LoggerFactory.getLogger(RestLikeController.class);

	@Autowired
	LikeService lService;

	@Autowired
	ResultHandler resultHandler;
	static final Class CLASSNAME = RestLikeController.class;
	
	
	@PostMapping("/articlelist")
	@ApiOperation(value = "아티클 번호에 대한 like수 반환")
	public ResponseEntity<Map<String, Object>> tagRegist(HttpServletResponse res, @RequestBody Map<String, Object> map)
			throws IOException {
		String articleids = (String) map.get("articleid");	
		logger.debug("Request Like count by ariticle id " + articleids);
		String[] input = articleids.split(",");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Map<String, Integer> likeCountMap = new HashMap<String, Integer>();
			for(String id : input) {
				logger.info("article id : " + id);
				int count = lService.getArticleLikeCountByArticleid(Integer.parseInt(id));
				likeCountMap.put(id, count);
			}
			entity = resultHandler.handleSuccess(likeCountMap,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	
	@PostMapping("/regist")
	@ApiOperation(value = "좋아요 하나 등록")
	public ResponseEntity<Map<String, Object>> likeRegist(HttpServletResponse res, @RequestBody Likearticle like)
			throws IOException {
		logger.info("Like regist : " + like.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Likearticle result = lService.registLike(like);
			entity = resultHandler.handleSuccess(result,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	
	@DeleteMapping("/delete/{userid}/{articleid}")
	@ApiOperation(value = "좋아요 취소")
	public ResponseEntity<Map<String, Object>> likeDelete(HttpServletResponse res, @PathVariable String userid,@PathVariable String articleid)
			throws IOException {
		logger.debug("delete userid - articleid: " + userid + "/" + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			int likekey = lService.getLikekeyByUseridAndArticleid(Integer.parseInt(userid), Integer.parseInt(articleid));
			Likearticle deleteLike = new Likearticle(likekey, Integer.parseInt(userid), Integer.parseInt(articleid));
			lService.deleteLike(deleteLike);
			entity = resultHandler.handleSuccess("success",CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	
	@GetMapping("/article/{articleid}")
	@ApiOperation(value = "게시글한개의 좋아요 수")
	public ResponseEntity<Map<String, Object>> likeDelete(HttpServletResponse res, @PathVariable String articleid)
			throws IOException {
		logger.debug("article like count : " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			int result = lService.getArticleLikeCountByArticleid(Integer.parseInt(articleid));
			entity = resultHandler.handleSuccess(result,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}
	
	@GetMapping("/userlike/{userid}")
	@ApiOperation(value = "유저가 좋아요 누른 아티클 번호리스트")
	public ResponseEntity<Map<String, Object>> articleListByUserLike(@PathVariable String userid)
			throws IOException {
		logger.debug("user likes article : " + userid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Integer> result = lService.getAllArticleByUserid(Integer.parseInt(userid));
			entity = resultHandler.handleSuccess(result,CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e,CLASSNAME);
		}
		return entity;
	}


}