delete from ref.subroute_new where route = 'T8' and direction = 'PINERO'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.101171 18.390863,-66.102418 18.390922,-66.102508 18.390954,-66.102556 18.391024,-66.102556 18.391283,-66.102604 18.391401,-66.102661 18.39142,-66.104756 18.391511,-66.104883 18.391587,-66.104975 18.391674,-66.105595 18.393187,-66.105579 18.393316,-66.105459 18.393396,-66.102617 18.394314,-66.102101 18.394515,-66.101354 18.395016,-66.099919 18.396601,-66.098723 18.39791,-66.097776 18.398737,-66.097006 18.39929,-66.096894 18.399375,-66.09646 18.399667,-66.095973 18.399957,-66.095417 18.400238,-66.094916 18.400505,-66.094405 18.400773,-66.093403 18.401204,-66.092265 18.401643,-66.09117 18.402011,-66.089961 18.402407,-66.088177 18.402954,-66.086082 18.403609,-66.083801 18.404378,-66.081558 18.405092,-66.078256 18.406133,-66.076577 18.406621,-66.075642 18.406839,-66.073932 18.407056,-66.071129 18.407193,-66.066418 18.407351,-66.059199 18.407586,-66.056527 18.407699,-66.047968 18.40801,-66.045989 18.40801,-66.045073 18.407871,-66.044417 18.407688,-66.044135 18.407409,-66.043988 18.407034,-66.043965 18.406487,-66.043528 18.402153,-66.043556 18.40203,-66.043715 18.401965,-66.045943 18.401666,-66.048146 18.401413,-66.048995 18.401309,-66.050596 18.401135,-66.050818 18.401141,-66.051126 18.401162,-66.051461 18.40123,-66.051854 18.401357,-66.051869 18.401365,-66.052004 18.401449,-66.052077 18.401573,-66.052108 18.401985,-66.051971 18.403737,-66.051945 18.404541,-66.051943 18.405693,-66.051965 18.406085,-66.052031 18.406396,-66.052253 18.406963,-66.052747 18.408178,-66.052828 18.408258,-66.052978 18.408302,-66.053179 18.408299,-66.053295 18.408274,-66.05386 18.408225,-66.053903 18.408225,-66.054404 18.408188,-66.054954 18.408154,-66.055098 18.40823,-66.055146 18.408579,-66.05509 18.408662,-66.055014 18.408753,-66.055034 18.408909,-66.055262 18.410464)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T8','PINERO', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_new where route = 'T8' and direction = 'MARTINEZ_NADAL'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.055425 18.410448,-66.055074 18.408261,-66.055141 18.408125,-66.056356 18.408087,-66.056458 18.408157,-66.056495 18.408285,-66.056574 18.41045,-66.056582 18.410659,-66.05652 18.410756,-66.05431 18.411861,-66.054231 18.411858,-66.054183 18.411783,-66.053623 18.410275,-66.052888 18.408436,-66.052809 18.408184,-66.052311 18.406939,-66.052091 18.406376,-66.052003 18.406089,-66.051983 18.405684,-66.051992 18.404541,-66.052051 18.403734,-66.052184 18.401714,-66.05219 18.401521,-66.052105 18.401398,-66.051947 18.401331,-66.0515 18.401181,-66.051144 18.401108,-66.050827 18.401081,-66.050576 18.401084,-66.048987 18.40125,-66.045952 18.401594,-66.043538 18.401921,-66.043357 18.402168,-66.043747 18.406213,-66.043826 18.408385,-66.04386 18.408535,-66.044013 18.408627,-66.044222 18.408637,-66.044657 18.408568,-66.045743 18.408364,-66.047054 18.408219,-66.051475 18.40801,-66.054924 18.407881,-66.058676 18.407748,-66.059105 18.407749,-66.06068 18.407696,-66.065048 18.407581,-66.069102 18.407441,-66.072589 18.407336,-66.074184 18.407232,-66.075151 18.407093,-66.076615 18.406791,-66.077912 18.406402,-66.081429 18.405286,-66.081468 18.405286,-66.085014 18.404164,-66.088005 18.403213,-66.091079 18.402225,-66.092937 18.401579,-66.0945 18.400915,-66.096029 18.400136,-66.097149 18.399395,-66.09792 18.398816,-66.098783 18.398065,-66.099981 18.396802,-66.101033 18.3956,-66.101479 18.395133,-66.102169 18.394661,-66.102661 18.394463,-66.105516 18.393513,-66.105656 18.393418,-66.1057 18.393248,-66.105616 18.392906,-66.105024 18.391635,-66.104904 18.39151,-66.104736 18.391446,-66.102714 18.391347,-66.102362 18.391366,-66.102038 18.391366,-66.101107 18.391321)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T8','MARTINEZ_NADAL', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
