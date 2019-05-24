package com.app.board.payload;

/**
 * Created by Shameera on May, 2019
 */
public class AutoWashDaysDTO {

	private String day;
	private boolean selected;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
