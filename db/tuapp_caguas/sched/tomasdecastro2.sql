
-- Terminal de Caguas a Tomas de Castro 2 Lunes a Viernes
with trip as ( 
  SELECT 
   'T2' route,
   'GEORGETTI' stop_area,
   'TOMAS_DE_CASTRO_2' direction, 
   'WORKDAY' schedule_type 
), stop_times as ( 
 select '06:00:00' stop_time
 union select '08:00:00'
 union select '10:00:00'
 union select '12:00:00'
 union select '14:00:00'
 union select '17:00:00'
 union select '17:10:00'
 union select '18:00:00'
 union select '18:10:00'

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
 select '07:00:00' stop_time
 union select '09:00:00'
 union select '11:00:00'
 union select '13:00:00'
 union select '15:00:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 
