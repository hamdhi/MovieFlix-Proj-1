package com.myjwtai.jwtai.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SeatRequest {
    @NotNull(message = "Screen ID is required")
    private Long screenId;
    
    @NotBlank(message = "Seat number cannot be empty")
    private String seatNumber;

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
