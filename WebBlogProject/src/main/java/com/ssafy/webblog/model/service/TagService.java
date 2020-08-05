package com.ssafy.webblog.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.dto.Tag;
import com.ssafy.webblog.model.repo.ArticleDao;
import com.ssafy.webblog.model.repo.TagDao;

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
public class TagService {

	@Autowired
	TagDao tDao;

	@Autowired
	ArticleDao aDao;

	// 태그 삽입
	public Tag insertTag(Tag tag) {
		Tag result = tDao.save(tag);
		return result;
	}

	// 태그 삭제
	public void deleteTag(int tagid) {
		Tag deleteTag = tDao.getTagByTagid(tagid);
		tDao.delete(deleteTag);
	}
	/*
	 * 수정이 굳이 필요한가? public Tag updateTag(Tag tag) { Tag result = tDao.save(tag);
	 * return result; }
	 */

	/**
	 * 태그 검색
	 * @param tagname
	 * @return 태그를 검색하면 태그 등록이 된 Article객체 리스트를 반환
	 */
	public Tag getTagByTagid(int tagid) {
		Tag findTag = tDao.getTagByTagid(tagid);
		return findTag;
	}
	
	
	public List<Article> getArticleListByTagname(String tagname) {
		List<Tag> searchingTagList = tDao.getTagByTagname(tagname);
		List<Article> result = new ArrayList<Article>();
		Article temp = null;

		for (Tag tag : searchingTagList) {
			temp = aDao.getArticleByArticleid(tag.getArticleid());
			if (temp != null && !result.contains(temp)) {
				result.add(temp);
			}
		}
		if(result.size() == 0) {
			return null;
		}
		return result;
	}
	
	public List<Tag> getTagListByArticleid(int articleid) {
		List<Tag> result = tDao.getTagByArticleid(articleid);
		return result;
	}
	
	public Tag getTagByArticleidAndTagname(int articleid, String tagname) {
		Tag result = tDao.getTagByArticleidAndTagname(articleid, tagname);
		return result;
	}
	
	public List<Tag> getTagByTagname(String tagname){
		List<Tag> result = tDao.getTagByTagname(tagname);
		return result;
	}

}
