package com.app.board.payload;

/**
 * Created by Shameera on May, 2019
 */
public class ViewMessageDTO {

	private String mesage;
	private String dateTime;

	public ViewMessageDTO() {
	}

	public ViewMessageDTO(String mesage, String dateTime) {
		this.mesage = mesage;
		this.dateTime = dateTime;
	}

	public String getMesage() {
		return mesage;
	}

	public void setMesage(String mesage) {
		this.mesage = mesage;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
