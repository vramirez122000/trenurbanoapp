package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.model.RouteGroup;
import com.trenurbanoapp.model.ScheduleType;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameter;
import de.jollyday.ManagerParameters;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Set;

/**
 * User: victor
 * Date: 12/3/11
 */
class ScheduleTypeCalculator {

    static final String GOOD_FRIDAY = "christian.GOOD_FRIDAY";

    static ScheduleType dayType(RouteGroup routeGroup, LocalDate date) {

        if(routeGroup == RouteGroup.ACUAEXPRESO) {
            return ScheduleType.WORKDAY;
        }

        final DayOfWeek dayOfWeek = date.getDayOfWeek();

        if(routeGroup == RouteGroup.TRANSCRIOLLO) {
            if (DayOfWeek.SUNDAY == dayOfWeek || DayOfWeek.SATURDAY == dayOfWeek) {
                return ScheduleType.RESTDAY;
            }
            HolidayManager atiHolidays = getHolidayManager("TRANSCRIOLLO");
            if (atiHolidays.isHoliday(date)) {
                return ScheduleType.RESTDAY;
            }
            return ScheduleType.WORKDAY;
        }


        if(routeGroup == RouteGroup.ATI_LD) {
            if (DayOfWeek.SUNDAY == dayOfWeek || DayOfWeek.SATURDAY == dayOfWeek) {
                return ScheduleType.RESTDAY;
            }
            HolidayManager atiHolidays = getHolidayManager("ATI");
            if (atiHolidays.isHoliday(date)) {
                return ScheduleType.RESTDAY;
            }
            return ScheduleType.WORKDAY;
        }

        if(routeGroup == RouteGroup.ATI_LS) {
            if (DayOfWeek.SUNDAY == dayOfWeek) {
                return null;
            }

            if (DayOfWeek.SATURDAY == dayOfWeek) {
                return ScheduleType.RESTDAY;
            }

            HolidayManager atiHolidays = getHolidayManager("ATI");
            if (atiHolidays.isHoliday(date)) {
                return ScheduleType.RESTDAY;
            }

            return ScheduleType.WORKDAY;
        }

        if(routeGroup == RouteGroup.ATI_LV) {
            if (DayOfWeek.SUNDAY == dayOfWeek || DayOfWeek.SATURDAY == dayOfWeek) {
                return null;
            }

            HolidayManager atiHolidays = getHolidayManager("ATI");
            if (atiHolidays.isHoliday(date)) {
                return ScheduleType.RESTDAY;
            }

            return ScheduleType.WORKDAY;
        }

        if(routeGroup == RouteGroup.TREN_URBANO) {

            if (dayOfWeek == DayOfWeek.SATURDAY
                    || dayOfWeek == DayOfWeek.SUNDAY) {
                return ScheduleType.RESTDAY;
            }


            final HolidayManager holidayManager = getHolidayManager("TREN_URBANO");
            if (holidayManager.isHoliday(date)) {
                return ScheduleType.RESTDAY;
            }

            return getTrenUrbanoWorkdayType(date, holidayManager);
        }

        return null;

    }

    private static HolidayManager getHolidayManager(String suffix) {
        String path = "holidays/Holidays_" + suffix + ".xml";
        URL resource = ScheduleTypeCalculator.class.getClassLoader().getResource(path);
        ManagerParameter params = ManagerParameters.create(resource);
        return HolidayManager.getInstance(params);
    }

    private static ScheduleType getTrenUrbanoWorkdayType(LocalDate date, HolidayManager holidayManager) {
        LocalDate memorialDay = LocalDate.of(date.getYear(), Month.MAY, 31)
                .with(DayOfWeek.MONDAY);

        LocalDate lastDaySummer = memorialDay.plusWeeks(10);

        if((date.isAfter(memorialDay) || date.isEqual(memorialDay))
                && date.isBefore(lastDaySummer)) {
            return ScheduleType.LOW_SEASON_WORKDAY;
        }

        LocalDate mondayOfChristmas = LocalDate.of(date.getYear(), Month.DECEMBER, 25)
                .with(DayOfWeek.MONDAY);

        LocalDate lastDayChristmasSeason = mondayOfChristmas.plusWeeks(3);

        if((date.isAfter(mondayOfChristmas) || date.isEqual(mondayOfChristmas))
                && date.isBefore(lastDayChristmasSeason)) {
            return ScheduleType.LOW_SEASON_WORKDAY;
        }

        LocalDate goodFriday = null;
        Set<Holiday> holidays = holidayManager.getHolidays(date.getYear());
        for (Holiday holiday : holidays) {
            if(GOOD_FRIDAY.equals(holiday.getPropertiesKey())) {
                goodFriday = LocalDate.of(
                        holiday.getDate().getYear(),
                        holiday.getDate().getMonth(),
                        holiday.getDate().getDayOfMonth());
            }
        }

        if(goodFriday == null) {
            return ScheduleType.WORKDAY;
        }

        LocalDate goodMonday = goodFriday
                .with(DayOfWeek.MONDAY);

        LocalDate mondayAfterGoodSunday = goodMonday
                .plusWeeks(1);

        if((date.isAfter(goodMonday) || date.isEqual(goodMonday))
                && date.isBefore(mondayAfterGoodSunday)) {
            return ScheduleType.LOW_SEASON_WORKDAY;
        }

        return ScheduleType.WORKDAY;
    }


}
