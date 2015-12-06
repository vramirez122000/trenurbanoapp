delete from ref.subroute_new where route = 'BC' and direction = 'TERM_CAGUAS';with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.076898 18.21164,-66.076848 18.211688,-66.07676 18.211688,-66.076616 18.211567,-66.076067 18.211312,-66.075671 18.211177,-66.075458 18.211177,-66.075245 18.211318,-66.07512 18.211508,-66.075011 18.211861,-66.074903 18.212224,-66.074837 18.212444,-66.074842 18.212574,-66.074895 18.212909,-66.074859 18.213015,-66.074573 18.21322,-66.074389 18.213357,-66.073817 18.213989,-66.073215 18.215071,-66.073083 18.215329,-66.072941 18.215496,-66.072542 18.215879,-66.071863 18.216494,-66.071637 18.216874,-66.071566 18.216968,-66.071467 18.21705,-66.070739 18.217557,-66.070577 18.217632,-66.070458 18.217659,-66.069884 18.217654,-66.069733 18.217638,-66.069311 18.217594,-66.069095 18.217631,-66.068727 18.217763,-66.068066 18.217997,-66.067848 18.218084,-66.06768 18.218139,-66.067527 18.218147,-66.067247 18.218178,-66.066815 18.218353,-66.066155 18.21866,-66.065695 18.21895,-66.065171 18.219347,-66.064879 18.21966,-66.064533 18.22003,-66.064361 18.220157,-66.064267 18.220211,-66.063547 18.220548,-66.061877 18.221367,-66.061206 18.221665,-66.061076 18.221714,-66.060762 18.221885,-66.060559 18.222086,-66.060164 18.222467,-66.058723 18.223984,-66.058512 18.224157,-66.05841 18.224207,-66.056331 18.226301,-66.054224 18.228401,-66.054175 18.228451,-66.054129 18.228468,-66.054039 18.228457,-66.053961 18.228424,-66.053792 18.228351,-66.053603 18.22823,-66.053479 18.228228,-66.053416 18.228312,-66.053258 18.228761,-66.053103 18.229299,-66.053011 18.229587,-66.052932 18.229746,-66.052772 18.229918,-66.052592 18.230043,-66.052382 18.230177,-66.052236 18.230365,-66.052154 18.230562,-66.051846 18.23141,-66.051711 18.231757,-66.051617 18.231943,-66.05152 18.232051,-66.051352 18.232185,-66.050675 18.232481,-66.050464 18.232596,-66.050366 18.232701,-66.050302 18.232775,-66.050169 18.232912,-66.050035 18.232985,-66.049935 18.233019,-66.049793 18.233031,-66.04968 18.233038,-66.049565 18.233046,-66.049318 18.23307,-66.048522 18.233118,-66.047774 18.233141,-66.047328 18.233138,-66.046629 18.233166,-66.046145 18.233212,-66.0454 18.233427,-66.044641 18.233622,-66.043962 18.233805,-66.04301 18.234037,-66.042606 18.234145,-66.042209 18.234287,-66.041814 18.234437,-66.041536 18.234462,-66.041232 18.234451,-66.040678 18.234423,-66.040417 18.234407,-66.040205 18.234414,-66.039659 18.234457,-66.039367 18.23447,-66.038831 18.23448,-66.038367 18.23448,-66.038211 18.234487,-66.037984 18.234605,-66.037936 18.234745,-66.03782 18.23596,-66.037644 18.236572,-66.037414 18.237129,-66.037187 18.237517,-66.036979 18.23782,-66.036932 18.237832,-66.036829 18.237787,-66.036752 18.237728,-66.036474 18.237501,-66.036229 18.237244,-66.036144 18.237119,-66.036105 18.236972,-66.036248 18.236288,-66.036162 18.23623,-66.035366 18.235949,-66.03513 18.235863,-66.03414 18.235501,-66.033188 18.235111,-66.033078 18.23503,-66.032524 18.234864)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'BC','TERM_CAGUAS', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_new where route = 'BC' and direction = 'BO_CANABONCITO';with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.032467 18.234846,-66.03208 18.234732,-66.031981 18.234729,-66.031774 18.235276,-66.03184 18.235427,-66.03261 18.235694,-66.032753 18.235741,-66.032832 18.23576,-66.032931 18.235726,-66.034555 18.236309,-66.035034 18.236481,-66.035215 18.236517,-66.036063 18.236796,-66.036094 18.236823,-66.036088 18.236878,-66.036079 18.236974,-66.036119 18.237132,-66.036206 18.237263,-66.036452 18.237519,-66.036866 18.237856,-66.037001 18.237903,-66.03715 18.23787,-66.037346 18.23756,-66.037582 18.237138,-66.037817 18.236542,-66.037979 18.235939,-66.038118 18.234834,-66.038162 18.234706,-66.038243 18.234626,-66.038366 18.234579,-66.038535 18.234575,-66.038759 18.23455,-66.039235 18.234547,-66.039606 18.234538,-66.039814 18.234517,-66.040154 18.234515,-66.040443 18.234543,-66.040882 18.234577,-66.041497 18.234618,-66.041795 18.234569,-66.042222 18.234413,-66.042561 18.234292,-66.042956 18.234181,-66.043937 18.233928,-66.044669 18.233736,-66.045406 18.233541,-66.046178 18.23335,-66.046448 18.233312,-66.047325 18.233266,-66.049033 18.233223,-66.049533 18.233208,-66.049677 18.233182,-66.049866 18.233139,-66.050107 18.233146,-66.050249 18.23312,-66.050311 18.233025,-66.050345 18.232848,-66.050407 18.232717,-66.050494 18.232632,-66.050697 18.232521,-66.051377 18.232223,-66.051554 18.232083,-66.051657 18.231968,-66.051755 18.231775,-66.051891 18.231425,-66.052198 18.230578,-66.052278 18.230387,-66.052415 18.23021,-66.052619 18.230079,-66.052804 18.229951,-66.052972 18.229771,-66.053055 18.229603,-66.053149 18.229312,-66.053303 18.228774,-66.053458 18.228333,-66.053503 18.228274,-66.053589 18.228275,-66.053769 18.228391,-66.053942 18.228465,-66.054026 18.228501,-66.054134 18.228514,-66.054202 18.228489,-66.054258 18.228432,-66.056365 18.226332,-66.058439 18.224243,-66.058539 18.224195,-66.058756 18.224017,-66.060199 18.222498,-66.060593 18.222117,-66.060792 18.221921,-66.061097 18.221754,-66.061224 18.221707,-66.061898 18.221407,-66.063569 18.220588,-66.06429 18.22025,-66.064387 18.220195,-66.064565 18.220063,-66.064914 18.21969,-66.065204 18.21938,-66.065723 18.218986,-66.066179 18.218699,-66.066834 18.218393,-66.067259 18.218222,-66.067531 18.218192,-66.067689 18.218183,-66.067865 18.218126,-66.068084 18.218039,-66.068744 18.217805,-66.069108 18.217674,-66.069313 18.21764,-66.069728 18.217682,-66.069881 18.217699,-66.070464 18.217704,-66.070593 18.217674,-66.070764 18.217596,-66.071496 18.217085,-66.071601 18.216998,-66.071677 18.216898,-66.071901 18.216522,-66.072575 18.215911,-66.072977 18.215526,-66.073123 18.215353,-66.073257 18.215091,-66.073856 18.214015,-66.074422 18.21339,-66.074602 18.213255,-66.074899 18.213042,-66.074943 18.212913,-66.07489 18.21257,-66.074885 18.21245,-66.074948 18.212237,-66.075056 18.211874,-66.075163 18.211527,-66.07528 18.21135,-66.075473 18.211222,-66.075663 18.211222,-66.076049 18.211353,-66.07659 18.211605,-66.076855 18.211808,-66.0769 18.211849)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'BC','BO_CANABONCITO', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_stop where route = 'BC' and direction = 'BO_CANABONCITO'; insert into ref.subroute_stop (stop, stop_order, route, direction) values (8000, 0, 'BC', 'BO_CANABONCITO'),(8003, 1, 'BC', 'BO_CANABONCITO');
delete from ref.subroute_stop where route = 'BC' and direction = 'TERM_CAGUAS'; insert into ref.subroute_stop (stop, stop_order, route, direction) values (8003, 0, 'BC', 'TERM_CAGUAS'),(8000, 1, 'BC', 'TERM_CAGUAS');
