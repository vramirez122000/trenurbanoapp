package com.trenurbanoapp.model;

/**
 * User: victor
 * Date: 9/9/11
 */
public enum ScheduleType {

    //Weekday not holiday
    WORKDAY("D\u00EDa laboral"),

    //Weekend or holiday
    RESTDAY("Fin de semana o feriado"),

    LOW_SEASON_WORKDAY("D\u00EDa laboral en temporada baja");

    private String description;

    ScheduleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
