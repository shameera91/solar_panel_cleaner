package com.app.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Shameera on May, 2019
 **/

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String userName;
	private String email;
	private String phone;
	private String firstName;
	private String lastName;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private Date lastLogin;
	private boolean isAdmin;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "users")
	private Set<Board> boards = new HashSet<>();

	public User() {

	}

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String userName, String email, String phone, String firstName, String lastName, String password,boolean isAdmin) {
        this.userName = userName.toLowerCase();
        this.email = email.toLowerCase();
		this.phone = phone;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.isAdmin = isAdmin;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
        return email.toLowerCase();
	}

	public void setEmail(String email) {
        this.email = email.toLowerCase();
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUserName() {
        return userName.toLowerCase();
	}

	public void setUserName(String userName) {
        this.userName = userName.toLowerCase();
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Board> getBoards() {
		return boards;
	}

	public void setBoards(Set<Board> boards) {
		this.boards = boards;
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

	@Override
	public boolean equals(Object obj){
		if (this == obj){
			return true;
		}
		User person = (User) obj;
        if (email != null ? !email.equalsIgnoreCase(person.email) : person.email != null) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public String toString() {

		return "Person [id="
				+ id
				+ ", email="
				+ email
				+ "]";
	}
}
