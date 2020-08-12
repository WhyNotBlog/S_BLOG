package com.ssafy.webblog.model.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Article;
import com.ssafy.webblog.model.repo.ArticleDao;

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

	public Page<Article> searchAll(int page) {
		Page<Article> result = null;
		result = artiDao.findAll(PageRequest.of(page, 6, Sort.Direction.DESC, "articleid"));
		return result;
	}
	
	public Page<Article> getArticleListByWriterid(int writerid, int page){
		Page<Article> result = artiDao.getArticleByWriterid(PageRequest.of(page, 6, Sort.Direction.DESC, "articleid"), writerid);
		return result;
	}
	
	public void addLikeToArticle(int articleid) {
		Article article = artiDao.getArticleByArticleid(articleid);
		article.setLikecount(article.getLikecount()+1);
	}
	public void dropLikeToArticle(int articleid) {
		Article article = artiDao.getArticleByArticleid(articleid);
		article.setLikecount(article.getLikecount()-1);
	}
	
	public void visitedArticle(int articleid) {
		Article article = artiDao.getArticleByArticleid(articleid);
		article.setHits(article.getHits()+1);
	}

}
