package com.app.board.service;

import com.app.board.model.User;
import com.app.board.payload.UserDetailDTO;
import com.app.board.payload.UserRegistrationRequest;

import java.util.List;
import java.util.Optional;

/**
 * Created by Shameera on May, 2019
 */

public interface UserService {

	User registerOrUpdateUser(UserRegistrationRequest request);

	List<UserDetailDTO> getAllUsersExceptLoggedInUser(Integer id);

	UserDetailDTO getUserDetailsByEmail(String email);

	Optional<User> findUserById(Integer id);

	void deleteById(Integer id);

	User saveUser(User user);


}
