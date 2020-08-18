package com.ssafy.webblog.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.dto.Likearticle;
import com.ssafy.webblog.model.dto.Tag;
import com.ssafy.webblog.model.repo.ArticleDao;
import com.ssafy.webblog.model.repo.LikearticleDao;

@Service
@Transactional
public class ArticleService {

	@Autowired
	ArticleDao artiDao;
//	User getUserByEmail(String email);
//	User getUserById(String uid);
//	Optional<User> findUserByEmailAndPassword(String email, String password);

	public Article getArticleByArticleId(int articleid) {
		Optional<Article> getArti = artiDao.findArticleByArticleid(articleid);
		if (getArti == null) {
			return null;
		}
		return getArti.get();
	}

	// 삽입
	public Article insertArticle(Article article) {
		Article result = artiDao.save(article);
		return result;
	}
	
	// 삭제
	public void deleteArticle(int articleid) {
		artiDao.deleteArticleByArticleid(articleid);
	}

	// 업데이트
	public Article updateArticle(Article article) {
		article.setModify(1);
		Article result = artiDao.save(article);
		return result;
	}

	// 목록 조회
	public Page<Article> searchBy(String input, int By, int page) {
		Page<Article> result = null;
		/*
		 * 0 : default search => title 1 : nickname검색 2 : 카테고리별 검색
		 */
		if (By == 1) {
			result = artiDao.getArticleByEditornicknameContaining(PageRequest.of(page, 6, Sort.Direction.DESC, "articleid"), input);
		} else if (By == 2) {
			result = artiDao.getArticleByCategory(PageRequest.of(page, 6, Sort.Direction.DESC, "articleid"), Integer.parseInt(input));
		} else {
			result = artiDao.getArticleByTitleContaining(PageRequest.of(page, 6, Sort.Direction.DESC, "articleid"), input);
		}
		return result;
	}

	public Page<Article> searchAll(int sort, int page) {
		Page<Article> result = null;
		if(sort == 0) result = artiDao.findAll(PageRequest.of(page, 6, Sort.Direction.DESC, "articleid"));
		else if(sort == 1) result = artiDao.findAll(PageRequest.of(page, 6, Sort.Direction.DESC, "hits"));
		else if(sort == 2) result = artiDao.findAll(PageRequest.of(page, 6, Sort.Direction.DESC, "likecount"));
		return result;
	}
	
	public Page<Article> getArticleListByWriterid(int writerid, int page){
		Page<Article> result = artiDao.getArticleByWriterid(PageRequest.of(page, 6, Sort.Direction.DESC, "articleid"), writerid);
		return result;
	}
	
	@Autowired
	LikearticleDao lDao;
	
	public List<Article> getLikedArticleListByUserId(int userid, int page) {
		Page<Likearticle> searchingLikedList = lDao.getLikearticleByUserid(PageRequest.of(page, 6, Sort.Direction.DESC, "likekey"), userid);
		List<Article> result = new ArrayList<Article>();
		Article temp = null;
		for (Likearticle likearticle : searchingLikedList) {
			temp = artiDao.getArticleByArticleid(likearticle.getArticleid());
			result.add(temp);
		}
		if(result.size() == 0) {
			return null;
		}
		return result;
	}
	
	public int addLikeToArticle(int articleid) {
		Article article = artiDao.getArticleByArticleid(articleid);
		article.setLikecount(article.getLikecount()+1);
		return article.getLikecount();
	}
	public int dropLikeToArticle(int articleid) {
		Article article = artiDao.getArticleByArticleid(articleid);
		article.setLikecount(article.getLikecount()-1);
		return article.getLikecount();
	}
	
	public int visitedArticle(int articleid) {
		Article article = artiDao.getArticleByArticleid(articleid);
		article.setHits(article.getHits()+1);
		return article.getHits();
	}

}
