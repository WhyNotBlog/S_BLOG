package com.ssafy.webblog.model.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.webblog.model.dto.User;
import com.ssafy.webblog.model.repo.UserDao;

@Service
@Transactional
public class UserAccountService {

	@Autowired
	UserDao userDao;

//	User getUserByEmail(String email);
//	User getUserById(String uid);
//	Optional<User> findUserByEmailAndPassword(String email, String password);

	public User LoginUserByEmailAndPassword(String email, String password) {
		Optional<User> loginUser = userDao.findUserByEmailAndPassword(email, password);
		if (loginUser == null) {
			return null;
		}
		return loginUser.get();
	}

	public User insertUser(User user) {
		User result = userDao.save(user);
		return result;
	}

	public void deleteUser(String nickname) {
		User deleteUser = userDao.getUserByNickname(nickname);
		userDao.delete(deleteUser);
	}

	public User updateUser(User user) {
		User result = userDao.save(user);
		return result;
	}

	public User getUserByEmail(String email) {
		User getUser = userDao.getUserByEmail(email);
		if (getUser == null) {
			return null;
		}
		return getUser;
	}

	public User getUserByNickname(String nickname) {
		User getUser = userDao.getUserByNickname(nickname);
		if (getUser == null) {
			return null;
		}
		return getUser;
	}
	

	public User getUserById(int id) {
		User getUser = userDao.getUserById(id);
		if (getUser == null) {
			return null;
		}
		return getUser;
	}

}
