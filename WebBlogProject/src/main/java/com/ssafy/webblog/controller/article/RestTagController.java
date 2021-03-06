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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import com.ssafy.webblog.controller.comment.RestCommentController;
import com.ssafy.webblog.controller.handler.ResultHandler;
import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.dto.Tag;
import com.ssafy.webblog.model.dto.Tagkind;
import com.ssafy.webblog.model.service.TagService;
import com.ssafy.webblog.model.service.TagkindService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/tag")
public class RestTagController {

	static Logger logger = LoggerFactory.getLogger(RestTagController.class);

	@Autowired
	TagService tService;

	@Autowired
	TagkindService tkService;

	@Autowired
	ResultHandler resultHandler;
	static final Class CLASSNAME = RestTagController.class;

	@PostMapping("/regist")
	@ApiOperation(value = "태그 등록")
	public ResponseEntity<Map<String, Object>> tagRegist(HttpServletRequest req,HttpServletResponse res, @RequestBody Map<String, Object> map)
			throws IOException {
		String tags = (String) map.get("tags");
		int articleid = (int) map.get("articleid");
		logger.debug("Tag regist article id / tagsize" + articleid + " / " + tags);
		String[] input = tags.split(",");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			for (String tag : input) {
				logger.info("tag : " + tag);
				Tag tmp = new Tag();
				tmp.setArticleid(articleid);
				tmp.setTagname(tag);
				Tag temp = tService.insertTag(tmp);
				int tagcount = tService.getTagByTagname(temp.getTagname()).size();
				tkService.insertTagkind(new Tagkind(tag, tagcount));

			}
			String result = "success";
			entity = resultHandler.handleSuccess(result, CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e, CLASSNAME);
		}
		return entity;
	}

	@PutMapping("/update")
	@ApiOperation(value = "태그 상태 업데이트")
	public ResponseEntity<Map<String, Object>> tagUpdate(HttpServletRequest req, HttpServletResponse res, @RequestBody Map<String, Object> map)
			throws IOException {
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			String tags = (String) map.get("tags");
			int articleid = (int) map.get("articleid");
			String[] input = tags.split(",");
			// 입력한 태그가 있다면 그래도
			// 입력한 태그가 없다면 삽입.
			// 남은 태그는 삭제
			List<Tag> currentTagList = tService.getTagListByArticleid(articleid);
			List<Tag> inputTagList = new ArrayList<Tag>();

			for (String tagname : input) {
				// 기존의 [아티클 - 태그네임] 쌍은 유지
				if (isContain(currentTagList, tagname)) {
					Tag tempTag = new Tag();
					tempTag = tService.getTagByArticleidAndTagname(articleid, tagname);
					currentTagList.remove(tempTag);
				}
				// 새로운 태그는 삽입
				else {
					Tag tempTag = new Tag();
					tempTag.setArticleid(articleid);
					tempTag.setTagname(tagname);
					inputTagList.add(tempTag);
				}
			}
			// 중복된거제거하고 업데이트할 내용에 없는 태그 삭제
			for (Tag tag : currentTagList) {
				System.out.println("삭제 : " + tag.toString());
				tService.deleteTag(tag.getTagid());
				int tagcount = tService.getTagByTagname(tag.getTagname()).size();
				tkService.insertTagkind(new Tagkind(tag.getTagname(), tagcount));
			}
			System.out.println();
			for (Tag tag : inputTagList) {
				System.out.println("삽입 : " + tag.toString());
				tService.insertTag(tag);
				int tagcount = tService.getTagByTagname(tag.getTagname()).size();
				tkService.insertTagkind(new Tagkind(tag.getTagname(), tagcount));
			}
			entity = resultHandler.handleSuccess("success", CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e, CLASSNAME);
		}
		return entity;
	}

	private boolean isContain(List<Tag> currentTagList, String tagname) {
		for (Tag tmp : currentTagList) {
			if (tmp.getTagname().equals(tagname)) {
				return true;
			}
		}
		return false;
	}

	@GetMapping("/taglist/{articleid}")
	@ApiOperation(value = "아티클에 등록된 태그")
	public ResponseEntity<Map<String, Object>> getTagListByArticleid(HttpServletRequest req, HttpServletResponse res,
			@PathVariable String articleid) throws JsonProcessingException, IOException {
		logger.debug("Searching Tag List registing article id: " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Tag> result = tService.getTagListByArticleid(Integer.parseInt(articleid));
			logger.info("Tag list that is registed " + articleid + " : " + result.size());
			entity = resultHandler.handleSuccess(result, CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e, CLASSNAME);
		}
		return entity;
	}

	@GetMapping("/tentaglist")
	@ApiOperation(value = "전체 태그중 상위 5개")
	public ResponseEntity<Map<String, Object>> getTagList(HttpServletRequest req) throws JsonProcessingException, IOException {
		logger.debug("Searching Ten Tag List");
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Tagkind> result = tkService.getTagkind();
			entity = resultHandler.handleSuccess(result, CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e, CLASSNAME);
		}
		return entity;
	}

	@DeleteMapping("/delete")
	@ApiOperation(value = "아티클이 지워지면 delete")
	public ResponseEntity<Map<String, Object>> getTagList(HttpServletRequest req, @RequestBody String articleid)
			throws JsonProcessingException, IOException {
		logger.debug("delete article - > tag delete : " + articleid);
		ResponseEntity<Map<String, Object>> entity = null;
		try {
			List<Tag> deleteTargets = tService.getTagListByArticleid(Integer.parseInt(articleid));
			for (Tag tag : deleteTargets) {
				logger.info("Tag delete - > " + tag.toString());
				tService.deleteTag(tag.getTagid());
				Tagkind tagkind = tkService.getTagkindByTagname(tag.getTagname());
				if (tagkind.getTagcount() <= 0)
					tkService.delete(tag.getTagname());
				else
					tkService.insertTagkind(new Tagkind(tag.getTagname(), tagkind.getTagcount()));
			}
			entity = resultHandler.handleSuccess("success", CLASSNAME);
		} catch (RuntimeException e) {
			entity = resultHandler.handleException(e, CLASSNAME);
		}
		return entity;
	}

}