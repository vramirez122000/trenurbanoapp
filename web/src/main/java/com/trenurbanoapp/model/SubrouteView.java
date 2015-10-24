package com.trenurbanoapp.model;

import com.trenurbanoapp.scraper.model.LatLng;

/**
 * Created by victor on 5/5/14.
 */
public class SubrouteView {

    private Subroute subroute = new Subroute();
    private double vehicleTrailAzimuth;
    private double subrouteSegmentAzimuth;
    private double vehicleTrailLength;
    private double subrouteSegmentLength;
    private LatLng subroutePosition;
    private double subrouteM;

    public Subroute getSubroute() {
        return subroute;
    }

    public void setSubroute(Subroute subroute) {
        this.subroute = subroute;
    }

    public double getVehicleTrailAzimuth() {
        return vehicleTrailAzimuth;
    }

    public void setVehicleTrailAzimuth(double vehicleTrailAzimuth) {
        this.vehicleTrailAzimuth = vehicleTrailAzimuth;
    }

    public double getSubrouteSegmentAzimuth() {
        return subrouteSegmentAzimuth;
    }

    public void setSubrouteSegmentAzimuth(double subrouteSegmentAzimuth) {
        this.subrouteSegmentAzimuth = subrouteSegmentAzimuth;
    }

    public double getVehicleTrailLength() {
        return vehicleTrailLength;
    }

    public void setVehicleTrailLength(double vehicleTrailLength) {
        this.vehicleTrailLength = vehicleTrailLength;
    }

    public double getSubrouteSegmentLength() {
        return subrouteSegmentLength;
    }

    public void setSubrouteSegmentLength(double subrouteSegmentLength) {
        this.subrouteSegmentLength = subrouteSegmentLength;
    }

    public LatLng getSubroutePosition() {
        return subroutePosition;
    }

    public void setSubroutePosition(LatLng subroutePosition) {
        this.subroutePosition = subroutePosition;
    }

    public double getSubrouteM() {
        return subrouteM;
    }

    public void setSubrouteM(double subrouteM) {
        this.subrouteM = subrouteM;
    }

    @Override
    public String toString() {
        return "SubrouteView{" +
                "subroute=" + subroute +
                ", vehicleTrailAzimuth=" + vehicleTrailAzimuth +
                ", subrouteSegmentAzimuth=" + subrouteSegmentAzimuth +
                ", vehicleTrailLength=" + vehicleTrailLength +
                ", subrouteSegmentLength=" + subrouteSegmentLength +
                '}';
    }
}
