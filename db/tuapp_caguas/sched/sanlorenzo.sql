
-- Terminal de Caguas a San Lorenzo Lunes a Viernes
with trip as ( 
  SELECT 
   'SL' route,
   'TERM_CAGUAS' stop_area, 
   'SAN_LORENZO' direction,
   'WORKDAY' schedule_type 
), stop_times as ( 
 select time '05:00:00' stop_time
 union select time '05:10:00'
 union select time '05:20:00'
 union select time '05:40:00'
 union select time '06:00:00'
 union select time '06:10:00'
 union select time '06:20:00'
 union select time '06:30:00'
 union select time '06:40:00'
 union select time '07:00:00'
 union select time '07:20:00'
 union select time '07:40:00'
 union select time '08:00:00'
 union select time '08:20:00'
 union select time '08:40:00'
 union select time '09:00:00'
 union select time '09:20:00'
 union select time '09:40:00'
 union select time '10:00:00'
 union select time '10:20:00'
 union select time '10:40:00'
 union select time '11:00:00'
 union select time '11:20:00'
 union select time '11:40:00'
 union select time '12:00:00'
 union select time '12:20:00'
 union select time '12:40:00'
 union select time '13:00:00'
 union select time '13:20:00'
 union select time '13:40:00'
 union select time '14:00:00'
 union select time '14:20:00'
 union select time '14:40:00'
 union select time '15:00:00'
 union select time '15:20:00'
 union select time '15:40:00'
 union select time '16:00:00'
 union select time '16:20:00'
 union select time '16:40:00'
 union select time '17:00:00'
 union select time '17:10:00'
 union select time '18:00:00'
 union select time '18:10:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 


-- Terminal de Caguas a San Lorenzo Fin de Semana
with trip as ( 
  SELECT 
   'SL' route,
   'TERM_CAGUAS' stop_area, 
   'SAN_LORENZO' direction,
   'RESTDAY' schedule_type 
), stop_times as ( 
 select time '07:00:00' stop_time
 union select time '08:00:00'
 union select time '09:00:00'
 union select time '10:00:00'
 union select time '11:00:00'
 union select time '12:00:00'
 union select time '13:00:00'
 union select time '14:00:00'
 union select time '15:00:00'
 union select time '16:00:00'

) INSERT INTO schedule (route, stop_area, direction, schedule_type, stop_time)
  select trip.route, trip.stop_area, trip.direction, trip.schedule_type, stop_times.stop_time
  from trip, stop_times order by stop_times.stop_time; 
