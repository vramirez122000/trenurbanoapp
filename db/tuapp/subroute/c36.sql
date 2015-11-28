delete from ref.subroute_new where route = 'C36' and direction = 'SAGRADO_CORAZON'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.034162 18.446566,-66.034163 18.446517,-66.034197 18.446438,-66.034293 18.44636,-66.034419 18.446287,-66.034568 18.44619,-66.034638 18.446159,-66.034727 18.446139,-66.034885 18.446143,-66.035054 18.446167,-66.035451 18.446188,-66.035806 18.446206,-66.036464 18.446289,-66.037157 18.446361,-66.037823 18.446417,-66.038092 18.446466,-66.038175 18.446514,-66.038239 18.44659,-66.038249 18.446669,-66.038197 18.447138,-66.038135 18.447863,-66.038103 18.448005,-66.038036 18.44817,-66.037946 18.448314,-66.037537 18.448887,-66.037508 18.44892,-66.037428 18.449031,-66.037406 18.449069,-66.037393 18.449091,-66.037389 18.44914,-66.037416 18.449175,-66.037861 18.44965,-66.038488 18.450604,-66.039191 18.451629,-66.039273 18.451687,-66.039392 18.451714,-66.039534 18.451725,-66.039685 18.451698,-66.039896 18.451631,-66.041207 18.451129,-66.041702 18.4508,-66.042888 18.450115,-66.044504 18.449253,-66.044671 18.449192,-66.044845 18.449181,-66.04521 18.449189,-66.047528 18.449446,-66.049251 18.44969,-66.051644 18.45,-66.052075 18.450063,-66.052402 18.450163,-66.052608 18.450251,-66.052667 18.45026,-66.052716 18.450257,-66.052745 18.450232,-66.05276 18.450196,-66.052777 18.450102,-66.052833 18.447031,-66.053979 18.442607,-66.054041 18.442564,-66.054221 18.442644,-66.056192 18.443842,-66.056375 18.443891,-66.056813 18.443928,-66.060709 18.444308,-66.063179 18.444228,-66.06362 18.44412,-66.063954 18.443884,-66.064349 18.443413,-66.064445 18.443281,-66.064477 18.443183,-66.064411 18.443053,-66.062638 18.441628,-66.062106 18.441019,-66.0599 18.438553,-66.059848 18.438476,-66.059733 18.438328,-66.059514 18.437876,-66.059456 18.437756,-66.059415 18.437566,-66.059292 18.436216,-66.059324 18.436102,-66.059299 18.435472,-66.059294 18.435381,-66.059373 18.435337,-66.059712 18.435333)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'C36','SAGRADO_CORAZON', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_new where route = 'C36' and direction = 'PUNTA_LAS_MARIAS'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.060456 18.435624,-66.060471 18.435698,-66.06044 18.435881,-66.060385 18.435998,-66.059412 18.436016,-66.059352 18.436115,-66.059346 18.436278,-66.059437 18.437508,-66.059399 18.437771,-66.059363 18.437822,-66.059241 18.437837,-66.058843 18.437883,-66.05824 18.437936,-66.057944 18.437968,-66.057854 18.437965,-66.057573 18.437937,-66.056429 18.437796,-66.054822 18.43758,-66.054022 18.437464,-66.053851 18.437419,-66.053735 18.437367,-66.052678 18.436883,-66.051401 18.436272,-66.050453 18.435791,-66.050225 18.435707,-66.049953 18.435624,-66.049699 18.435583,-66.049369 18.435517,-66.048563 18.4354,-66.047496 18.435227,-66.04659 18.435087,-66.046159 18.435028,-66.0461 18.43503,-66.046082 18.435045,-66.046077 18.43505,-66.046073 18.435052,-66.04604 18.435099,-66.046037 18.435101,-66.045927 18.435402,-66.045353 18.436895,-66.045132 18.437466,-66.045122 18.437525,-66.045121 18.437527,-66.045122 18.437528,-66.045132 18.437564,-66.045634 18.437983,-66.046582 18.438798,-66.046762 18.438924,-66.047064 18.439067,-66.048923 18.439919,-66.051659 18.441164,-66.052128 18.441394,-66.053199 18.442048,-66.053525 18.442247,-66.053543 18.44228,-66.05354 18.442354,-66.053483 18.442607,-66.053262 18.443447,-66.052873 18.444957,-66.052487 18.446537,-66.052441 18.44677,-66.052405 18.447189,-66.052388 18.448054,-66.052362 18.449401,-66.052364 18.449982,-66.052359 18.450033,-66.052347 18.45006,-66.052329 18.450077,-66.052299 18.450077,-66.052222 18.45006,-66.051528 18.449946,-66.050082 18.449766,-66.048579 18.449547,-66.046739 18.449327,-66.04503 18.449132,-66.044827 18.449132,-66.044645 18.449154,-66.044507 18.449194,-66.044364 18.449263,-66.043481 18.449731,-66.041956 18.450588,-66.041389 18.450969,-66.041196 18.451076,-66.039844 18.451594,-66.039545 18.451673,-66.039458 18.451669,-66.039309 18.451639,-66.039237 18.451599,-66.039143 18.451501,-66.039073 18.451391,-66.038131 18.449995,-66.037817 18.449526,-66.037613 18.449273,-66.03751 18.449158,-66.037406 18.449069,-66.03731 18.448986,-66.035217 18.447372,-66.034188 18.446616,-66.034162 18.446566)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'C36','PUNTA_LAS_MARIAS', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;