package com.ssafy.webblog.controller.account.SNS;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.service.JwtDecoder;
import com.ssafy.webblog.model.service.SNSAccessTokenService;
import com.ssafy.webblog.model.service.UserAccountService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/account")
public class SNSTokenContoller {

	static Logger logger = LoggerFactory.getLogger(SNSTokenContoller.class);
	
	@Autowired
	SNSAccessTokenService SService;
	
	@Autowired
	UserAccountService uService;

	@PostMapping("/getSNSToken")
	@ApiOperation(value = "SNS로그인")
	public ResponseEntity<Map<String, Object>> getToken(HttpServletResponse res)
			throws JsonProcessingException, IOException {
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			Map<String, Object> resultMap = new HashMap<>();
			String token = SService.getToken();
			String email = SService.getEmail();	
			User user = uService.getUserByEmail(email);
			resultMap.put("email", user.getEmail());
			resultMap.put("nickname", user.getNickname());
			resultMap.put("loginkind", user.getLoginkind());
			res.setHeader("jwt-auth-token", token);
			entity = handleSuccess(resultMap);
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