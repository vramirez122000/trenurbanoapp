package com.trenurbanoapp.model;

/**
 * User: victor
 * Date: 9/9/11
 */
public enum ScheduleType {

    //Weekday not holiday
    WORKDAY("D\u00EDa de Trabajo"),

    //Weekend or holiday
    RESTDAY("D\u00EDa Feriado o Fin de Semana"),

    LOW_SEASON_WORKDAY("D\u00EDa de Trabajo en Temporada Baja");

    private String description;

    ScheduleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
