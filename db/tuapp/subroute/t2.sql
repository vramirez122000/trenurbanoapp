delete from ref.subroute_new where route = 'T2' and direction = 'BAYAMON'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.060234 18.435299,-66.060396 18.435365,-66.060458 18.435483,-66.060451 18.435533,-66.060328 18.435958,-66.060293 18.435989,-66.059337 18.436007,-66.059315 18.43598,-66.059271 18.435539,-66.059251 18.434636,-66.059182 18.433888,-66.05913 18.433059,-66.059084 18.431928,-66.05895 18.431289,-66.058271 18.427909,-66.057419 18.423976,-66.057452 18.423942,-66.057568 18.423889,-66.05775 18.423807,-66.057846 18.423716,-66.057899 18.423675,-66.057981 18.423641,-66.058012 18.42363,-66.059669 18.423056,-66.061956 18.422505,-66.065107 18.421789,-66.067581 18.421311,-66.068137 18.421167,-66.068431 18.421083,-66.06974 18.420549,-66.072102 18.419441,-66.075628 18.417741,-66.079717 18.415792,-66.082645 18.414541,-66.083467 18.414301,-66.084736 18.414045,-66.085698 18.413929,-66.091881 18.413713,-66.093946 18.413643,-66.094575 18.413606,-66.095082 18.413565,-66.09574 18.413471,-66.096079 18.413397,-66.097457 18.412918,-66.101234 18.41145,-66.10249 18.411115,-66.102719 18.411085,-66.102964 18.411057,-66.103197 18.411076,-66.103429 18.411175,-66.103598 18.411297,-66.103684 18.411429,-66.103657 18.411752,-66.103618 18.412236,-66.103654 18.412396,-66.103727 18.412521,-66.103805 18.41259,-66.103907 18.412634,-66.104041 18.412653,-66.104165 18.412653,-66.104297 18.412602,-66.104416 18.412517,-66.104806 18.412066,-66.105685 18.411011,-66.106125 18.410577,-66.106647 18.410131,-66.10693 18.409837,-66.107216 18.409488,-66.107533 18.409042,-66.107778 18.408635,-66.108118 18.408043,-66.108426 18.407742,-66.108679 18.407611,-66.109087 18.407435,-66.109448 18.407312,-66.109731 18.407197,-66.110064 18.407038,-66.111408 18.406231,-66.114489 18.404425,-66.116329 18.403318,-66.118338 18.402101,-66.118607 18.401929,-66.118998 18.40163,-66.119927 18.400677,-66.120439 18.400227,-66.12095 18.399897,-66.121341 18.399685,-66.121713 18.399527,-66.122336 18.399321,-66.122773 18.39923,-66.123101 18.399178,-66.123577 18.39913,-66.129382 18.399318,-66.129902 18.399285,-66.131659 18.399139,-66.132254 18.399078,-66.132565 18.399027,-66.13349 18.398789,-66.134572 18.398422,-66.13744 18.397333,-66.138295 18.397031,-66.142076 18.395664,-66.1428 18.395479,-66.143948 18.395321,-66.144538 18.395292,-66.145144 18.395323,-66.148593 18.395684,-66.149634 18.395787,-66.15068 18.395823,-66.151219 18.395827,-66.15207 18.395838,-66.152849 18.395879,-66.153881 18.395938,-66.155205 18.396053,-66.15668 18.396222,-66.157412 18.396278,-66.157789 18.396298,-66.157872 18.396315,-66.157951 18.396362,-66.157998 18.396463,-66.158125 18.398379,-66.158138 18.398536,-66.158186 18.398759,-66.158506 18.399737,-66.159098 18.401621,-66.159127 18.401782,-66.159053 18.401885,-66.158965 18.40194,-66.158747 18.401992,-66.158508 18.401987,-66.158163 18.401939,-66.157164 18.401773,-66.156165 18.401608,-66.15596 18.401562,-66.155916 18.401519,-66.155893 18.401461,-66.155829 18.401149,-66.155772 18.400735,-66.155747 18.400636,-66.155712 18.400591,-66.155642 18.400556,-66.155593 18.400549,-66.155197 18.400588)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T2','BAYAMON', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_new where route = 'T2' and direction = 'SAGRADO_CORAZON'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.155126 18.400604,-66.154834 18.400694,-66.154764 18.400774,-66.15474 18.400973,-66.154782 18.401207,-66.154857 18.401461,-66.154913 18.401581,-66.155015 18.401651,-66.155188 18.401696,-66.155837 18.401682,-66.156039 18.401665,-66.15809 18.40201,-66.158496 18.402058,-66.158734 18.402076,-66.158939 18.402041,-66.159186 18.401979,-66.159288 18.401908,-66.159358 18.40182,-66.159349 18.401722,-66.158979 18.40094,-66.158597 18.399699,-66.158266 18.398746,-66.158211 18.398414,-66.158104 18.396455,-66.158053 18.396251,-66.157988 18.396176,-66.157829 18.396109,-66.157228 18.396083,-66.155726 18.396017,-66.154252 18.395853,-66.153298 18.395778,-66.151989 18.395723,-66.151563 18.395719,-66.150933 18.395725,-66.150498 18.395707,-66.148823 18.395574,-66.146388 18.395274,-66.144558 18.395083,-66.143937 18.395123,-66.142805 18.395291,-66.142073 18.395455,-66.140893 18.395844,-66.139013 18.396602,-66.134513 18.398338,-66.133797 18.398604,-66.133145 18.398793,-66.132459 18.398938,-66.131758 18.399028,-66.130698 18.399119,-66.129558 18.399198,-66.128261 18.399203,-66.127843 18.39919,-66.126235 18.399124,-66.125452 18.399098,-66.12355 18.39905,-66.123037 18.399097,-66.122657 18.399161,-66.122231 18.399262,-66.121857 18.399374,-66.121391 18.399562,-66.120834 18.399864,-66.120407 18.400156,-66.120165 18.400335,-66.119463 18.401033,-66.118894 18.401605,-66.118555 18.401864,-66.11764 18.402416,-66.11551 18.403719,-66.112774 18.405345,-66.110369 18.406737,-66.109561 18.407141,-66.109171 18.407219,-66.10897 18.407225,-66.108802 18.407225,-66.108556 18.407212,-66.108342 18.407262,-66.10811 18.407365,-66.107952 18.407485,-66.107825 18.407638,-66.107734 18.407815,-66.107674 18.408087,-66.107565 18.408452,-66.107464 18.408696,-66.107355 18.408869,-66.107144 18.409139,-66.106697 18.409651,-66.106347 18.409958,-66.106115 18.410104,-66.105933 18.410198,-66.105541 18.410301,-66.10497 18.410404,-66.103242 18.410757,-66.101305 18.411274,-66.100375 18.411622,-66.099049 18.412134,-66.097152 18.412855,-66.096172 18.413189,-66.095631 18.413342,-66.095015 18.413415,-66.094038 18.413451,-66.092391 18.413531,-66.08839 18.413656,-66.085459 18.41381,-66.084638 18.413913,-66.083276 18.414229,-66.082558 18.414414,-66.081167 18.414954,-66.078414 18.416218,-66.074659 18.417992,-66.071689 18.419419,-66.070173 18.420126,-66.068491 18.420861,-66.067856 18.421067,-66.067161 18.42123,-66.064679 18.421707,-66.060842 18.422595,-66.059837 18.422828,-66.059353 18.422998,-66.058285 18.423408,-66.058082 18.423488,-66.058036 18.423544,-66.058012 18.42363,-66.058011 18.423631,-66.058032 18.423778,-66.058579 18.426306,-66.059283 18.429503,-66.059884 18.432043,-66.060112 18.43272,-66.060621 18.434287,-66.06066 18.434634,-66.060649 18.434931,-66.060403 18.435747,-66.060338 18.435942,-66.060299 18.435993,-66.059356 18.436003,-66.05932 18.435962,-66.059302 18.43552,-66.059358 18.435333,-66.05938 18.435263,-66.059459 18.435218,-66.059756 18.435223)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T2','SAGRADO_CORAZON', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
