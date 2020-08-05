package com.ssafy.webblog.controller.account.Follow;

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

import com.ssafy.webblog.model.dto.Follow;
import com.ssafy.webblog.model.service.FollowService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/follow")
public class RestFollowController {

	static Logger logger = LoggerFactory.getLogger(RestFollowController.class);

	@Autowired
	FollowService fService;

	@PostMapping("/regist")
	@ApiOperation(value = "팔로우 관계 하나 등록")
	public ResponseEntity<Map<String, Object>> followRegist(HttpServletResponse res, @RequestBody Follow follow)
			throws IOException {
		logger.info("follow regist : " + follow.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Follow result = fService.insertFollow(follow);
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@DeleteMapping("/delete")
	@ApiOperation(value = "팔로우 취소")
	public ResponseEntity<Map<String, Object>> followDelete(HttpServletResponse res, @RequestBody Follow follow)
			throws IOException {
		int userid = follow.getUserid();
		int targetid = follow.getTargetid();
		logger.debug("delete follow [ " + userid + " - > " + targetid + " ]");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			fService.deleteFollow(userid, targetid);
			entity = handleSuccess("success");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@GetMapping("/count/{userid}")
	@ApiOperation(value = "유저id로 팔로워수, 팔로잉 수")
	public ResponseEntity<Map<String, Object>> followCount(HttpServletResponse res, @PathVariable String userid)
			throws IOException {
		logger.debug("user follpw count : " + userid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Map<String,Integer> result = new HashMap<String, Integer>();
			result.put("following", fService.getFollowCount(Integer.parseInt(userid)));
			result.put("follower", fService.getFollowerCount(Integer.parseInt(userid)));
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@GetMapping("/followingList/{userid}")
	@ApiOperation(value = "내가 팔로우하는 user id 리스트")
	public ResponseEntity<Map<String, Object>> followingUserList(HttpServletResponse res, @PathVariable String userid)
			throws IOException {
		logger.debug("user following list : " + userid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Integer> result = fService.getFollowList(Integer.parseInt(userid));
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	
	@GetMapping("/followList/{userid}")
	@ApiOperation(value = "나를 팔로우하는 user id 리스트")
	public ResponseEntity<Map<String, Object>> followerUserList(HttpServletResponse res, @PathVariable String userid)
			throws IOException {
		logger.debug("user follower list : " + userid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Integer> result = fService.getFollowingList(Integer.parseInt(userid));
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}
	

	// ----


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