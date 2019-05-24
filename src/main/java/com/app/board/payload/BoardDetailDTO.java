package com.app.board.payload;

import com.app.board.model.User;

import java.util.List;
import java.util.Set;

/**
 * Created by Shameera on May, 2019
 */
public class BoardDetailDTO {

	private Integer boardIdentity;
	private String simNumber;
	private String contactName;
	private String location;
	private String washTime;
	private Integer waterPerWash;
	private Set<User> users;
	private List<AutoWashDaysDTO> autoWashDays;

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

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Integer getWaterPerWash() {
		return waterPerWash;
	}

	public void setWaterPerWash(Integer waterPerWash) {
		this.waterPerWash = waterPerWash;
	}

	public List<AutoWashDaysDTO> getAutoWashDays() {
		return autoWashDays;
	}

	public void setAutoWashDays(List<AutoWashDaysDTO> autoWashDays) {
		this.autoWashDays = autoWashDays;
	}
}
