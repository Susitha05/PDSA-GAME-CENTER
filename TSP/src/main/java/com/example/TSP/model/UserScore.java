package com.example.tsp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_scores")
public class UserScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "home_city")
    private String homeCity;

    @Column(length = 1000, name = "selected_cities")
    private String selectedCities; // JSON string of selected city indices

    @Column(length = 1000, name = "shortest_route")
    private String shortestRoute; // JSON string of shortest path

    @Column(name = "total_distance")
    private double totalDistance;

    @Column(name = "correct_dots")
    private int correctDots;

    @Column(name = "accuracy_percentage")
    private double accuracyPercentage;

    // Algorithm execution times in milliseconds
    @Column(name = "algorithm1_time")
    private long algorithm1Time;

    @Column(name = "algorithm2_time")
    private long algorithm2Time;

    @Column(name = "algorithm3_time")
    private long algorithm3Time;

    // Constructors
    public UserScore() {}

    public UserScore(String name, String homeCity, String selectedCities,
                     String shortestRoute, double totalDistance, int correctDots,
                     double accuracyPercentage, long algorithm1Time,
                     long algorithm2Time, long algorithm3Time) {
        this.name = name;
        this.homeCity = homeCity;
        this.selectedCities = selectedCities;
        this.shortestRoute = shortestRoute;
        this.totalDistance = totalDistance;
        this.correctDots = correctDots;
        this.accuracyPercentage = accuracyPercentage;
        this.algorithm1Time = algorithm1Time;
        this.algorithm2Time = algorithm2Time;
        this.algorithm3Time = algorithm3Time;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getSelectedCities() {
        return selectedCities;
    }

    public void setSelectedCities(String selectedCities) {
        this.selectedCities = selectedCities;
    }

    public String getShortestRoute() {
        return shortestRoute;
    }

    public void setShortestRoute(String shortestRoute) {
        this.shortestRoute = shortestRoute;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getCorrectDots() {
        return correctDots;
    }

    public void setCorrectDots(int correctDots) {
        this.correctDots = correctDots;
    }

    public double getAccuracyPercentage() {
        return accuracyPercentage;
    }

    public void setAccuracyPercentage(double accuracyPercentage) {
        this.accuracyPercentage = accuracyPercentage;
    }

    public long getAlgorithm1Time() {
        return algorithm1Time;
    }

    public void setAlgorithm1Time(long algorithm1Time) {
        this.algorithm1Time = algorithm1Time;
    }

    public long getAlgorithm2Time() {
        return algorithm2Time;
    }

    public void setAlgorithm2Time(long algorithm2Time) {
        this.algorithm2Time = algorithm2Time;
    }

    public long getAlgorithm3Time() {
        return algorithm3Time;
    }

    public void setAlgorithm3Time(long algorithm3Time) {
        this.algorithm3Time = algorithm3Time;
    }

    @Override
    public String toString() {
        return "UserScore{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", homeCity='" + homeCity + '\'' +
                ", totalDistance=" + totalDistance +
                ", correctDots=" + correctDots +
                ", accuracyPercentage=" + accuracyPercentage +
                ", algorithm1Time=" + algorithm1Time +
                ", algorithm2Time=" + algorithm2Time +
                ", algorithm3Time=" + algorithm3Time +
                '}';
    }
}