package com.ssafy.webblog.model.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.webblog.model.dto.User;

public interface UserDao extends JpaRepository<User, String> {
	User getUserByEmail(String email);

	User getUserByNickname(String nickname);
	
	User getUserById(int id);

	Optional<User> findUserByEmailAndPassword(String email, String password);

}
