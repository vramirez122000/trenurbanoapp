delete from ref.subroute_new where route = 'C43' and direction = 'CAROLINA'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-65.992118 18.421871,-65.992459 18.421778,-65.992519 18.421697,-65.992538 18.421602,-65.992505 18.421407,-65.992465 18.421261,-65.99239 18.421206,-65.992189 18.421217,-65.991891 18.421289,-65.989976 18.421744,-65.988769 18.422021,-65.98868 18.422092,-65.988666 18.422163,-65.988841 18.422795,-65.989055 18.423864,-65.989311 18.42537,-65.989469 18.425999,-65.989654 18.426818,-65.989836 18.427225,-65.989891 18.427354,-65.989883 18.427433,-65.989824 18.427491,-65.989514 18.427671,-65.989421 18.427684,-65.989328 18.427646,-65.989174 18.42732,-65.989068 18.427026,-65.988961 18.42602,-65.988882 18.42543,-65.988855 18.425392,-65.988796 18.425379,-65.988659 18.425402,-65.987969 18.425544,-65.986778 18.425795,-65.984731 18.426263,-65.983046 18.426643,-65.982417 18.426769,-65.980403 18.426967,-65.978655 18.427169,-65.978578 18.427177,-65.977765 18.427271,-65.97632 18.427421,-65.976019 18.427395,-65.975143 18.42719,-65.970826 18.426042,-65.970494 18.425943,-65.970337 18.425925,-65.970279 18.425994,-65.970233 18.426155,-65.969865 18.427474,-65.969738 18.427943,-65.969714 18.42809,-65.969803 18.428951,-65.969857 18.429003,-65.970379 18.429135,-65.970953 18.429307,-65.970976 18.429351,-65.970961 18.429429,-65.970837 18.429825,-65.970841 18.429939,-65.970965 18.430388,-65.971146 18.430491,-65.972558 18.430949,-65.972694 18.430994,-65.97276 18.430984,-65.972814 18.430921,-65.973131 18.429873,-65.973151 18.429829,-65.973197 18.429811,-65.975147 18.430333,-65.975197 18.430373,-65.975182 18.430447,-65.974838 18.431615,-65.974822 18.431696,-65.974849 18.431747,-65.974903 18.431795,-65.977227 18.432404,-65.97739 18.432396,-65.977584 18.43233,-65.979312 18.431054,-65.979393 18.43097,-65.979404 18.430907,-65.97932 18.430812,-65.9792 18.430633,-65.979119 18.430465,-65.979034 18.430208,-65.978837 18.428685,-65.978671 18.427365,-65.97866 18.427233,-65.978655 18.427169,-65.978078 18.420297,-65.977999 18.419191,-65.97803 18.419151,-65.978144 18.419122,-65.978577 18.419084,-65.979023 18.419031,-65.979367 18.418957,-65.97993 18.418744,-65.981005 18.418284,-65.982462 18.417715,-65.983012 18.417503,-65.983261 18.417356,-65.98347 18.417237,-65.983812 18.417131,-65.984109 18.417069,-65.984206 18.417042,-65.984294 18.417026,-65.984361 18.41695,-65.984361 18.416882,-65.984332 18.416803,-65.983773 18.415853,-65.98312 18.414777,-65.982548 18.413906,-65.982443 18.413552,-65.982299 18.412971,-65.982141 18.411977,-65.982079 18.41157,-65.982045 18.411073,-65.982046 18.410706,-65.982111 18.409857,-65.982163 18.408911,-65.982166 18.40857,-65.982109 18.408212,-65.981885 18.407018,-65.981892 18.406939,-65.981924 18.40688,-65.981972 18.406854,-65.982079 18.406841,-65.982914 18.406876,-65.985054 18.406926,-65.988605 18.407017,-65.988933 18.40699,-65.989215 18.406915,-65.989441 18.406802,-65.989554 18.40677,-65.989662 18.406818,-65.991013 18.40883,-65.991183 18.409195,-65.991347 18.409731,-65.991436 18.410094,-65.993098 18.417014,-65.993973 18.420697,-65.99393 18.420777,-65.993758 18.420839,-65.992384 18.421169,-65.991951 18.42129,-65.99149 18.421402,-65.991445 18.421472,-65.991475 18.421579,-65.991575 18.421937,-65.991667 18.421975,-65.992073 18.421881)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'C43','CAROLINA', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
