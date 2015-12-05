create index ScheduleIndex on Schedule using btree (
  stop_area,
  route,
  direction,
  schedule_type
);