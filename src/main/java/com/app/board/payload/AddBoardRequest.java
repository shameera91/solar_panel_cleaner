package com.app.board.payload;

import java.util.List;

/**
 * Created by Shameera on May, 2019
 */
public class AddBoardRequest {

	private Integer boardIdentity;
	private String simNumber;
	private String contactName;
	private String location;
	private Integer users[];
	private String washTime;
	private List<AutoWashDaysDTO> autoWashDays;
	private Integer waterPerWash;
	private String lon;

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	private String lat;

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

	public Integer[] getUsers() {
		return users;
	}

	public void setUsers(Integer[] users) {
		this.users = users;
	}

	public String getWashTime() {
		return washTime;
	}

	public void setWashTime(String washTime) {
		this.washTime = washTime;
	}

	public List<AutoWashDaysDTO> getAutoWashDays() {
		return autoWashDays;
	}

	public void setAutoWashDays(List<AutoWashDaysDTO> autoWashDays) {
		this.autoWashDays = autoWashDays;
	}

	public Integer getWaterPerWash() {
		return waterPerWash;
	}

	public void setWaterPerWash(Integer waterPerWash) {
		this.waterPerWash = waterPerWash;
	}
}
