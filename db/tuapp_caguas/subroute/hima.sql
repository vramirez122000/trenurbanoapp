delete from ref.subroute where route = 'HI' and direction = 'TERM_CAGUAS';with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.030963 18.21875,-66.031127 18.218717,-66.031346 18.218695,-66.03147 18.21881,-66.031597 18.218878,-66.031917 18.218829,-66.032193 18.218758,-66.03262 18.218277,-66.032677 18.218233,-66.032801 18.218312,-66.032913 18.218367,-66.033017 18.21835,-66.033121 18.218197,-66.033673 18.217449,-66.034284 18.216697,-66.034755 18.21626,-66.034832 18.216252,-66.035604 18.216737,-66.036344 18.217194,-66.03636 18.217254,-66.036276 18.217409,-66.036234 18.217558,-66.036242 18.217722,-66.03628 18.217875,-66.036602 18.218845,-66.036647 18.218996,-66.036735 18.219093,-66.036841 18.21913,-66.037093 18.219153,-66.037722 18.2192,-66.037794 18.219219,-66.037822 18.219273,-66.037718 18.220227,-66.037735 18.220275,-66.037804 18.220306,-66.039244 18.22044,-66.03969 18.220444,-66.04059 18.220505,-66.040631 18.220529,-66.040625 18.220618,-66.039634 18.222561,-66.037328 18.226743,-66.037129 18.227145,-66.036432 18.228407,-66.036162 18.228931,-66.035612 18.230205,-66.035167 18.231265,-66.035111 18.231277,-66.034879 18.231252,-66.034417 18.231325,-66.03431 18.231827,-66.034118 18.232441,-66.033969 18.232988,-66.03373 18.233717,-66.033503 18.234387,-66.033422 18.234439,-66.033222 18.234863,-66.033119 18.235035,-66.032763 18.234988,-66.032448 18.235016)'), 32161) geom) insert into ref.subroute(route, direction, geom) select 'HI','TERM_CAGUAS', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute where route = 'HI' and direction = 'HIMA';with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.032334 18.235034,-66.031877 18.235038,-66.031749 18.235322,-66.030735 18.235051,-66.031324 18.233574,-66.031505 18.233171,-66.031832 18.232466,-66.031912 18.232384,-66.032211 18.232007,-66.03301 18.23034,-66.034564 18.228074,-66.034857 18.22758,-66.034906 18.227411,-66.034944 18.227254,-66.035028 18.227115,-66.035145 18.227022,-66.035421 18.226531,-66.035528 18.226415,-66.03572 18.226347,-66.035952 18.226364,-66.036416 18.226563,-66.036799 18.226724,-66.037118 18.226879,-66.037335 18.226863,-66.039676 18.222582,-66.040516 18.221037,-66.040596 18.220826,-66.040703 18.220624,-66.040671 18.220532,-66.040592 18.220483,-66.039691 18.220421,-66.039245 18.220417,-66.03781 18.220284,-66.037754 18.220259,-66.037742 18.220224,-66.037846 18.219269,-66.03781 18.2192,-66.037727 18.219177,-66.037095 18.219131,-66.036846 18.219108,-66.036749 18.219074,-66.036668 18.218985,-66.036625 18.218839,-66.036303 18.217869,-66.036265 18.217719,-66.036257 18.21756,-66.036298 18.217417,-66.036385 18.217257,-66.036364 18.21718,-66.035617 18.216719,-66.034838 18.216229,-66.034744 18.216238,-66.034267 18.216682,-66.033654 18.217436,-66.033101 18.218185,-66.033003 18.21833,-66.032917 18.218343,-66.032813 18.218293,-66.032676 18.218205,-66.032603 18.21826,-66.03218 18.218738,-66.031912 18.218807,-66.031601 18.218855,-66.031485 18.218792,-66.031355 18.218672,-66.031124 18.218695,-66.030958 18.218728)'), 32161) geom) insert into ref.subroute(route, direction, geom) select 'HI','HIMA', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_stop where route = 'HI' and direction = 'HIMA'; insert into ref.subroute_stop (stop, stop_order, route, direction) values (8000, 0, 'HI', 'HIMA'),(8022, 1, 'HI', 'HIMA');
delete from ref.subroute_stop where route = 'HI' and direction = 'TERM_CAGUAS'; insert into ref.subroute_stop (stop, stop_order, route, direction) values (8022, 0, 'HI', 'TERM_CAGUAS'),(8000, 1, 'HI', 'TERM_CAGUAS');