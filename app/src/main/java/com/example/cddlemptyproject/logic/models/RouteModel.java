package com.example.cddlemptyproject.logic.models;

import com.example.cddlemptyproject.logic.processing.DataProcessor;

import java.util.ArrayList;

public class RouteModel {

    private String userName;
    private String routeName;

    private ArrayList<Long> timestamp;
    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;
    private ArrayList<Double> altitude;

    private ArrayList<Double> instantSpeed;



    public RouteModel(String userName, String routeName){
        this.userName = userName;
        this.routeName = routeName;
        this.timestamp = new ArrayList<>();
        this.latitude = new ArrayList<>();
        this.longitude = new ArrayList<>();
        this.altitude = new ArrayList<>();
        this.instantSpeed = new ArrayList<>();
    }

    public void addTimestamp(Long t){
        timestamp.add(t);
    }
    public void addLatitude(Double l){
        latitude.add(l);
    }
    public void addLongitude(Double l){
        longitude.add(l);
    }
    public void addAltitude(Double l){
        altitude.add(l);
    }
    public void addInstantSpeed(Double l){instantSpeed.add(l);}


    public int getSize(){return latitude.size();}

    public String getRouteName() {
        return routeName;
    }

    public String getUserName() {
        return userName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "RouteModel{" +
                "userName='" + userName + '\'' +
                ", routeName='" + routeName + '\'' +
                ", timestamp=" + timestamp +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                '}';
    }



    public Double getTotalTravelledDistance(){
        int i = latitude.size()-1;
        return DataProcessor.distance(latitude.get(0),longitude.get(0),latitude.get(i),longitude.get(i), "K");
    }

    public long getTotalTravelledTimeInSeconds(){
        int i = timestamp.size()-1;

        return timestamp.get(i)-timestamp.get(0);

    }

    public Double getAvgSpeed(){
        double distanceInKilometer = getTotalTravelledDistance();
        long timeInHour = getTotalTravelledTimeInSeconds()/3600;
        return distanceInKilometer/timeInHour;
    }

    public Long getTimestampAtIndex(int i){
        return timestamp.get(i);
    }
    public Double getLatitudeAtIndex(int i){
        return latitude.get(i);
    }
    public Double getLongitudeAtIndex(int i){
        return longitude.get(i);
    }
    public Double getAltitudeAtIndex(int i){
        return altitude.get(i);
    }

}
