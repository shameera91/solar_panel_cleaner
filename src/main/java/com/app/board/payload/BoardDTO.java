package com.app.board.payload;

/**
 * Created by Shameera on May, 2019
 */
public class BoardDTO {
	private Integer id;
	private Integer boardIdentity;
	private String location;
	private String status;
	private String washDateTime;
	private String lastWash;
	private int numberOfWashes;
	private Integer waterPerWash;
	private Double factor;
    private String boardName;

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;

	public Integer getBoardIdentity() {
		return boardIdentity;
	}

	public void setBoardIdentity(Integer boardIdentity) {
		this.boardIdentity = boardIdentity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWashDateTime() {
		return washDateTime;
	}

	public void setWashDateTime(String washDateTime) {
		this.washDateTime = washDateTime;
	}

	public String getLastWash() {
		return lastWash;
	}

	public void setLastWash(String lastWash) {
		this.lastWash = lastWash;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getNumberOfWashes() {
		return numberOfWashes;
	}

	public void setNumberOfWashes(int numberOfWashes) {
		this.numberOfWashes = numberOfWashes;
	}

	public Integer getWaterPerWash() {
		return waterPerWash;
	}

	public void setWaterPerWash(Integer waterPerWash) {
		this.waterPerWash = waterPerWash;
	}

	public Double getFactor() {
		return factor;
	}

	public void setFactor(Double factor) {
		this.factor = factor;
	}
}
