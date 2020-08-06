package com.ssafy.webblog.controller.article;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.dto.Tagkind;
import com.ssafy.webblog.model.dto.Tagtemp;
import com.ssafy.webblog.model.service.TagkindService;
import com.ssafy.webblog.model.service.TagtempService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/tagtemp")
public class RestTagtempController {

	static Logger logger = LoggerFactory.getLogger(RestTagtempController.class);

	@Autowired
	TagtempService ttService;


	@PostMapping("/regist")
	@ApiOperation(value = "임시 태그 등록")
	public ResponseEntity<Map<String, Object>> tagRegist(HttpServletResponse res, @RequestBody Map<String, Object> map)
			throws IOException {
		String tagtemps = (String) map.get("tagtemps");
		int articletempid = (int) map.get("articletempid");
		logger.debug("Tagtemp regist articletemp id / tagtemp" + articletempid + " / " + tagtemps);
		String[] input = tagtemps.split(",");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			for (String tag : input) {
				logger.info("tag : " + tag);
				Tagtemp tmp = new Tagtemp();
				tmp.setArticleid(articletempid);
				tmp.setTagname(tag);
				ttService.insertTagtemp(tmp);
			}
			String result = "success";
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@PutMapping("/update")
	@ApiOperation(value = "임시 태그 상태 업데이트")
	public ResponseEntity<Map<String, Object>> tagUpdate(HttpServletResponse res, @RequestBody Map<String, Object> map)
			throws IOException {
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			String tagtemps = (String) map.get("tagtemps");
			int articletempid = (int) map.get("articletempid");
			String[] input = tagtemps.split(",");
			
			List<Tagtemp> currentTagtempList = ttService.getTagtempListByArticleid(articletempid);
			//기존꺼 전부 삭제
			for(Tagtemp tagtemp : currentTagtempList ) {
				ttService.deleteTagtemp(tagtemp.getTagid());
			}
			//새로 지정한 태그로 전부 삽입
			for (String tag : input) {
				logger.info("tag : " + tag);
				Tagtemp tmp = new Tagtemp();
				tmp.setArticleid(articletempid);
				tmp.setTagname(tag);
				ttService.insertTagtemp(tmp);
			}
			entity = handleSuccess("success");
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}

	@GetMapping("/taglist/{articletempid}")
	@ApiOperation(value = "임시 아티클에 등록된 임시태그")
	public ResponseEntity<Map<String, Object>> getTagListByArticleid(HttpServletResponse res,
			@PathVariable String articletempid) throws JsonProcessingException, IOException {
		logger.debug("Searching Tag List registing article id: " + articletempid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Tagtemp> result = ttService.getTagtempListByArticleid(Integer.parseInt(articletempid));
			logger.info("Tagtemp list that is registed " + articletempid + " : " + result.size());
			entity = handleSuccess(result);
		} catch (RuntimeException e) {
			entity = handleException(e);
		}
		return entity;
	}


	// ------------------------------------------

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