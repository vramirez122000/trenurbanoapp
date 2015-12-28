-- Ruta expreso Sagrado -> Plaza Las Américas

-- Piñero

WITH RECURSIVE t(n) AS (
  VALUES (time'05:00:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t WHERE n < time'20:00:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'C22' :: varchar(3) route,
     'SAGRADO_CORAZON' :: varchar(20) stop_area,
     'PLAZA_LAS_AMERICAS' :: varchar(20) direction,
     'WORKDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);

-- Plaza las Americas

WITH OFFICIAL_SCHED AS (
    SELECT ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME
    FROM SCHEDULE WHERE ROUTE = 'C22')
INSERT INTO SCHEDULE(route, stop_area, direction, schedule_type, stop_time, error_minutes)
  (SELECT ROUTE, DIRECTION, STOP_AREA, SCHEDULE_TYPE, STOP_TIME + INTERVAL '18 MIN', 2
   FROM OFFICIAL_SCHED);