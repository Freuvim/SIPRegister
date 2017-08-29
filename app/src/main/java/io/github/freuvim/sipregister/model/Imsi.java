package io.github.freuvim.sipregister.model;

import java.io.Serializable;

public class Imsi implements Serializable{

    private String imsi;
    private int lat;
    private int lon;
    private int lac;

    public Imsi() {}
    public Imsi(String imsi, int lat, int lon, int lac) {
        this.imsi = imsi;
        this.lat = lat;
        this.lon = lon;
        this.lac = lac;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }
}
