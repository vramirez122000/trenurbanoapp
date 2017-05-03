
-- Terminal de Caguas a Tomas de Castro 2 Lunes a Viernes
with trip as ( 
  SELECT 
   'T2' route,
   'GEORGETTI' stop_area,
   'TOMAS_DE_CASTRO_2' direction, 
   'WORKDAY' schedule_type 
), stop_times as ( 
 select time '06:00:00' stop_time
 union select time '08:00:00'
 union select time '10:00:00'
 union select time '12:00:00'
 union select time '14:00:00'
 union select time '17:00:00'
 union select time '17:10:00'
 union select time '18:00:00'
 union select time '18:10:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 


-- Terminal de Caguas a Tomas de Castro 2 Fin de Semana
with trip as ( 
  SELECT 
   'T2' route,
   'GEORGETTI' stop_area,
   'TOMAS_DE_CASTRO_2' direction, 
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
