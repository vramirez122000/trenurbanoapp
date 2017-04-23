--No stop areas. Left blank
with stop_area_tmp(id, "desc", sort_order, lng, lat) as (
        select 'TERM_CAGUAS',   'Terminal Caguas',                1,  -66.032452, 18.235060
  union select 'GEORGETTI',     'Calle Georgetti',                2,  -66.035114, 18.231229
  union select 'AGUAS_BUENAS',  'Aguas Buenas',                   3,  -66.035114, 18.231229
  union select 'BAIROA',        'Bo. Bairoa, Antigua Vía 25',     3,  -66.035114, 18.231229
  union select 'BEATRIZ',       'Bo. Beatriz',                    3,  -66.035114, 18.231229
  union select 'BONNEVILLE',    'Urb. Bonneville',                3,  -66.035114, 18.231229
  union select 'CHANGA',        'Bo. Río Cañas, Sect. La Changa', 3,  -66.035114, 18.231229
  union select 'HORMIGAS',      'Hormigas',                       3,  -66.035114, 18.231229
)
insert into ref.stop_area (id, "desc", lng, lat, geom, sort_order)
  select id, "desc", lng, lat, ST_SetSRID(ST_Point(lng, lat), 4326), sort_order from stop_area_tmp;
