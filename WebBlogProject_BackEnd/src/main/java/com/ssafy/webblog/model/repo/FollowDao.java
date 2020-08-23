package com.ssafy.webblog.model.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.Follow;

public interface FollowDao extends JpaRepository<Follow, String> {
	//나를 팔로우하는 리스트
	List<Follow> getFollowByTargetid(int targetid);
	
	//내가 팔로우하는 리스트
	List<Follow> getFollowByUserid(int userid);
	
	Follow getFollowByUseridAndTargetid(int userid, int tragetid);
	//추가 삭제는 jparepository 지원
}
