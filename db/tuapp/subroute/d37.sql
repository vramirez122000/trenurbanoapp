delete from ref.subroute_new where route = 'D37' and direction = 'TOA_BAJA'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.115857 18.443414,-66.116296 18.44312,-66.116834 18.442772,-66.117109 18.442519,-66.117287 18.442353,-66.117439 18.442263,-66.117627 18.442181,-66.11792 18.442121,-66.11814 18.442078,-66.118287 18.442028,-66.118663 18.441895,-66.118991 18.441802,-66.11959 18.441632,-66.119947 18.441543,-66.121029 18.441409,-66.121776 18.441309,-66.121951 18.441259,-66.122052 18.441199,-66.122167 18.441132,-66.122286 18.441095,-66.122772 18.441119,-66.123184 18.441165,-66.123241 18.441151,-66.123426 18.441197,-66.124825 18.441403,-66.125181 18.441489,-66.126144 18.44181,-66.126697 18.442029,-66.127546 18.44249,-66.12778 18.442564,-66.128048 18.442616,-66.128262 18.442644,-66.128614 18.442685,-66.129182 18.44276,-66.12958 18.442814,-66.130096 18.442919,-66.130281 18.442976,-66.131299 18.443463,-66.131501 18.443596,-66.131718 18.443805,-66.131902 18.443962,-66.132146 18.444163,-66.132311 18.444257,-66.132451 18.444322,-66.132692 18.44441,-66.132808 18.444492,-66.134064 18.445759,-66.135492 18.447224,-66.135584 18.447331,-66.136377 18.448359,-66.136641 18.448804,-66.137643 18.45058,-66.138551 18.452328,-66.139141 18.453529,-66.13926 18.453675,-66.139485 18.453789,-66.139784 18.453848,-66.139944 18.453848,-66.140226 18.453814,-66.140496 18.453727,-66.140785 18.453591,-66.141049 18.453395,-66.141181 18.4533,-66.141667 18.452778,-66.141934 18.452453,-66.1421 18.452113,-66.142189 18.451775,-66.142219 18.451594,-66.142223 18.451239,-66.142214 18.45084,-66.142227 18.450627,-66.142265 18.450385,-66.142397 18.450077,-66.142545 18.44979,-66.142889 18.449472,-66.143485 18.448928,-66.144464 18.448062,-66.145694 18.44702,-66.145775 18.446984,-66.14583 18.447,-66.14639 18.447583,-66.146527 18.447682,-66.146709 18.447775,-66.146832 18.447811,-66.147785 18.447964,-66.148512 18.448061,-66.148665 18.448117,-66.149346 18.448714,-66.149954 18.449272,-66.149971 18.449348,-66.149959 18.449429,-66.149853 18.449538,-66.148705 18.450664,-66.148497 18.450869,-66.148293 18.451183,-66.148009 18.451686,-66.146881 18.453741,-66.146885 18.453931,-66.147038 18.45398,-66.147712 18.454,-66.148542 18.454055,-66.150307 18.454107,-66.150586 18.454107,-66.150962 18.45408,-66.150986 18.45408,-66.151682 18.454005,-66.152341 18.453907,-66.153333 18.453754,-66.15411 18.453615,-66.154994 18.453446,-66.155319 18.453355,-66.155624 18.453233,-66.156127 18.45298,-66.156849 18.452596,-66.157225 18.452417,-66.15764 18.452248,-66.158107 18.452084,-66.159299 18.451782,-66.160395 18.451541,-66.161403 18.451336,-66.162699 18.451039,-66.163237 18.450898,-66.163885 18.450759,-66.164327 18.450734,-66.165019 18.450704,-66.16522 18.450671,-66.165835 18.450655,-66.166231 18.450643,-66.166312 18.450597,-66.166378 18.450522,-66.166394 18.450376,-66.166388 18.449467,-66.166354 18.448975,-66.166236 18.448321,-66.166147 18.44781,-66.166078 18.447331,-66.166068 18.445873,-66.166057 18.444498,-66.166034 18.443264,-66.166008 18.441687,-66.165978 18.439619,-66.165964 18.438382,-66.165995 18.438066,-66.166009 18.437814,-66.166013 18.437676,-66.166055 18.43759,-66.166161 18.437519,-66.166688 18.437523,-66.166932 18.4375,-66.168707 18.437524,-66.172644 18.437612,-66.180245 18.437748,-66.185938 18.437871,-66.187306 18.43792,-66.187674 18.438064,-66.188445 18.43844,-66.188787 18.438483,-66.190842 18.438464,-66.195255 18.438456,-66.19556 18.438475,-66.195947 18.438563,-66.195984 18.43862,-66.19591 18.439126)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'D37','TOA_BAJA', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_new where route = 'D37' and direction = 'CATANO'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.196012 18.439152,-66.19604 18.439013,-66.196113 18.438584,-66.196006 18.43846,-66.195548 18.438348,-66.194915 18.438337,-66.194146 18.438337,-66.18872 18.438361,-66.1884 18.438301,-66.187769 18.438012,-66.187393 18.437815,-66.186873 18.437755,-66.185842 18.437686,-66.181301 18.437588,-66.179702 18.43755,-66.175004 18.437471,-66.173201 18.437457,-66.172613 18.437441,-66.168265 18.43733,-66.167328 18.437305,-66.166685 18.437313,-66.166413 18.437349,-66.166066 18.437337,-66.165904 18.43736,-66.165791 18.437503,-66.165762 18.437729,-66.165788 18.438463,-66.165848 18.444048,-66.165853 18.446306,-66.165881 18.447302,-66.166085 18.448592,-66.166187 18.44924,-66.166196 18.449943,-66.166199 18.450393,-66.16616 18.45047,-66.166091 18.45053,-66.166004 18.450572,-66.164704 18.450585,-66.16415 18.450608,-66.163751 18.450679,-66.163085 18.450818,-66.162064 18.45107,-66.160021 18.451502,-66.15826 18.451916,-66.157457 18.452197,-66.156725 18.452521,-66.155929 18.452952,-66.155435 18.453189,-66.154942 18.453342,-66.153587 18.453566,-66.151198 18.453941,-66.15042 18.453967,-66.149027 18.45393,-66.1471 18.453892,-66.147028 18.453869,-66.147 18.453812,-66.146993 18.45374,-66.147099 18.453534,-66.148021 18.451815,-66.148485 18.450984,-66.148989 18.450456,-66.149998 18.449451,-66.150042 18.449368,-66.150034 18.449285,-66.149915 18.449146,-66.148802 18.448131,-66.148686 18.448055,-66.148302 18.447953,-66.1469 18.447746,-66.146656 18.447677,-66.146521 18.447586,-66.146191 18.447292,-66.145916 18.447007,-66.145828 18.446932,-66.145772 18.446925,-66.145697 18.446943,-66.14545 18.447113,-66.145204 18.447325,-66.143358 18.448906,-66.142858 18.449262,-66.142477 18.449634,-66.142245 18.449983,-66.14215 18.450202,-66.14208 18.450489,-66.142062 18.451049,-66.142049 18.451505,-66.141982 18.451908,-66.141914 18.452127,-66.141775 18.452459,-66.141587 18.452687,-66.141083 18.453203,-66.140983 18.453294,-66.140655 18.453522,-66.14043 18.453623,-66.140027 18.453712,-66.139849 18.453723,-66.139651 18.453714,-66.139466 18.453683,-66.139294 18.453597,-66.139184 18.453499,-66.138996 18.453185,-66.138271 18.45169,-66.137559 18.450355,-66.137128 18.449581,-66.136431 18.448385,-66.135981 18.447784,-66.13552 18.447216,-66.1338 18.445447,-66.132812 18.444455,-66.13269 18.444372,-66.132377 18.444261,-66.132216 18.444173,-66.13183 18.443873,-66.131762 18.443808,-66.131601 18.443622,-66.131493 18.443526,-66.130305 18.442953,-66.129643 18.44279,-66.128292 18.442615,-66.127945 18.442567,-66.127689 18.442497,-66.127428 18.442385,-66.126873 18.442069,-66.126694 18.441975,-66.126133 18.441753,-66.124926 18.441385,-66.123985 18.441238,-66.123349 18.441148,-66.123312 18.441124,-66.123292 18.441085,-66.123289 18.441056,-66.123291 18.441051,-66.123251 18.440619,-66.123232 18.440442,-66.12322 18.440275,-66.123186 18.440031,-66.123175 18.439953,-66.123163 18.439887,-66.123111 18.439856,-66.12307 18.439859,-66.122742 18.439969,-66.120782 18.440675,-66.119249 18.441203,-66.119116 18.441252,-66.118654 18.441426,-66.117808 18.441727,-66.117773 18.441741,-66.117141 18.441981,-66.116983 18.442076,-66.116952 18.442094,-66.11522 18.443518,-66.115151 18.443592,-66.115145 18.443652,-66.115193 18.443694,-66.115256 18.443723,-66.115301 18.443723,-66.115337 18.443723,-66.115445 18.443675,-66.115857 18.443414)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'D37','CATANO', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
