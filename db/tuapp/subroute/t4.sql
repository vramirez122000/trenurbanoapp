delete from ref.subroute_new where route = 'T4' and direction = 'CATANO'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.101123 18.390887,-66.102463 18.390941,-66.102559 18.391016,-66.10257 18.39136,-66.102651 18.39142,-66.104683 18.391488,-66.104815 18.391523,-66.104911 18.391596,-66.105007 18.391739,-66.105179 18.392156,-66.105483 18.392827,-66.105591 18.393231,-66.105577 18.393319,-66.105497 18.393388,-66.103055 18.394171,-66.102467 18.394343,-66.101997 18.394563,-66.101381 18.394981,-66.100007 18.396532,-66.099086 18.39753,-66.098289 18.398307,-66.097266 18.399134,-66.097229 18.399265,-66.099292 18.402157,-66.100091 18.403298,-66.101462 18.405387,-66.103093 18.407856,-66.103495 18.40859,-66.103679 18.409051,-66.104146 18.410489,-66.103929 18.411008,-66.1039 18.411205,-66.103929 18.411315,-66.103978 18.411369,-66.104453 18.411736,-66.105099 18.412399,-66.10543 18.41277,-66.105527 18.412932,-66.10555 18.41316,-66.105496 18.413569,-66.10543 18.414084,-66.105436 18.414282,-66.105473 18.414424,-66.105541 18.414559,-66.105689 18.414707,-66.105845 18.414811,-66.106053 18.414885,-66.106538 18.414968,-66.107756 18.41513,-66.108649 18.415271,-66.108876 18.415346,-66.109028 18.415436,-66.109188 18.415554,-66.109344 18.415735,-66.10946 18.415988,-66.1096 18.416738,-66.109685 18.417261,-66.109782 18.417697,-66.110037 18.418735,-66.110065 18.419115,-66.110059 18.420175,-66.110055 18.421183,-66.11011 18.421412,-66.110234 18.42178,-66.110582 18.422648,-66.111587 18.4252,-66.111829 18.42573,-66.112256 18.426456,-66.112679 18.427067,-66.113133 18.427685,-66.113391 18.428055,-66.113417 18.428139,-66.113409 18.428195,-66.113373 18.428243,-66.113301 18.428275,-66.113168 18.428316,-66.113061 18.428351,-66.11302 18.428411,-66.113025 18.428483,-66.113156 18.428821,-66.114189 18.431351,-66.115371 18.434263,-66.116236 18.436428,-66.117092 18.438498,-66.118165 18.441119,-66.118307 18.441462,-66.118324 18.441518,-66.118254 18.441579,-66.117418 18.441884,-66.117054 18.442024,-66.116175 18.442737,-66.115224 18.44354,-66.115161 18.443615,-66.115184 18.443696,-66.115283 18.443733,-66.115424 18.443693,-66.116836 18.442768,-66.117294 18.442352,-66.117522 18.442227,-66.117725 18.442158,-66.118131 18.442077,-66.11858 18.44191,-66.118693 18.441879,-66.119703 18.441605,-66.120068 18.44152,-66.120503 18.44147,-66.121192 18.441396,-66.121754 18.441324,-66.121932 18.441272,-66.122068 18.441179,-66.122229 18.441102,-66.122509 18.441101,-66.122772 18.441128,-66.123136 18.441157,-66.123243 18.44114,-66.123281 18.441071,-66.123251 18.440781,-66.123213 18.440301,-66.123154 18.439948,-66.123163 18.439875,-66.123239 18.439826,-66.124675 18.439338,-66.12526 18.439116,-66.125302 18.439051,-66.125302 18.438975,-66.125269 18.438922,-66.125174 18.438894,-66.125112 18.438908,-66.124899 18.438954)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T4','CATANO', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_new where route = 'T4' and direction = 'CAPETILLO'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.124899 18.438954,-66.124893 18.438956,-66.124721 18.439043,-66.124644 18.43911,-66.124641 18.4392,-66.124675 18.43928,-66.124782 18.439443,-66.125272 18.439841,-66.126048 18.440232,-66.126371 18.440328,-66.126445 18.440491,-66.126219 18.440886,-66.125778 18.441591,-66.125651 18.44161,-66.124851 18.4414,-66.123307 18.441159,-66.122304 18.441073,-66.12216 18.441097,-66.121908 18.441242,-66.12117 18.441341,-66.120062 18.441483,-66.119652 18.441561,-66.119087 18.441711,-66.118584 18.441867,-66.118538 18.441871,-66.118473 18.441782,-66.118348 18.441487,-66.118286 18.441339,-66.117707 18.439946,-66.117056 18.438341,-66.116136 18.436048,-66.115779 18.435165,-66.11492 18.433041,-66.11436 18.431662,-66.11372 18.430092,-66.113293 18.429035,-66.113118 18.428586,-66.113109 18.428488,-66.11319 18.428432,-66.113344 18.428379,-66.113501 18.42828,-66.113664 18.428142,-66.114012 18.427734,-66.114027 18.427634,-66.113963 18.427511,-66.113725 18.427359,-66.112829 18.426636,-66.112499 18.426319,-66.112219 18.425868,-66.111836 18.425183,-66.11128 18.423897,-66.11073 18.422646,-66.110457 18.421926,-66.11026 18.421416,-66.110202 18.421122,-66.110166 18.420567,-66.110171 18.419197,-66.110157 18.418902,-66.110042 18.418317,-66.10983 18.417557,-66.109752 18.417111,-66.109544 18.415999,-66.109411 18.415676,-66.109187 18.415432,-66.109148 18.415399,-66.109014 18.415309,-66.108883 18.415255,-66.108291 18.415128,-66.106087 18.414795,-66.105933 18.414737,-66.105791 18.414641,-66.105702 18.414554,-66.105622 18.414453,-66.105569 18.41434,-66.105544 18.41421,-66.105524 18.414068,-66.105577 18.41379,-66.105638 18.41336,-66.105663 18.413051,-66.105627 18.412922,-66.10555 18.412735,-66.10541 18.412525,-66.105258 18.412337,-66.105179 18.412171,-66.105212 18.412041,-66.10541 18.411785,-66.105807 18.411292,-66.105925 18.410947,-66.105883 18.410765,-66.105765 18.410591,-66.105622 18.410466,-66.105381 18.410415,-66.105135 18.410432,-66.104784 18.410476,-66.104375 18.410538,-66.104233 18.410501,-66.104125 18.410175,-66.103533 18.408559,-66.100115 18.403272,-66.097417 18.399452,-66.097353 18.39935,-66.097381 18.399239,-66.098091 18.398662,-66.098806 18.398024,-66.099719 18.397077,-66.100606 18.396098,-66.101493 18.395074,-66.102305 18.394593,-66.103144 18.394308,-66.105551 18.393485,-66.105683 18.393364,-66.105691 18.393117,-66.105047 18.391664,-66.104935 18.39152,-66.104771 18.39144,-66.102681 18.391353,-66.102437 18.391357,-66.102165 18.391384,-66.101138 18.391346)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T4','CAPETILLO', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
