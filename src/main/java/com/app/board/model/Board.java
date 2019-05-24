package com.app.board.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Shameera on May, 2019
 */

@Entity
@Table(name = "board")
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer boardIdentity;
	private String simNumber;
	private String contactName;
	private String location;

	private String washTime;

	private Integer waterPerWash;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_boards",joinColumns = @JoinColumn(name = "board_id"),inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users;

	private String lastWash;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBoardIdentity() {
		return boardIdentity;
	}

	public void setBoardIdentity(Integer boardIdentity) {
		this.boardIdentity = boardIdentity;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Integer getWaterPerWash() {
		return waterPerWash;
	}

	public void setWaterPerWash(Integer waterPerWash) {
		this.waterPerWash = waterPerWash;
	}

	public String getWashTime() {
		return washTime;
	}

	public void setWashTime(String washTime) {
		this.washTime = washTime;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getLastWash() {
		return lastWash;
	}

	public void setLastWash(String lastWash) {
		this.lastWash = lastWash;
	}
}
