--No stop areas. Left blank
/*with stop_area_tmp(id, "desc", sort_order, lng, lat) as (
  select 'TERM_CAGUAS',     'Terminal Caguas',         1,  -66.032452, 18.235060 union
  select 'TERM_SAN_LORENZO','Terminal San Lorenzo',    1,  -65.961513, 18.187426 union
  select 'VALLE_TOLIMA',    'Valle Tolima',            1,  -66.046304, 18.242135
)
insert into ref.stop_area (id, "desc", lng, lat, geom, sort_order)
  select id, "desc", lng, lat, ST_SetSRID(ST_Point(lng, lat), 4326), sort_order from stop_area_tmp;*/
