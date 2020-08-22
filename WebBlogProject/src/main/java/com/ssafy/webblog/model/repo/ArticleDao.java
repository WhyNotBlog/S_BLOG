package com.ssafy.webblog.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Article;

public interface ArticleDao extends JpaRepository<Article, String> {
	//검색을 위한 select
	Page<Article> getArticleByEditornicknameContaining(Pageable pageable, String editornickname);
	
	//List<Article> getArticleByTitle(String title);
	
	Page<Article> getArticleByTitleContaining(Pageable pageable, String title);
	
	Page<Article> getArticleByCategory(Pageable pageable, int editornickname);

	Page<Article> getArticleByWriterid(Pageable pageable, int writerid);
	
	//article id로 조회
	
	Article getArticleByArticleid(int articleid);
	//article 전체 객체 반환
	Optional<Article> findArticleByArticleid(int articleid);
	
	Page<Article> findAll(Pageable pageable);

	void deleteArticleByArticleid(int articleid);
	

}
