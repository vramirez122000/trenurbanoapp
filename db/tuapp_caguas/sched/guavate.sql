
-- Terminal de Caguas a Guavate Lunes a Viernes
with trip as ( 
  SELECT 
   'GV' route,
   'GEORGETTI' stop_area,
   'GUAVATE' direction, 
   'WORKDAY' schedule_type 
), stop_times as ( 
 select time '06:00:00' stop_time
 union select time '08:00:00'
 union select time '10:00:00'
 union select time '12:00:00'
 union select time '14:00:00'
 union select time '15:30:00'
 union select time '16:30:00'
 union select time '17:00:00'
 union select time '17:10:00'
 union select time '18:00:00'
 union select time '18:10:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 


-- Terminal de Caguas a Guavate Fin de Semana
with trip as ( 
  SELECT 
   'GV' route,
   'GEORGETTI' stop_area,
   'GUAVATE' direction, 
   'RESTDAY' schedule_type 
), stop_times as ( 
 select time '07:00:00' stop_time
 union select time '09:00:00'
 union select time '11:00:00'
 union select time '13:00:00'
 union select time '15:00:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 
