package com.trenurbanoapp.model;

/**
 * Created by victor on 4/23/14.
 */
public class SubrouteKey {

    private String route;
    private String direction;

    public SubrouteKey() {
    }

    public SubrouteKey(String route, String direction) {
        this.route = route;
        this.direction = direction;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Subroute{" +
                ", route='" + route + '\'' +
                ", direction='" + direction + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubrouteKey that = (SubrouteKey) o;

        if (!route.equals(that.route)) return false;
        return !(direction != null ? !direction.equals(that.direction) : that.direction != null);

    }

    @Override
    public int hashCode() {
        int result = route.hashCode();
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        return result;
    }
}
