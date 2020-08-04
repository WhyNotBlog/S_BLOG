package com.ssafy.webblog.model.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Article;

public interface ArticleDao extends JpaRepository<Article, String> {
	//검색을 위한 select
	List<Article> getArticleByEditornicknameContaining(String editornickname);
	
	//List<Article> getArticleByTitle(String title);
	
	List<Article> getArticleByTitleContaining(String title);
	
	List<Article> getArticleByCategory(String editornickname);

	List<Article> getArticleByWriterid(int writerid);
	
	//article id로 조회
	
	Article getArticleByArticleid(int articleid);
	//article 전체 객체 반환
	Optional<Article> findArticleByArticleid(int articleid);
	
	

}
