"DepartureIntervalP1W":"
05:00:00
05:30:00
06:00:00
06:30:00
07:00:00
08:00:00
09:00:00
10:00:00
11:00:00
12:00:00
13:00:00
14:00:00
15:00:00
16:00:00
17:00:00
17:10:00
18:00:00
18:10:00
,1","DepartureIntervalP1S":"
07:00:00
08:30:00
10:00:00
11:30:00
13:00:00
14:30:00
16:00:00
,1","DepartureIntervalP1SnH":"
700
830
1000
1130
1300
1430
1600,1"},


-- Terminal de Caguas a Urb. Bonneville Lunes a Viernes
with trip as (
    SELECT
      'CM' route,
      'TERM_CAGUAS' stop_area,
      'CENTRO_MEDICO' direction,
      'WORKDAY' schedule_type
), stop_times as (
  SELECT TIME '05:00:00' stop_time
  UNION SELECT TIME '05:30:00'
  UNION SELECT TIME '06:00:00'
  UNION SELECT TIME '06:30:00'
  UNION SELECT TIME '07:00:00'
  UNION SELECT TIME '08:00:00'
  UNION SELECT TIME '09:00:00'
  UNION SELECT TIME '10:00:00'
  UNION SELECT TIME '11:00:00'
  UNION SELECT TIME '12:00:00'
  UNION SELECT TIME '13:00:00'
  UNION SELECT TIME '14:00:00'
  UNION SELECT TIME '15:00:00'
  UNION SELECT TIME '16:00:00'
  UNION SELECT TIME '17:00:00'
  UNION SELECT TIME '17:10:00'
  UNION SELECT TIME '18:00:00'
  UNION SELECT TIME '18:10:00'
) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;

-- Terminal de Caguas a Barrio Beatriz Sabado y domingo

with trip as (
    SELECT
      'CM' route,
      'TERM_CAGUAS' stop_area,
      'CENTRO_MEDICO' direction,
      'RESTDAY' schedule_type
), stop_times as (
        SELECT TIME '07:00:00' stop_time
  UNION SELECT TIME '08:30:00'
  UNION SELECT TIME '10:00:00'
  UNION SELECT TIME '11:30:00'
  UNION SELECT TIME '13:00:00'
  UNION SELECT TIME '14:30:00'
  UNION SELECT TIME '16:00:00'
) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time;
