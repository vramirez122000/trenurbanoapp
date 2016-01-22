with stop_area_tmp(id, "desc", sort_order, lng, lat) as (
  select 'SAGRADO_CORAZON',    'Sagrado Corazón',         1,  -66.05968,	18.43764 union
  select 'HATO_REY',           'Hato Rey',                2,  -66.05998,	18.43028 union
  select 'ROOSEVELT',          'Roosevelt',               3,  -66.05878,	18.42453 union
  select 'DOMENECH',           'Dómenech',                4,  -66.05634,	18.41635 union
  select 'PINERO',             'Piñero',                  5,  -66.05522,	18.41169 union
  select 'UNIVERSIDAD',        'Universidad',             6,  -66.05193,	18.40602 union
  select 'RIO_PIEDRAS',        'Río Piedras',             7,  -66.05241,	18.40025 union
  select 'CUPEY',              'Cupey',                   8,  -66.06321,	18.39212 union
  select 'CENTRO_MEDICO',      'Centro Médico',           9,  -66.07573,	18.39317 union
  select 'SAN_FRANCISCO',      'San Francisco',           10, -66.08404,	18.39231 union
  select 'LAS_LOMAS',          'Las Lomas',               11, -66.09518,	18.39240 union
  select 'MARTINEZ_NADAL',     'Martínez Nadal',          12, -66.10291,	18.39281 union
  select 'TORRIMAR',           'Torrimar',                13, -66.12018,	18.39360 union
  select 'JARDINES',           'Jardines',                14, -66.12892,	18.39573 union
  select 'DEPORTIVO',          'Deportivo',               15, -66.15125,	18.39592 union
  select 'BAYAMON',            'Bayamón',                 16, -66.15381,	18.40095 union
  select 'TOA_BAJA',           'Toa Baja',                17, -66.24710,	18.23033 union
  select 'SAN_JUAN',           'Viejo San Juan',          18, -66.11069,	18.46501 union
  select 'CATANO',             'Cataño',                  19, -66.11662,	18.44314 union
  select 'AEROPUERTO',         'Aeropuerto',              20, -66.00427,	18.43801 union
  select 'PLAZA_LAS_AMERICAS', 'Plaza Las Américas',      21, -66.07427,	18.41838 union
  select 'PUNTA_LAS_MARIAS',   'Punta Las Marías',        22, -66.03676,	18.44713 union
  select 'CONVENCIONES',       'Centro de Convenciones',	23, -66.09015,	18.45413 union
  select 'ITURREGUI',          'Terminal Iturregui',      24, -65.99220,  18.42170
)
insert into ref.stop_area (id, "desc", lng, lat, geom, sort_order)
  select id, "desc", lng, lat, ST_SetSRID(ST_Point(lng, lat), 4326), sort_order from stop_area_tmp;

