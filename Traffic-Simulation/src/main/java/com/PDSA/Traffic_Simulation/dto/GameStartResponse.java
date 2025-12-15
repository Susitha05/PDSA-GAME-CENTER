package com.PDSA.Traffic_Simulation.dto;

import java.util.List;

public class GameStartResponse {
    private Long roundId;
    private List<RoadCapacity> roadCapacities;
    private String networkGraph; // JSON representation

    public GameStartResponse(Long roundId, List<RoadCapacity> roadCapacities, String networkGraph) {
        this.roundId = roundId;
        this.roadCapacities = roadCapacities;
        this.networkGraph = networkGraph;
    }

    public GameStartResponse() {}

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public List<RoadCapacity> getRoadCapacities() {
        return roadCapacities;
    }

    public void setRoadCapacities(List<RoadCapacity> roadCapacities) {
        this.roadCapacities = roadCapacities;
    }

    public String getNetworkGraph() {
        return networkGraph;
    }

    public void setNetworkGraph(String networkGraph) {
        this.networkGraph = networkGraph;
    }
}
