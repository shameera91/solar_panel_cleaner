package com.app.board.service.Impl;

import com.app.board.model.Board;
import com.app.board.model.User;
import com.app.board.payload.UserDetailDTO;
import com.app.board.payload.UserRegistrationRequest;
import com.app.board.repository.UserRepository;
import com.app.board.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Shameera on May, 2019
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Override
	public User registerOrUpdateUser(UserRegistrationRequest request) {
        User user = new User(request.getUserName(), request.getEmail().toLowerCase(), request.getPhone(), request.getFirstName(),
				request.getLastName(),encoder.encode(request.getPassword()),request.getIsAdmin());
		return userRepository.save(user);
	}

	@Override
	public List<UserDetailDTO> getAllUsersExceptLoggedInUser(Integer id) {
		User thisUser = userRepository.findOne(id);
		List<UserDetailDTO> arrangedList = new ArrayList<>();
		if (!thisUser.isAdmin()) {
			UserDetailDTO userDtl = new UserDetailDTO();
			userDtl.setId(thisUser.getId());
			userDtl.setUserName(thisUser.getUserName());
			userDtl.setEmail(thisUser.getEmail().toLowerCase());
			userDtl.setFirstName(thisUser.getFirstName());
			userDtl.setLastName(thisUser.getLastName());
			arrangedList.add(userDtl);
			return arrangedList;
		}
		List<User> allUsers = userRepository.findAll();
		for (User user:allUsers){
			if(!(user.getId() == id)){
				UserDetailDTO userDtl = new UserDetailDTO();
				userDtl.setId(user.getId());
				userDtl.setUserName(user.getUserName());
                userDtl.setEmail(user.getEmail().toLowerCase());
				userDtl.setFirstName(user.getFirstName());
				userDtl.setLastName(user.getLastName());

				Set<Board> boards = user.getBoards();
				Integer []boardIds= new Integer[boards.size()]; int i=0;
				if(!boards.isEmpty()){
					for (Board b : boards){
						boardIds[i] = b.getBoardIdentity();
						i++;
					}
				}

				userDtl.setBoardIds(boardIds);
				//userDtl.setBoards(user.getBoards());
				userDtl.setAdmin(user.isAdmin());
				userDtl.setPhone(user.getPhone());

				arrangedList.add(userDtl);
			}
		}
		return arrangedList;
	}

	@Override
	public UserDetailDTO getUserDetailsByEmail(String email) {
		UserDetailDTO userDtl = new UserDetailDTO();
        Optional<User> userObj = userRepository.findByEmail(email.toLowerCase());
		if(userObj.isPresent()){
			User user = userObj.get();
			userDtl.setId(user.getId());
			userDtl.setUserName(user.getUserName());
            userDtl.setEmail(user.getEmail().toLowerCase());
			userDtl.setFirstName(user.getFirstName());
			userDtl.setLastName(user.getLastName());

			Set<Board> boards = user.getBoards();
			Integer []boardIds=new Integer[boards.size()]; int i=0;
			if(!boards.isEmpty()){
				for (Board b : boards){
					boardIds[i] = b.getBoardIdentity();
					i++;
				}
			}

			userDtl.setBoardIds(boardIds);
			userDtl.setAdmin(user.isAdmin());
			userDtl.setPhone(user.getPhone());

			/*updating the logged in date and time*/
			user.setLastLogin(new Date());
			userRepository.save(user);
		}

		return userDtl;
	}

	@Override
	public Optional<User> findUserById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public void deleteById(Integer id) {
		 userRepository.delete(id);
	}

	@Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}
}
