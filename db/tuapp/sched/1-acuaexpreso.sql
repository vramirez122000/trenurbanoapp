DELETE FROM SCHEDULE where route = 'AX1';

-- San Juan a Cataño

-- mañana
WITH RECURSIVE t(n) AS (
  VALUES (time'06:15:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t
  WHERE n + interval '30 min' <= time'11:15:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'AX1' :: varchar(3) route,
     'SAN_JUAN' :: varchar(20) stop_area,
     'CATANO' :: varchar(20) direction,
     'WORKDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);

--tarde
WITH RECURSIVE t(n) AS (
  VALUES (time'12:00:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t
  WHERE n + interval '30 min' <= time'19:00:00'
)
insert into schedule(route, stop_area, direction, schedule_type, stop_time)
(SELECT
   'AX1' :: varchar(3) route,
   'SAN_JUAN' :: varchar(20) stop_area,
   'CATANO' :: varchar(20) direction,
   'WORKDAY' :: VARCHAR(10) schedule_type,
   n stop_time
 FROM t);

-- Cataño a San Juan

-- mañana
WITH RECURSIVE t(n) AS (
  VALUES (time'06:00:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t
  WHERE n + INTERVAL '30 min' <= time'11:30:00'
)
INSERT INTO schedule(route, stop_area, direction, schedule_type, stop_time)
  (SELECT
     'AX1' :: varchar(3) route,
     'CATANO' :: varchar(20) stop_area,
     'SAN_JUAN' :: varchar(20) direction,
     'WORKDAY' :: VARCHAR(10) schedule_type,
     n stop_time
   FROM t);

-- mañana
WITH RECURSIVE t(n) AS (
  VALUES (time'12:15:00')
  UNION ALL
  SELECT n + interval '30 min' FROM t
  WHERE n + interval '30 min' <= time'18:45:00'
)
INSERT INTO schedule(route, stop_area, direction, schedule_type, stop_time)
(SELECT
   'AX1' :: varchar(3) route,
   'CATANO' :: varchar(20) stop_area,
   'SAN_JUAN' :: varchar(20) direction,
   'WORKDAY' :: VARCHAR(10) schedule_type,
   n stop_time
 FROM t);
