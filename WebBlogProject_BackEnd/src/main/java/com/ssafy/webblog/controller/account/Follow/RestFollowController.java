package com.ssafy.webblog.controller.account.Follow;

import java.io.IOException;
import java.util.ArrayList;
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
import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.service.FollowService;
import com.ssafy.webblog.model.service.UserAccountService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/follow")
public class RestFollowController {

	static Logger logger = LoggerFactory.getLogger(RestFollowController.class);

	@Autowired
	FollowService fService;

	@Autowired
	UserAccountService uSerivce;

	@PostMapping("/regist")
	@ApiOperation(value = "팔로우 관계 하나 등록")
	public ResponseEntity<Map<String, Object>> followRegist(HttpServletResponse res, @RequestBody Follow follow)
			throws IOException {
		logger.info("follow regist : " + follow.toString());
		ResponseEntity<Map<String, Object>> entity = null;
		Map<String, Object> targetUserInfo = new HashMap<String, Object>();
		try {
			Follow result = fService.insertFollow(follow);
			int targetid = result.getTargetid();
			User user = uSerivce.getUserById(targetid);
			targetUserInfo.put("data", result);
			targetUserInfo.put("id", user.getId());			
			targetUserInfo.put("nickname", user.getNickname());			
			entity = handleSuccess(targetUserInfo);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@DeleteMapping("/delete/{userid}/{targetid}")
	@ApiOperation(value = "팔로우 취소")
	public ResponseEntity<Map<String, Object>> followDelete(HttpServletResponse res, @PathVariable String userid, @PathVariable String targetid)
			throws IOException {
		logger.debug("delete follow [ " + userid + " - > " + targetid + " ]");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			fService.deleteFollow(Integer.parseInt(userid), Integer.parseInt(targetid));
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
			Map<String, Integer> result = new HashMap<String, Integer>();
			result.put("following", fService.getFollowCount(Integer.parseInt(userid)));
			result.put("follower", fService.getFollowerCount(Integer.parseInt(userid)));
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@GetMapping("/followingList/{userid}")
	@ApiOperation(value = "내가 팔로우하는 user의  id,nickname 리스트 반환")
	public ResponseEntity<Map<String, Object>> followingUserList(HttpServletResponse res, @PathVariable String userid)
			throws IOException {
		logger.debug("user following list : " + userid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Integer> followingUseridList = fService.getFollowList(Integer.parseInt(userid));
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Integer id : followingUseridList) {
				User user = uSerivce.getUserById(id);
				Map<String, Object> tempUserInfo = new HashMap<String, Object>();
				tempUserInfo.put("id", id);
				tempUserInfo.put("nickname", user.getNickname());
				tempUserInfo.put("picture", user.getPicture());
				result.add(tempUserInfo);
			}
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
			// logic ----
			List<Integer> followUseridList = fService.getFollowingList(Integer.parseInt(userid));
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Integer id : followUseridList) {
				User user = uSerivce.getUserById(id);
				Map<String, Object> tempUserInfo = new HashMap<String, Object>();
				tempUserInfo.put("id", id);
				tempUserInfo.put("nickname", user.getNickname());
				tempUserInfo.put("picture", user.getPicture());
				result.add(tempUserInfo);
			}
			// logic end ----
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