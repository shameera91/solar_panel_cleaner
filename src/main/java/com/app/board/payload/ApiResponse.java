package com.app.board.payload;

/**
 * Created by Shameera on May, 2019
 */
public class ApiResponse {

	private Object data;
	private Boolean error;
	private String message;

	public ApiResponse() {

	}

	public ApiResponse(Boolean error, String message) {
		this.error = error;
		this.message = message;
	}

	public ApiResponse(Object data, Boolean error, String message) {
		this.data = data;
		this.error = error;
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
