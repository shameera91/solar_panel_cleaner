package com.app.board.model;

import javax.persistence.*;

/**
 * Created by Shameera on May, 2019
 *
 * */

@Entity
@Table(name = "board_wash_dates")
public class BoardWashDays {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String day;

	@ManyToOne
	private Board board;

	private boolean selected;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public boolean getIsSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
