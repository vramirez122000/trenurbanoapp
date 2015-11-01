drop table if exists ref.schedule;

CREATE TABLE ref.schedule
(
  route varchar(20) NOT NULL,
  stop_area varchar(20) NOT NULL,
  direction varchar(20) NOT NULL,
  schedule_type varchar(20) NOT NULL,
  stop_time time NOT NULL,
  error_minutes smallint NOT NULL DEFAULT 0,
  primary key (route, stop_area, direction, schedule_type, stop_time)
);
