
-- San Juan a Sagrado Corazon Lunes a Viernes
with trip as (
    SELECT
      'T21' route,
      'SAN_JUAN' stop_area,
      'SAGRADO_CORAZON' direction,
      'WORKDAY' schedule_type
), stop_times as (
        select TIME '05:00:00' stop_time
  union select TIME '05:40:00'
  union select TIME '06:20:00'
  union select TIME '06:41:00'
  union select TIME '07:02:00'
  union select TIME '07:23:00'
  union select TIME '07:44:00'
  union select TIME '08:05:00'
  union select TIME '08:26:00'
  union select TIME '08:47:00'
  union select TIME '09:17:00'
  union select TIME '09:32:00'
  union select TIME '09:47:00'
  union select TIME '10:17:00'
  union select TIME '10:47:00'
  union select TIME '11:17:00'
  union select TIME '11:47:00'
  union select TIME '12:17:00'
  union select TIME '12:47:00'
  union select TIME '13:17:00'
  union select TIME '13:47:00'
  union select TIME '14:17:00'
  union select TIME '14:47:00'
  union select TIME '15:08:00'
  union select TIME '15:29:00'
  union select TIME '15:50:00'
  union select TIME '16:11:00'
  union select TIME '16:32:00'
  union select TIME '16:53:00'
  union select TIME '17:14:00'
  union select TIME '17:35:00'
  union select TIME '17:56:00'
  union select TIME '18:17:00'
  union select TIME '18:38:00'
  union select TIME '18:59:00'
  union select TIME '19:29:00'
  union select TIME '19:59:00'
  union select TIME '20:29:00'
  union select TIME '20:59:00'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
from trip, stop_times order by stop_times.stop_time;

-- Sagrado Corazon a San Juan Lunes a Viernes
with trip as (
    SELECT
      'T21' route,
      'SAGRADO_CORAZON' stop_area,
      'SAN_JUAN' direction,
      'WORKDAY' schedule_type
), stop_times as (
        select TIME '05:00:00' stop_time
  union select TIME '05:40:00'
  union select TIME '06:20:00'
  union select TIME '07:00:00'
  union select TIME '07:23:00'
  union select TIME '07:44:00'
  union select TIME '08:05:00'
  union select TIME '08:26:00'
  union select TIME '08:47:00'
  union select TIME '09:08:00'
  union select TIME '09:29:00'
  union select TIME '09:57:00'
  union select TIME '10:22:00'
  union select TIME '10:37:00'
  union select TIME '10:57:00'
  union select TIME '11:27:00'
  union select TIME '11:57:00'
  union select TIME '12:27:00'
  union select TIME '12:57:00'
  union select TIME '13:27:00'
  union select TIME '13:57:00'
  union select TIME '14:27:00'
  union select TIME '14:57:00'
  union select TIME '15:27:00'
  union select TIME '15:50:00'
  union select TIME '16:11:00'
  union select TIME '16:32:00'
  union select TIME '16:53:00'
  union select TIME '17:14:00'
  union select TIME '17:35:00'
  union select TIME '17:56:00'
  union select TIME '18:17:00'
  union select TIME '18:38:00'
  union select TIME '18:59:00'
  union select TIME '19:20:00'
  union select TIME '19:41:00'
  union select TIME '20:09:00'
  union select TIME '20:39:00'
  union select TIME '21:09:00'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;



-- San Juan a Sagrado Corazon Sabados y feriados
with trip as (
    SELECT
      'T21' route,
      'SAN_JUAN' stop_area,
      'SAGRADO_CORAZON' direction,
      'RESTDAY' schedule_type
), stop_times as (
        select TIME '06:00:00' stop_time
  union select TIME '06:30:00'
  union select TIME '07:00:00'
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
  union select TIME '18:30:00'
  union select TIME '19:05:00'
  union select TIME '19:40:00'
  union select TIME '20:25:00'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;

-- Sagrado Corazon a San Juan sabados y feriados
with trip as (
    SELECT
      'T21' route,
      'SAGRADO_CORAZON' stop_area,
      'SAN_JUAN' direction,
      'RESTDAY' schedule_type
), stop_times as (
        select TIME '06:41:00' stop_time
  union select TIME '07:11:00'
  union select TIME '07:41:00'
  union select TIME '08:11:00'
  union select TIME '08:41:00'
  union select TIME '09:11:00'
  union select TIME '09:42:00'
  union select TIME '10:12:00'
  union select TIME '10:42:00'
  union select TIME '11:12:00'
  union select TIME '11:42:00'
  union select TIME '12:12:00'
  union select TIME '12:42:00'
  union select TIME '13:12:00'
  union select TIME '13:42:00'
  union select TIME '14:12:00'
  union select TIME '14:42:00'
  union select TIME '15:12:00'
  union select TIME '15:42:00'
  union select TIME '16:12:00'
  union select TIME '16:42:00'
  union select TIME '17:12:00'
  union select TIME '17:42:00'
  union select TIME '18:12:00'
  union select TIME '18:42:00'
  union select TIME '19:15:00'
  union select TIME '19:45:00'
  union select TIME '20:20:00'
) INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;
