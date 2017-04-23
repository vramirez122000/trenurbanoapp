
-- Terminal de Caguas a Río Cañas, Sector La Changa, Lunes a Viernes
with trip as (
    SELECT
      'CH' route,
      'TERM_CAGUAS' stop_area,
      'CHANGA' direction,
      'WORKDAY' schedule_type
), stop_times as (
  SELECT TIME '05:00:00' stop_time
  UNION SELECT TIME '05:00:00'
  UNION SELECT TIME '06:30:00'
  UNION SELECT TIME '08:00:00'
  UNION SELECT TIME '09:30:00'
  UNION SELECT TIME '11:00:00'
  UNION SELECT TIME '12:30:00'
  UNION SELECT TIME '14:00:00'
  UNION SELECT TIME '15:30:00'
  UNION SELECT TIME '17:00:00'
  UNION SELECT TIME '17:10:00'
) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;

-- Terminal de Caguas a Río Cañas, Sector La Changa, Sabado y domingo

with trip as (
    SELECT
      'CH' route,
      'TERM_CAGUAS' stop_area,
      'CHANGA' direction,
      'RESTDAY' schedule_type
), stop_times as (
  SELECT TIME '07:00:00' stop_time
  UNION SELECT TIME '07:00:00'
  UNION SELECT TIME '08:30:00'
  UNION SELECT TIME '10:00:00'
  UNION SELECT TIME '11:30:00'
  UNION SELECT TIME '13:00:00'
  UNION SELECT TIME '14:30:00'
  UNION SELECT TIME '16:00:00'
) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;
