package com.ssafy.webblog.model.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.Follow;
import com.ssafy.webblog.model.repo.FollowDao;

@Service
@Transactional
public class FollowService {

	@Autowired
	FollowDao fDao;
	//내가 팔로우하는 사람 수
	public int getFollowCount(int userid){
		int result = fDao.getFollowByUserid(userid).size();
		return result;
	}
	// 나를 팔로우 하는 사람들 수
	public int getFollowerCount(int targetid){
		int result = fDao.getFollowByTargetid(targetid).size();
		return result;
	}
	//내가 팔로우하는 유저들의 유저id 리스트
	public List<Integer> getFollowList(int userid){
		List<Follow> Followers = fDao.getFollowByUserid(userid);
		List<Integer> result = new ArrayList<Integer>();
		for(Follow tmp : Followers) {
			result.add(tmp.getTargetid());
		}
		return result;
	}
}
// user/target
// a - > b
// a - > c
// b - > c
