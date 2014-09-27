package org.techteam.bashhappens.fragments;

public class CityInfo {
    private String city;
    private double lat;
    private double lng;

    public CityInfo() {
        city = "";
        lat = 0.0;
        lng = 0.0;
    }

    public CityInfo(String city, double lat, double lng) {
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }


    public String getCity() {
        return city;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
