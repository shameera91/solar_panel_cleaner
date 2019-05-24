package com.app.board.controller;

import com.app.board.model.User;
import com.app.board.payload.ApiResponse;
import com.app.board.payload.ChangePasswordRequest;
import com.app.board.payload.UserDetailDTO;
import com.app.board.payload.UserRegistrationRequest;
import com.app.board.repository.UserRepository;
import com.app.board.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by Shameera on May, 2019
 */

@RestController
@RequestMapping("/api")
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UserService userService;



	@PutMapping("/changePassword/{id}")
	public ResponseEntity<?> changeUserPassword(@PathVariable Integer id,@RequestBody ChangePasswordRequest request){
		ApiResponse response = new ApiResponse();
		if(request.getOldPassword().equals(request.getNewPassword())){
			response.setError(true);
			response.setMessage("New password cant be equal to old password");
		}else{
			Optional<User> userById = userService.findUserById(id);
			if(userById.isPresent()){
				User user = userById.get();
				if(passwordEncoder.matches(request.getOldPassword(),user.getPassword())){
					user.setPassword(passwordEncoder.encode(request.getNewPassword()));
					userService.saveUser(user);

					response.setError(false);
					response.setMessage("Success");
				}else{
					response.setError(true);
					response.setMessage("Old password is incorrect");
				}

			}else{
				response.setError(true);
				response.setMessage("No user found");
			}
		}
		return ResponseEntity.ok(response);
		}

	@PutMapping("/editUser/{id}")
	public ResponseEntity<?> editUser(@PathVariable Integer id,@Valid @RequestBody UserRegistrationRequest request){
		ApiResponse response= new ApiResponse();
		Optional<User> userById = userService.findUserById(id);
		if(userById.isPresent()){
			User user = userById.get();

			user.setAdmin(request.getIsAdmin());
			if(request.getPassword() != null){
				user.setPassword(passwordEncoder.encode(request.getPassword()));
			}
			user.setEmail(request.getEmail());
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setPhone(request.getPhone());
			user.setUserName(request.getUserName());

			userService.saveUser(user);

			response.setMessage("Success");
			response.setError(false);
			//response.setData(userService.saveUser(user));
		}else{
			response.setMessage("No user found");
			response.setError(true);
			response.setData(null);

		}
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/deleteUser/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id){
		ApiResponse response= new ApiResponse();
		if(userService.findUserById(id).isPresent()){
			userService.deleteById(id);
			response.setError(false);
			response.setMessage("Success");
		}else{
			response.setError(true);
			response.setMessage("No user available");
		}
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getAllUsers/{id}")
	public  ResponseEntity<?> getUsers(@PathVariable Integer id){
		return new ResponseEntity<>(new ApiResponse(userService.getAllUsersExceptLoggedInUser(id),false,"Success"),HttpStatus.OK);
	}

	@PostMapping("/saveUser")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request){
		if(userRepository.existsByEmail(request.getEmail())) {
			ApiResponse response = new ApiResponse(true,"Email is already taken");
			return new ResponseEntity<>(response,HttpStatus.OK);
		}else{
			userService.registerOrUpdateUser(request);
			return new ResponseEntity<>(new ApiResponse(false,"Success"),HttpStatus.OK);
		}
	}

	@GetMapping("/userDetails")
	public ResponseEntity<?> getUserDetailsByEmail(@RequestParam String email){   /* sign in api */
		ApiResponse response = new ApiResponse();
		UserDetailDTO userDetailsByEmail = userService.getUserDetailsByEmail(email);
		if(userDetailsByEmail != null){
			response.setMessage("Success");
			response.setError(false);
			response.setData(userDetailsByEmail);
		}else{
			response.setData(null);
			response.setMessage("No user found");
			response.setError(true);
		}
		return new ResponseEntity<>(response,HttpStatus.OK);
	}

}
