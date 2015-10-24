package com.trenurbanoapp.scraper.model;

/**
 * User: victor
 * Date: 5/7/13
 */
public final class LatLng {

    private double lat;
    private double lng;

    public LatLng() {
    }

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LatLng latLng = (LatLng) o;

        if (Double.compare(latLng.lat, lat) != 0) return false;
        if (Double.compare(latLng.lng, lng) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "[" + lat + ',' + lng + ']';
    }
}
