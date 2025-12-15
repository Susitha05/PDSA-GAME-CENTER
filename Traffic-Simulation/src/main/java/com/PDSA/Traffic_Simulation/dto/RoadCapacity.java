package com.PDSA.Traffic_Simulation.dto;

public class RoadCapacity {
    private String from;
    private String to;
    private Integer capacity;

    public RoadCapacity(String from, String to, Integer capacity) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
    }

    public RoadCapacity() {}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
}
