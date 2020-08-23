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

	// 내가 팔로우하는 사람 수
	public int getFollowCount(int userid) {
		int result = fDao.getFollowByUserid(userid).size();
		return result;
	}

	// 나를 팔로우 하는 사람들 수
	public int getFollowerCount(int userid) {
		int result = fDao.getFollowByTargetid(userid).size();
		return result;
	}

	// 내가 팔로우하는 유저들의 유저id 리스트
	public List<Integer> getFollowList(int userid) {
		List<Follow> Followers = fDao.getFollowByUserid(userid);
		List<Integer> result = new ArrayList<Integer>();
		for (Follow tmp : Followers) {
			result.add(tmp.getTargetid());
		}
		return result;
	}

	// 나를 팔로우하는 유저들의 유저id 리스트
	public List<Integer> getFollowingList(int userid) {
		List<Follow> Followings = fDao.getFollowByTargetid(userid);
		List<Integer> result = new ArrayList<Integer>();
		for (Follow tmp : Followings) {
			result.add(tmp.getUserid());
		}
		return result;
	}

	public Follow insertFollow(Follow follow) {
		Follow result = fDao.save(follow);
		return result;
	}

	public void deleteFollow(int userid, int targetid) {
		Follow deleteTargetFollow = fDao.getFollowByUseridAndTargetid(userid, targetid);
		fDao.delete(deleteTargetFollow);
	}
}
// user/target
// a - > b
// a - > c
// b - > c
