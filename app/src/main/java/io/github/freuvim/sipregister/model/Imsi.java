package io.github.freuvim.sipregister.model;

public class Imsi {

    private long imsi;
    private int lat;
    private int lon;
    private int cgi;

    public long getImsi() {
        return imsi;
    }

    public void setImsi(long imsi) {
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
