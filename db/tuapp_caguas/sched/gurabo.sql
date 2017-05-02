
-- Terminal de Caguas a Gurabo Lunes a Viernes
with trip as ( 
  SELECT 
   'GU' route,
   'TERM_CAGUAS' stop_area, 
   'GURABO' direction, 
   'WORKDAY' schedule_type 
), stop_times as ( 
 select '05:00:00' stop_time
 union select '05:10:00'
 union select '05:20:00'
 union select '05:40:00'
 union select '06:00:00'
 union select '06:10:00'
 union select '06:20:00'
 union select '06:30:00'
 union select '06:40:00'
 union select '06:50:00'
 union select '07:00:00'
 union select '07:20:00'
 union select '07:40:00'
 union select '08:00:00'
 union select '08:20:00'
 union select '08:40:00'
 union select '09:00:00'
 union select '09:20:00'
 union select '09:40:00'
 union select '10:00:00'
 union select '10:20:00'
 union select '10:40:00'
 union select '11:00:00'
 union select '11:20:00'
 union select '11:40:00'
 union select '12:00:00'
 union select '12:20:00'
 union select '12:40:00'
 union select '13:00:00'
 union select '13:20:00'
 union select '13:40:00'
 union select '14:00:00'
 union select '14:20:00'
 union select '14:40:00'
 union select '15:00:00'
 union select '15:20:00'
 union select '15:40:00'
 union select '16:00:00'
 union select '16:30:00'
 union select '17:00:00'
 union select '17:10:00'
 union select '18:00:00'
 union select '18:10:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 


-- Terminal de Caguas a Gurabo Fin de Semana
with trip as ( 
  SELECT 
   'GU' route,
   'TERM_CAGUAS' stop_area, 
   'GURABO' direction, 
   'RESTDAY' schedule_type 
), stop_times as ( 
 select '07:00:00' stop_time
 union select '08:00:00'
 union select '09:00:00'
 union select '10:00:00'
 union select '11:00:00'
 union select '12:00:00'
 union select '13:00:00'
 union select '14:00:00'
 union select '15:00:00'
 union select '16:00:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 
