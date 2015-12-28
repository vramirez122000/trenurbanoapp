package com.trenurbanoapp.model;

/**
 * Created by victor on 5/5/14.
 */
public class SubrouteView {

    private SubrouteKey subrouteKey = new SubrouteKey();
    private double azimuth;
    private double subrouteM;

    public SubrouteKey getSubrouteKey() {
        return subrouteKey;
    }

    public void setSubrouteKey(SubrouteKey subrouteKey) {
        this.subrouteKey = subrouteKey;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getSubrouteM() {
        return subrouteM;
    }

    public void setSubrouteM(double subrouteM) {
        this.subrouteM = subrouteM;
    }
}
