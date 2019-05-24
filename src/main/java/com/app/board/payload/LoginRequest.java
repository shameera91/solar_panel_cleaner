package com.app.board.payload;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Shameera on May, 2019
 */
public class LoginRequest {

	@NotBlank
	private String email;

	@NotBlank
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
