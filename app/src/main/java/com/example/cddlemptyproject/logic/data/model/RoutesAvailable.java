package com.example.cddlemptyproject.logic.data.model;

import java.util.List;

public class RoutesAvailable {

    private List<String> latitude;
    private List<String> longitude;
    private String routeName;

    public RoutesAvailable(String routeName){
        this.routeName = routeName;
    }

    public List<String> getLatitude() {
        return latitude;
    }

    public List<String> getLongitude() {
        return longitude;
    }

    public String getRouteName() {
        return routeName;
    }

}
