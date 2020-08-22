package com.ssafy.webblog.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.dto.Tagtemp;
import com.ssafy.webblog.model.repo.ArticleDao;
import com.ssafy.webblog.model.repo.TagtempDao;

/*
 * 태그 저장, 삭제
 * 사용자가 게시글에 태그를 달때 저장, 
 * 사용자가 태그 저장에서 삭제 누르면 태그 목록에서 제거
 * 
 * 태그가 사용되는 경우
 * 1. 태그별로 검색을 할 때 태그가 붙은 article목록을 반환 할 때, 즉 태그를 검색한다.
 * */
@Service
@Transactional
public class TagtempService {

	@Autowired
	TagtempDao ttDao;

	@Autowired
	ArticleDao aDao;

	// 태그 삽입
	public Tagtemp insertTagtemp(Tagtemp Tagtemp) {
		Tagtemp result = ttDao.save(Tagtemp);
		return result;
	}

	// 태그 삭제
	public void deleteTagtemp(int tagid) {
		Tagtemp deleteTagtemp = ttDao.getTagtempByTagid(tagid);
		ttDao.delete(deleteTagtemp);
	}

	public Tagtemp getTagtempByTagid(int tagid) {
		Tagtemp findTagtemp = ttDao.getTagtempByTagid(tagid);
		return findTagtemp;
	}
	
	
	public List<Tagtemp> getTagtempListByArticleid(int articleid) {
		List<Tagtemp> result = ttDao.getTagtempByArticleid(articleid);
		return result;
	}
	
	public Tagtemp getTagtempByArticleidAndTagname(int articleid, String tagname) {
		Tagtemp result = ttDao.getTagtempByArticleidAndTagname(articleid, tagname);
		return result;
	}
	
	public List<Tagtemp> getTagtempByTagname(String tagname){
		List<Tagtemp> result = ttDao.getTagtempByTagname(tagname);
		return result;
	}

}
