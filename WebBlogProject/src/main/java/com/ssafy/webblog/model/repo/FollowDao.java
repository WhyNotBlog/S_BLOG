package com.ssafy.webblog.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Follow;

public interface FollowDao extends JpaRepository<Follow, String> {
	List<Follow> getFollowByUserid(int userid);
	
}
