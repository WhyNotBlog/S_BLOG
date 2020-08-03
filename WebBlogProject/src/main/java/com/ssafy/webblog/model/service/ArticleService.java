package com.ssafy.webblog.model.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void deleteArticle(String articleid) {
		Article deleteArticle = artiDao.getArticleByArticleid(Integer.parseInt(articleid));
		artiDao.delete(deleteArticle);
	}

	// 업데이트
	public Article updateArticle(Article article) {
		article.setModify(1);
		Article result = artiDao.save(article);
		return result;
	}

	// 목록 조회
	public List<Article> searchBy(String input, int By) {
		List<Article> result = null;
		/*
		 * 0 : default search => title 1 : nickname검색 2 : 카테고리별 검색
		 */
		if (By == 1) {
			result = artiDao.getArticleByEditornickname(input);
		} else if (By == 2) {
			result = artiDao.getArticleByCategory(input);
		} else {
			result = artiDao.getArticleByTitle(input);
		}
		return result;
	}

	public List<Article> searchAll() {
		List<Article> result = null;
		result = artiDao.getArticleBy();
		return result;
	}

}
