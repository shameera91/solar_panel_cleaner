package com.app.board.controller;

import com.app.board.exception.AppException;
import com.app.board.model.Role;
import com.app.board.model.RoleName;
import com.app.board.model.User;
import com.app.board.payload.ApiResponse;
import com.app.board.payload.JwtAuthenticationResponse;
import com.app.board.payload.LoginRequest;
import com.app.board.payload.SignUpRequest;
import com.app.board.repository.RoleRepository;
import com.app.board.repository.UserRepository;
import com.app.board.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

/**
 * Created by Shameera on May, 2019
 */

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;

	@PostMapping("/signin")
	public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		ApiResponse response = new ApiResponse();
		try{
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							loginRequest.getEmail(),
							loginRequest.getPassword()
					)
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = tokenProvider.generateToken(authentication);
			response.setData(new JwtAuthenticationResponse(jwt));
			response.setError(false);
			response.setMessage("Success");
			return new ResponseEntity<>(response,HttpStatus.OK);
		}catch (Exception e){
			response.setError(true);
			response.setMessage("Invalid credentials");
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		/*if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
					HttpStatus.BAD_REQUEST);
		}*/

		if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			ApiResponse response = new ApiResponse(true,"Email is already taken");
			/*response.setError(true);
			response.setMessage(HttpStatus.BAD_REQUEST.toString());*/
			return new ResponseEntity<>(response,HttpStatus.OK);
		}

		// Creating user's account
		User user = new User(signUpRequest.getEmail(), signUpRequest.getPassword());

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));

		user.setRoles(Collections.singleton(userRole));

		User result = userRepository.save(user);

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/api/users/{email}")
				.buildAndExpand(result.getEmail()).toUri();

		ApiResponse response = new ApiResponse();
		response.setError(false);
		response.setMessage("User registered successfully");

		return ResponseEntity.created(location).body(response);
	}
}
