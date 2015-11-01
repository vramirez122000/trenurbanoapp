-- Ruta expreso Sagrado -> Convenciones
DELETE FROM schedule where route = 'C36';

-- Sagrado

-- Primeros viajes
INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME) VALUES
  ('C36', 'SAGRADO_CORAZON', 'PUNTA_LAS_MARIAS', 'WORKDAY', TIME '05:00:00'),
  ('C36', 'SAGRADO_CORAZON', 'PUNTA_LAS_MARIAS', 'WORKDAY', TIME '05:30:00');

-- Pico matutino
WITH RECURSIVE t(n) AS (
  VALUES (time'06:00:00')
  UNION ALL
  SELECT n + interval '20 min' FROM t WHERE n < time'08:40:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'C35' :: varchar(3) route,
     'SAGRADO_CORAZON' :: varchar(20) stop_area,
     'PUNTA_LAS_MARIAS' :: varchar(20) direction,
     'WORKDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);

--medio dia
WITH RECURSIVE t(n) AS (
  VALUES (time'09:00:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t WHERE n < time'14:30:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'C35' :: varchar(3) route,
     'SAGRADO_CORAZON' :: varchar(20) stop_area,
     'PUNTA_LAS_MARIAS' :: varchar(20) direction,
     'WORKDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);

--pico de la tarde
WITH RECURSIVE t(n) AS (
  VALUES (time'15:00:00')
  UNION ALL
  SELECT n + interval '20 min' FROM t WHERE n < time'18:40:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'C35' :: varchar(3) route,
     'SAGRADO_CORAZON' :: varchar(20) stop_area,
     'PUNTA_LAS_MARIAS' :: varchar(20) direction,
     'WORKDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);

-- Primeros viajes
INSERT INTO SCHEDULE (ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME) VALUES
  ('C36', 'SAGRADO_CORAZON', 'PUNTA_LAS_MARIAS', 'WORKDAY', TIME '19:00:00'),
  ('C36', 'SAGRADO_CORAZON', 'PUNTA_LAS_MARIAS', 'WORKDAY', TIME '19:30:00'),
  ('C36', 'SAGRADO_CORAZON', 'PUNTA_LAS_MARIAS', 'WORKDAY', TIME '20:00:00');


-- Punta las Marias

WITH OFFICIAL_SCHED AS (
    SELECT ROUTE, STOP_AREA, DIRECTION, SCHEDULE_TYPE, STOP_TIME
    FROM SCHEDULE WHERE ROUTE = 'C36')
INSERT INTO SCHEDULE(route, stop_area, direction, schedule_type, stop_time, error_minutes)
  (SELECT ROUTE, DIRECTION, STOP_AREA, SCHEDULE_TYPE, STOP_TIME + INTERVAL '30 MIN', 5
   FROM OFFICIAL_SCHED);