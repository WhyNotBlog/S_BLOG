package com.ssafy.webblog.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Like;

public interface LikeDao extends JpaRepository<Like, String> {
	//한 게시글의 좋아요 수
	
	List<Like> getLikeByArticleid(int articleid);
	
	
	List<Like> getLikeByUserid(int userid);
}
