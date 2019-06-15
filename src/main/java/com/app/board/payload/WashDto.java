package com.app.board.payload;

public class WashDto {
    String washTime;
    int numberOfWashes;

    public WashDto() {

    }

    public String getWashTime() {
        return washTime;
    }

    public void setWashTime(String washTime) {
        this.washTime = washTime;
    }

    public int getNumberOfWashes() {
        return numberOfWashes;
    }

    public void setNumberOfWashes(int numberOfWashes) {
        this.numberOfWashes = numberOfWashes;
    }

    public WashDto(String washTime, int numberOfWashes) {
        this.washTime = washTime;
        this.numberOfWashes = numberOfWashes;
    }
}
