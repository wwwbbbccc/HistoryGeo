package com.history.geography;

public class Provinces {
    private int no;
    private String location_id;
    private String name_chs;
    private String parent_id;
    private String lng;
    private String lat;
    private String source;

    public Provinces(int no, String location_id, String name_chs, String parent_id, String lng, String lat, String source) {
        this.no = no;
        this.location_id = location_id;
        this.name_chs = name_chs;
        this.parent_id = parent_id;
        this.lng = lng;
        this.lat = lat;
        this.source = source;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getName_chs() {
        return name_chs;
    }

    public void setName_chs(String name_chs) {
        this.name_chs = name_chs;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
