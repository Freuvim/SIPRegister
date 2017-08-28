package io.github.freuvim.sipregister.model;

import java.io.Serializable;

public class Imsi implements Serializable{

    private String imsi;
    private int lat;
    private int lon;
    private int cgi;

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

    public int getCgi() {
        return cgi;
    }

    public void setCgi(int cgi) {
        this.cgi = cgi;
    }
}
