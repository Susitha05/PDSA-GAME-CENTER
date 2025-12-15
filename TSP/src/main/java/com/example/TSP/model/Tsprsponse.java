package com.example.tsp.model;

import java.util.List;

public class Tsprsponse {
    public List<city> points;
    public List<Integer> shortestPath;
    public double distance;
    public int homeCityIndex;

    // Algorithm 1: Nearest Neighbor results
    public List<Integer> algorithm1Path;
    public double algorithm1Distance;
    public double algorithm1Time;

    // Algorithm 2: Dynamic Programming results
    public List<Integer> algorithm2Path;
    public double algorithm2Distance;
    public double algorithm2Time;

    // Algorithm 3: Genetic Algorithm results
    public List<Integer> algorithm3Path;
    public double algorithm3Distance;
    public double algorithm3Time;

    // Best algorithm name
    public String bestAlgorithm;

    // Constructors
    public Tsprsponse() {}

    // Getters and Setters
    public List<city> getPoints() {
        return points;
    }

    public void setPoints(List<city> points) {
        this.points = points;
    }

    public List<Integer> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Integer> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getHomeCityIndex() {
        return homeCityIndex;
    }

    public void setHomeCityIndex(int homeCityIndex) {
        this.homeCityIndex = homeCityIndex;
    }

    public List<Integer> getAlgorithm1Path() {
        return algorithm1Path;
    }

    public void setAlgorithm1Path(List<Integer> algorithm1Path) {
        this.algorithm1Path = algorithm1Path;
    }

    public double getAlgorithm1Distance() {
        return algorithm1Distance;
    }

    public void setAlgorithm1Distance(double algorithm1Distance) {
        this.algorithm1Distance = algorithm1Distance;
    }

    public double getAlgorithm1Time() {
        return algorithm1Time;
    }

    public void setAlgorithm1Time(double algorithm1Time) {
        this.algorithm1Time = algorithm1Time;
    }

    public List<Integer> getAlgorithm2Path() {
        return algorithm2Path;
    }

    public void setAlgorithm2Path(List<Integer> algorithm2Path) {
        this.algorithm2Path = algorithm2Path;
    }

    public double getAlgorithm2Distance() {
        return algorithm2Distance;
    }

    public void setAlgorithm2Distance(double algorithm2Distance) {
        this.algorithm2Distance = algorithm2Distance;
    }

    public double getAlgorithm2Time() {
        return algorithm2Time;
    }

    public void setAlgorithm2Time(double algorithm2Time) {
        this.algorithm2Time = algorithm2Time;
    }

    public List<Integer> getAlgorithm3Path() {
        return algorithm3Path;
    }

    public void setAlgorithm3Path(List<Integer> algorithm3Path) {
        this.algorithm3Path = algorithm3Path;
    }

    public double getAlgorithm3Distance() {
        return algorithm3Distance;
    }

    public void setAlgorithm3Distance(double algorithm3Distance) {
        this.algorithm3Distance = algorithm3Distance;
    }

    public double getAlgorithm3Time() {
        return algorithm3Time;
    }

    public void setAlgorithm3Time(double algorithm3Time) {
        this.algorithm3Time = algorithm3Time;
    }

    public String getBestAlgorithm() {
        return bestAlgorithm;
    }

    public void setBestAlgorithm(String bestAlgorithm) {
        this.bestAlgorithm = bestAlgorithm;
    }

    @Override
    public String toString() {
        return "TspResponse{" +
                "points=" + points +
                ", shortestPath=" + shortestPath +
                ", distance=" + distance +
                ", homeCityIndex=" + homeCityIndex +
                ", bestAlgorithm='" + bestAlgorithm + '\'' +
                '}';
    }
}