package com.myjwtai.jwtai.payload.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class LockSeatsRequest {
    @NotNull(message = "Show ID is required")
    private Long showId;
    
    @NotEmpty(message = "You must select at least one seat to lock")
    private List<Long> showSeatIds;

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public List<Long> getShowSeatIds() {
        return showSeatIds;
    }

    public void setShowSeatIds(List<Long> showSeatIds) {
        this.showSeatIds = showSeatIds;
    }
}
