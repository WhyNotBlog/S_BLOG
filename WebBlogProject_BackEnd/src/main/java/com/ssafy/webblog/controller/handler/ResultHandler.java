package com.ssafy.webblog.controller.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResultHandler {

	static Logger logger ;

	public ResponseEntity<Map<String, Object>> handleSuccess(Object data, Class classname) {
		logger = LoggerFactory.getLogger(classname.getClass());
		logger.info("Rest Controller name : " + classname.getName());
		logger.info("data : " + data.toString());
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", true);
		resultMap.put("data", data);
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}

	public ResponseEntity<Map<String, Object>> handleException(Exception e, Class classname) {
		logger = LoggerFactory.getLogger(classname.getClass());
		logger.error("예외 발생 : ", e);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", false);
		resultMap.put("data", e.getMessage());
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
