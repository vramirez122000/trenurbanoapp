delete from ref.subroute where route = 'CI' and direction = 'TERM_CAGUAS';with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.158311 18.175719,-66.15804 18.175538,-66.157932 18.175585,-66.157934 18.175699,-66.158097 18.175854,-66.158132 18.175972,-66.158095 18.176034,-66.157983 18.1761,-66.157942 18.17621,-66.158026 18.176408,-66.158319 18.176772,-66.158323 18.17684,-66.158286 18.176903,-66.15705 18.177441,-66.156641 18.177619,-66.156422 18.177666,-66.156026 18.17765,-66.155662 18.177696,-66.155152 18.17768,-66.154105 18.177492,-66.153905 18.177501,-66.153724 18.177584,-66.153547 18.177728,-66.153387 18.177845,-66.153121 18.177888,-66.152806 18.177785,-66.152618 18.17754,-66.1524 18.177163,-66.152231 18.176936,-66.151994 18.176723,-66.151793 18.176584,-66.151566 18.176473,-66.150093 18.175828,-66.149623 18.175686,-66.149408 18.175674,-66.14919 18.175733,-66.14898 18.175824,-66.148723 18.176085,-66.148684 18.176194,-66.148721 18.176321,-66.149255 18.177085,-66.149339 18.17729,-66.149365 18.177467,-66.149347 18.177924,-66.149312 18.178065,-66.149168 18.178272,-66.148568 18.178792,-66.148283 18.178946,-66.148158 18.179003,-66.146645 18.179741,-66.146142 18.18001,-66.145806 18.180299,-66.144751 18.181577,-66.143968 18.182694,-66.143675 18.183124,-66.14357 18.183482,-66.143531 18.183553,-66.143439 18.183638,-66.143303 18.18371,-66.14313 18.183742,-66.140696 18.184074,-66.140521 18.184097,-66.140373 18.184149,-66.140243 18.184238,-66.139367 18.185154,-66.139044 18.185428,-66.138081 18.186027,-66.136875 18.186749,-66.136601 18.186928,-66.136314 18.187136,-66.13561 18.187513,-66.135457 18.187579,-66.135287 18.187686,-66.135185 18.187797,-66.134867 18.188311,-66.134637 18.18856,-66.134537 18.188683,-66.134384 18.188973,-66.13433 18.189107,-66.13433 18.189218,-66.134376 18.189611,-66.134365 18.189687,-66.134326 18.189777,-66.134245 18.189879,-66.132759 18.190904,-66.132585 18.190997,-66.129456 18.192194,-66.129223 18.192288,-66.128695 18.192412,-66.128437 18.19248,-66.127997 18.1926,-66.127838 18.192657,-66.127651 18.192778,-66.126984 18.193441,-66.126769 18.193668,-66.126297 18.194526,-66.125789 18.195449,-66.12571 18.195551,-66.125569 18.195647,-66.125144 18.195822,-66.124952 18.195879,-66.124655 18.195894,-66.124382 18.195832,-66.123905 18.195671,-66.123511 18.195653,-66.122861 18.19578,-66.122371 18.195939,-66.1221 18.196066,-66.121864 18.196172,-66.121656 18.196217,-66.12075 18.196154,-66.120415 18.196173,-66.119725 18.196312,-66.119227 18.196438,-66.118439 18.196689,-66.118061 18.19674,-66.117252 18.196688,-66.116298 18.196656,-66.115748 18.196741,-66.115455 18.196863,-66.114854 18.197206,-66.114561 18.197354,-66.114305 18.19743,-66.113846 18.19748,-66.113456 18.197544,-66.113176 18.197644,-66.112712 18.197864,-66.112386 18.198018,-66.112097 18.19814,-66.111767 18.198228,-66.111562 18.198306,-66.111441 18.19839,-66.111333 18.198516,-66.111167 18.198792,-66.110801 18.1993,-66.110656 18.199662,-66.110408 18.201318,-66.110312 18.201584,-66.110149 18.201784,-66.109931 18.201944,-66.109681 18.202035,-66.10932 18.202123,-66.108822 18.202281,-66.108475 18.202411,-66.108238 18.202555,-66.10775 18.202997,-66.107419 18.203367,-66.10722 18.203671,-66.107096 18.203974,-66.107064 18.204234,-66.106987 18.20576,-66.106913 18.206329,-66.106703 18.206878,-66.106454 18.207276,-66.106083 18.207682,-66.105609 18.20801,-66.105014 18.208294,-66.104329 18.208549,-66.103696 18.208694,-66.102311 18.20896,-66.101675 18.209009,-66.101198 18.208905,-66.100324 18.208531,-66.099706 18.208214,-66.09951 18.208037,-66.099309 18.207792,-66.099021 18.207413,-66.098791 18.207177,-66.098396 18.206976,-66.097828 18.206839,-66.096976 18.206736,-66.09634 18.206449,-66.095587 18.20603,-66.093834 18.205952,-66.0932 18.205892,-66.092919 18.205826,-66.092481 18.20566,-66.092101 18.205429,-66.091383 18.204804,-66.090439 18.203969,-66.089811 18.203515,-66.089422 18.203375,-66.089085 18.203325,-66.088181 18.203312,-66.087276 18.203347,-66.086852 18.203328,-66.086571 18.203275,-66.086328 18.203154,-66.086044 18.202975,-66.084878 18.202094,-66.08444 18.201846,-66.083823 18.20163,-66.083148 18.201648,-66.080335 18.201935,-66.079487 18.202024,-66.07898 18.202241,-66.078499 18.202611,-66.078057 18.202988,-66.077124 18.203799,-66.076798 18.204036,-66.076461 18.204196,-66.075598 18.204482,-66.074989 18.204586,-66.074172 18.204504,-66.073546 18.204514,-66.073104 18.204721,-66.072828 18.20505,-66.072648 18.205411,-66.072588 18.205737,-66.072795 18.20775,-66.072812 18.208148,-66.072765 18.208429,-66.072634 18.208693,-66.072371 18.208999,-66.072054 18.209198,-66.071031 18.209661,-66.07046 18.209889,-66.069695 18.210327,-66.069475 18.210478,-66.068202 18.21136,-66.067591 18.211665,-66.065145 18.212572,-66.064561 18.212661,-66.064068 18.212529,-66.06181 18.211302,-66.061257 18.211159,-66.060854 18.211167,-66.060552 18.21121,-66.05815 18.211866,-66.057753 18.211971,-66.05614 18.212414,-66.054636 18.212827,-66.054261 18.212969,-66.052997 18.213502,-66.051729 18.214176,-66.051531 18.214317,-66.050972 18.214839,-66.050016 18.215378,-66.048031 18.21646,-66.046111 18.217428,-66.043805 18.2186,-66.041643 18.219674,-66.041521 18.219716,-66.041372 18.219671,-66.041241 18.219632,-66.041138 18.219629,-66.041071 18.219678,-66.041018 18.219865,-66.039634 18.222561,-66.037328 18.226743,-66.037129 18.227145,-66.036432 18.228407,-66.036162 18.228931,-66.035612 18.230205,-66.035167 18.231265,-66.035111 18.231277,-66.034879 18.231252,-66.034417 18.231325,-66.03431 18.231827,-66.034118 18.232441,-66.033969 18.232988,-66.03373 18.233717,-66.033503 18.234387,-66.033422 18.234439,-66.033222 18.234863,-66.033119 18.235035,-66.032763 18.234988,-66.032448 18.235016)'), 32161) geom) insert into ref.subroute(route, direction, geom) select 'CI','TERM_CAGUAS', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute where route = 'CI' and direction = 'CIDRA';with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.032334 18.235034,-66.031877 18.235038,-66.031749 18.235322,-66.030735 18.235051,-66.031324 18.233574,-66.031505 18.233171,-66.031832 18.232466,-66.031912 18.232384,-66.032211 18.232007,-66.03301 18.23034,-66.034564 18.228074,-66.034857 18.22758,-66.034906 18.227411,-66.034944 18.227254,-66.035028 18.227115,-66.035145 18.227022,-66.035421 18.226531,-66.035528 18.226415,-66.03572 18.226347,-66.035952 18.226364,-66.036416 18.226563,-66.036799 18.226724,-66.037118 18.226879,-66.037335 18.226863,-66.039676 18.222582,-66.041207 18.219811,-66.041371 18.219773,-66.041521 18.219787,-66.041648 18.219751,-66.043827 18.21864,-66.046133 18.217468,-66.048054 18.216499,-66.050094 18.215428,-66.051027 18.214903,-66.051628 18.214357,-66.051744 18.214263,-66.053116 18.21355,-66.054627 18.212922,-66.05616 18.21249,-66.057799 18.212041,-66.058233 18.211924,-66.060562 18.211286,-66.060907 18.211236,-66.061261 18.21124,-66.061823 18.211377,-66.063998 18.212604,-66.064545 18.212728,-66.065137 18.212651,-66.067581 18.211746,-66.068247 18.211413,-66.069647 18.210508,-66.070605 18.209922,-66.071095 18.20974,-66.07209 18.209249,-66.072399 18.20907,-66.072693 18.208737,-66.072834 18.208444,-66.072879 18.208134,-66.072869 18.207728,-66.072671 18.20574,-66.072731 18.205413,-66.072902 18.205068,-66.073136 18.204756,-66.073558 18.204558,-66.07417 18.204549,-66.074991 18.204631,-66.07561 18.204526,-66.076479 18.204237,-66.076823 18.204074,-66.077154 18.203833,-66.078088 18.203022,-66.078528 18.202647,-66.079035 18.202299,-66.079542 18.2021,-66.080341 18.20198,-66.083815 18.201676,-66.08441 18.201899,-66.085993 18.203028,-66.086244 18.203202,-66.086548 18.203335,-66.086845 18.2034,-66.087263 18.203421,-66.089082 18.20337,-66.08941 18.203419,-66.089787 18.203554,-66.090408 18.204003,-66.091351 18.204837,-66.092073 18.205464,-66.092461 18.205701,-66.092905 18.205869,-66.093191 18.205936,-66.09383 18.205997,-66.095572 18.206075,-66.096318 18.206489,-66.096963 18.20678,-66.097819 18.206883,-66.098379 18.207011,-66.09876 18.207212,-66.098984 18.207441,-66.099272 18.207819,-66.099472 18.208061,-66.099681 18.208251,-66.100319 18.208564,-66.101187 18.208935,-66.101673 18.209041,-66.102316 18.208992,-66.103703 18.208725,-66.104339 18.20858,-66.105028 18.208322,-66.105626 18.208037,-66.106106 18.207705,-66.106481 18.207294,-66.106733 18.206891,-66.106945 18.206336,-66.10702 18.205762,-66.107097 18.204236,-66.107128 18.203982,-66.10725 18.203685,-66.107446 18.203386,-66.107774 18.203019,-66.108258 18.202579,-66.10849 18.202439,-66.108834 18.20231,-66.109329 18.202154,-66.109692 18.202065,-66.109948 18.201971,-66.110173 18.201807,-66.110342 18.201599,-66.110441 18.201326,-66.110688 18.19967,-66.110831 18.199315,-66.111195 18.198809,-66.111361 18.198534,-66.111464 18.198413,-66.111578 18.198334,-66.111777 18.198258,-66.112108 18.198169,-66.1124 18.198047,-66.112727 18.197892,-66.113189 18.197673,-66.113464 18.197575,-66.113851 18.197511,-66.114312 18.197461,-66.114574 18.197383,-66.11487 18.197233,-66.11547 18.196891,-66.115758 18.196772,-66.1163 18.196687,-66.11725 18.19672,-66.118062 18.196772,-66.118447 18.19672,-66.119237 18.196468,-66.119733 18.196343,-66.120419 18.196204,-66.120749 18.196185,-66.121659 18.196249,-66.121875 18.196202,-66.122114 18.196095,-66.122384 18.195968,-66.12287 18.19581,-66.123514 18.195685,-66.123898 18.195702,-66.124372 18.195862,-66.124652 18.195925,-66.124957 18.19591,-66.125155 18.195852,-66.125585 18.195675,-66.125734 18.195574,-66.125817 18.195465,-66.126327 18.19454,-66.126796 18.193686,-66.127008 18.193463,-66.127673 18.192802,-66.127853 18.192685,-66.128008 18.19263,-66.128446 18.19251,-66.128704 18.192442,-66.129234 18.192318,-66.129469 18.192223,-66.132599 18.191026,-66.132777 18.19093,-66.134269 18.189902,-66.134355 18.189793,-66.134398 18.189695,-66.134409 18.189611,-66.134363 18.189216,-66.134363 18.189113,-66.134414 18.188985,-66.134565 18.1887,-66.134662 18.18858,-66.134894 18.18833,-66.135212 18.187816,-66.135309 18.18771,-66.135473 18.187606,-66.135625 18.187541,-66.136332 18.187163,-66.136478 18.187065,-66.138055 18.186084,-66.139064 18.185453,-66.13939 18.185176,-66.140265 18.184262,-66.140388 18.184177,-66.140529 18.184128,-66.1407 18.184105,-66.143135 18.183773,-66.143314 18.183739,-66.143459 18.183663,-66.143558 18.183572,-66.143601 18.183493,-66.143705 18.183137,-66.143995 18.182711,-66.144781 18.181599,-66.145826 18.18031,-66.146158 18.18005,-66.14669 18.179764,-66.148234 18.179049,-66.148483 18.178921,-66.149208 18.1783,-66.149362 18.17808,-66.149408 18.177928,-66.149428 18.177452,-66.149388 18.177277,-66.14934 18.17712,-66.14875 18.17623,-66.14875 18.176184,-66.148772 18.176119,-66.149039 18.175896,-66.149252 18.175837,-66.149593 18.175823,-66.150003 18.17597,-66.15191 18.176817,-66.152185 18.176972,-66.152373 18.177181,-66.15259 18.177557,-66.152785 18.177811,-66.153119 18.17792,-66.153401 18.177875,-66.153568 18.177753,-66.153742 18.177611,-66.153914 18.177532,-66.154103 18.177524,-66.155148 18.177712,-66.155664 18.177728,-66.156027 18.177682,-66.156425 18.177698,-66.156652 18.177649,-66.157064 18.17747,-66.158326 18.176922,-66.158362 18.176834,-66.158354 18.176762,-66.158052 18.176394,-66.158021 18.176288,-66.158017 18.176183,-66.158097 18.176123,-66.158282 18.176007,-66.158356 18.175928,-66.158331 18.175788)'), 32161) geom) insert into ref.subroute(route, direction, geom) select 'CI','CIDRA', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_stop where route = 'CI' and direction = 'TERM_CAGUAS'; insert into ref.subroute_stop (stop, stop_order, route, direction) values (8013, 0, 'CI', 'TERM_CAGUAS'),(8000, 1, 'CI', 'TERM_CAGUAS');
delete from ref.subroute_stop where route = 'CI' and direction = 'CIDRA'; insert into ref.subroute_stop (stop, stop_order, route, direction) values (8000, 0, 'CI', 'CIDRA'),(8013, 1, 'CI', 'CIDRA');