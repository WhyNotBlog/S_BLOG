package com.ssafy.webblog.model.service;

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

	public List<Follow> getFollowerByUserid(int userid){
		List<Follow> result = fDao.getFollowByUserid(userid);
		return result;
	}
	
	public int getFollowerCount(int userid) {
		List<Follow> result = fDao.getFollowByUserid(userid);
		return result.size();
	}
}
