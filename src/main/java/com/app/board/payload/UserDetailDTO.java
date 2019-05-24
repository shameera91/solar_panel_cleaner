package com.app.board.payload;

import com.app.board.model.Board;

import java.util.Set;

/**
 * Created by Shameera on May, 2019
 */
public class UserDetailDTO {

	private Integer id;
	private String userName;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	private Integer []boardIds;
	private boolean isAdmin;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer[] getBoardIds() {
		return boardIds;
	}

	public void setBoardIds(Integer[] boardIds) {
		this.boardIds = boardIds;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
