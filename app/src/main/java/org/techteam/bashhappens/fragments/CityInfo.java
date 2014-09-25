package org.techteam.bashhappens.fragments;

public class CityInfo {
    String city;
    double lat;
    double lng;

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


}
