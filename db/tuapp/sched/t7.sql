
-- San Juan a Sagrado Corazon Lunes a Viernes
with trip as (
    SELECT
      'T7' route,
      'CUPEY' stop_area,
      'CAROLINA' direction,
      'WORKDAY' schedule_type
), stop_times as (
        select TIME '05:00:00' stop_time
  union select TIME '05:30:00'
  union select TIME '06:15:00'
  union select TIME '06:40:00'
  union select TIME '07:05:00'
  union select TIME '07:30:00'
  union select TIME '07:55:00'
  union select TIME '08:20:00'
  union select TIME '08:45:00'
  union select TIME '09:15:00'
  union select TIME '09:45:00'
  union select TIME '10:15:00'
  union select TIME '10:45:00'
  union select TIME '11:15:00'
  union select TIME '11:45:00'
  union select TIME '12:15:00'
  union select TIME '12:45:00'
  union select TIME '13:15:00'
  union select TIME '13:45:00'
  union select TIME '14:15:00'
  union select TIME '14:45:00'
  union select TIME '15:10:00'
  union select TIME '15:35:00'
  union select TIME '16:00:00'
  union select TIME '16:25:00'
  union select TIME '16:50:00'
  union select TIME '17:15:00'
  union select TIME '17:40:00'
  union select TIME '18:05:00'
  union select TIME '18:30:00'
  union select TIME '18:55:00'
  union select TIME '19:25:00'
  union select TIME '19:55:00'
  union select TIME '20:55:00'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;

-- Carolina a Cupey Lunes a Viernes
with trip as (
    SELECT
      'T7' route,
      'CAROLINA' stop_area,
      'CUPEY' direction,
      'WORKDAY' schedule_type
), stop_times as (
        select TIME '05:03:00' stop_time
  union select TIME '05:33:00'
  union select TIME '06:07:00'
  union select TIME '06:32:00'
  union select TIME '06:57:00'
  union select TIME '07:22:00'
  union select TIME '07:47:00'
  union select TIME '08:12:00'
  union select TIME '08:37:00'
  union select TIME '09:02:00'
  union select TIME '09:27:00'
  union select TIME '09:52:00'
  union select TIME '10:21:00'
  union select TIME '10:51:00'
  union select TIME '11:21:00'
  union select TIME '11:51:00'
  union select TIME '12:21:00'
  union select TIME '12:51:00'
  union select TIME '13:21:00'
  union select TIME '13:51:00'
  union select TIME '14:21:00'
  union select TIME '14:51:00'
  union select TIME '15:21:00'
  union select TIME '15:51:00'
  union select TIME '16:17:00'
  union select TIME '16:42:00'
  union select TIME '17:07:00'
  union select TIME '17:32:00'
  union select TIME '17:57:00'
  union select TIME '18:22:00'
  union select TIME '18:47:00'
  union select TIME '19:12:00'
  union select TIME '19:37:00'
  union select TIME '20:02:00'
  union select TIME '20:59:00'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;



-- Cupey a Carolina Sabados y feriados
with trip as (
    SELECT
      'T7' route,
      'CUPEY' stop_area,
      'CAROLINA' direction,
      'RESTDAY' schedule_type
), stop_times as (
        select TIME '05:50:00' stop_time
  union select TIME '06:30:00'
  union select TIME '07:15:00'
  union select TIME '07:30:00'
  union select TIME '08:00:00'
  union select TIME '08:30:00'
  union select TIME '09:00:00'
  union select TIME '09:30:00'
  union select TIME '10:00:00'
  union select TIME '10:30:00'
  union select TIME '11:00:00'
  union select TIME '11:30:00'
  union select TIME '12:00:00'
  union select TIME '12:30:00'
  union select TIME '13:00:00'
  union select TIME '13:30:00'
  union select TIME '14:00:00'
  union select TIME '14:30:00'
  union select TIME '15:00:00'
  union select TIME '15:30:00'
  union select TIME '16:00:00'
  union select TIME '16:30:00'
  union select TIME '17:00:00'
  union select TIME '17:30:00'
  union select TIME '18:00:00'
  union select TIME '18:20:00'
  union select TIME '19:00:00'
  union select TIME '19:30:00'
  union select TIME '20:00:00'
  union select TIME '20:20:00'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;

-- Carolina a Cupey sabados y feriados
with trip as (
    SELECT
      'T7' route,
      'CAROLINA' stop_area,
      'CUPEY' direction,
      'RESTDAY' schedule_type
), stop_times as (
        select TIME '06:15' stop_time
  union select TIME '06:41'
  union select TIME '07:21'
  union select TIME '08:06'
  union select TIME '08:21'
  union select TIME '08:51'
  union select TIME '09:21'
  union select TIME '09:53'
  union select TIME '10:23'
  union select TIME '10:53'
  union select TIME '11:23'
  union select TIME '11:53'
  union select TIME '12:23'
  union select TIME '12:53'
  union select TIME '13:23'
  union select TIME '13:53'
  union select TIME '14:23'
  union select TIME '14:53'
  union select TIME '15:23'
  union select TIME '15:53'
  union select TIME '16:23'
  union select TIME '16:53'
  union select TIME '17:23'
  union select TIME '17:53'
  union select TIME '18:23'
  union select TIME '18:53'
  union select TIME '19:13'
  union select TIME '19:52'
  union select TIME '20:22'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;
