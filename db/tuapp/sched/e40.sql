-- Ruta expreso Piñero -> Aeropuerto
DELETE FROM schedule where route = 'E40';

-- Piñero

WITH RECURSIVE t(n) AS (
  VALUES (time'05:00:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t WHERE n + interval '30 min' <= time'20:00:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'E40' :: varchar(3) route,
     'PINERO' :: varchar(10) stop_area,
     'AEROPUERTO' :: varchar(10) direction,
     'WORKDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);


WITH RECURSIVE t(n) AS (
  VALUES (time'05:00:00')
  UNION ALL
  SELECT n + interval '1 hour' FROM t WHERE n + interval '1 hour' <= time'20:00:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'E40' :: varchar(3) route,
     'PINERO' :: varchar(10) stop_area,
     'AEROPUERTO' :: varchar(10) direction,
     'RESTDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);

-- Aeropuerto

WITH RECURSIVE t(n) AS (
  VALUES (time'05:20:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t WHERE n + interval '30 min' <= time'20:00:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time, error_minutes)
(SELECT
   'E40' :: varchar(3) route,
   'AEROPUERTO' :: varchar(10) stop_area,
   'PINERO' :: varchar(10) direction,
   'WORKDAY' :: VARCHAR(10) schedule_type,
   n stop_time,
   2 :: smallint error_minutes
 FROM t);

WITH RECURSIVE t(n) AS (
  VALUES (time'05:20:00')
  UNION ALL
  SELECT n + interval '1 hour' FROM t WHERE n + interval '1 hour' <= time'20:00:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time, error_minutes)
(SELECT
   'E40' :: varchar(3) route,
   'AEROPUERTO' :: varchar(10) stop_area,
   'PINERO' :: varchar(10) direction,
   'RESTDAY' :: VARCHAR(10) schedule_type,
   n stop_time,
   2 :: smallint error_minutes
 FROM t);