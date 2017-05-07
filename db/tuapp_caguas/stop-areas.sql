--No stop areas. Left blank
with stop_area_tmp(id, "desc", sort_order, sched_origin, lng, lat) as (
        select 'TERM_CAGUAS',   'Terminal Caguas',                1, true,  -66.032452, 18.235060
  union select 'GEORGETTI',     'Calle Georgetti',                2, true,  -66.035114, 18.231229
  union select 'AGUAS_BUENAS',  'Aguas Buenas',                   3, false,  -66.035114, 18.231229
  union select 'BAIROA',        'Bo. Bairoa, Antigua Vía 25',     3, false,  -66.035114, 18.231229
  union select 'BEATRIZ',       'Bo. Beatriz',                    3, false,  -66.035114, 18.231229
  union select 'BONNEVILLE',    'Urb. Bonneville',                3, false,  -66.035114, 18.231229
  union select 'CHANGA',        'Bo. Río Cañas, Sect. La Changa', 3, false,  -66.035114, 18.231229
  union select 'HORMIGAS',      'Hormigas',                       3, false,  -66.035114, 18.231229
  union select 'BO_CANABONCITO','Bo. Cañaboncito',                3, false,  -66.035114, 18.231229
  union select 'BO_LAMESA',     'Bo. La Mesa, Sect. La Barra',    3, false,  -66.035114, 18.231229
  union select 'BO_BORINQUEN',  'Bo. Borinquen',                  3, false,  -66.035114, 18.231229
  union select 'CAGUAX',        'Urb. Caguax',                    3, false,  -66.035114, 18.231229
  union select 'LAS_CAROLINAS', 'Parc. Las Carolinas',            3, false,  -66.035114, 18.231229
  union select 'CASTELLON_DELGADO', 'Castellón-Delgado',          3, false,  -66.035114, 18.231229
  union select 'CENTRO_MEDICO', 'Centro Médico',                  3, false,  -66.035114, 18.231229
  union select 'CIDRA',         'Cidra',                          3, false,  -66.035114, 18.231229
  union select 'GUAVATE',       'Bo. Guavate',                    3, false,  -66.035114, 18.231229
  union select 'GURABO',        'Gurabo',                         3, false,  -66.035114, 18.231229
  union select 'HOSPITAL',      'Hospital Regional',              3, false,  -66.035114, 18.231229
  union select 'JUNCOS',        'Juncos',                         3, false,  -66.035114, 18.231229
  union select 'MARIOLGA_HIMA', 'HIMA, Urb. Mariolga',            3, false,  -66.035114, 18.231229
  union select 'NAVARRO',       'Bo. Navarro',                    3, false,  -66.035114, 18.231229
  union select 'PLAZA_CENTRO',  'Plaza Centro',                   3, false,  -66.035114, 18.231229
  union select 'SAN_ANTONIO',   'Bo. San Antonio',                3, false,  -66.035114, 18.231229
  union select 'SAN_LORENZO',   'San Lorenzo',                    3, false,  -66.035114, 18.231229
  union select 'SAN_SALVADOR',  'Bo. San Salvador',               3, false,  -66.035114, 18.231229
  union select 'TOMAS_DE_CASTRO_1','Bo. Tomás de Castro 1',       3, false,  -66.035114, 18.231229
  union select 'TOMAS_DE_CASTRO_2','Bo. Tomás de Castro 2',       3, false,  -66.035114, 18.231229
  union select 'TURABO_ARRIBA',  'Urb. Turabo Arriba',            3, false,  -66.035114, 18.231229
  union select 'TURABO_GARDENS', 'Urb. Turabo Gardens',           3, false,  -66.035114, 18.231229
  union select 'VALLE_TOLIMA',  'Urb. Valle Tolima',              3, false,  -66.035114, 18.231229
  union select 'VILLA_BLANCA',  'Urb. Villa Blanca',              3, false,  -66.035114, 18.231229
  union select 'VILLA_ESPERANZA', 'Urb. Villa Esperanza',         3, false,  -66.035114, 18.231229
  union select 'VILLAS_DE_CASTRO', 'Urb. Villas de Castro',       3, false,  -66.035114, 18.231229
  union select 'VILLAS_DEL_REY', 'Urb. Villas del Rey',       3, false,  -66.035114, 18.231229
)
insert into ref.stop_area (id, "desc", lng, lat, geom, sort_order, sched_origin)
  select id, "desc", lng, lat, ST_SetSRID(ST_Point(lng, lat), 4326), sort_order, sched_origin from stop_area_tmp;
