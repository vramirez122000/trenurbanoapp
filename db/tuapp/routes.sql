drop table if exists ref.route_new;

create table ref.route_new (
  id varchar(100) PRIMARY KEY,
  "desc" varchar(255),
  color varchar(50),
  gpsenabled boolean,
  sort_order int,
  priority int
);

INSERT INTO ref.route_new (id,"desc",color,gpsenabled, sort_order, priority) VALUES
 ('TU','Tren Urbano','#4d8327',false,1,1),
 ('T3','T3','#bf6a4b',false,2,2),
 ('E10','E10','#583c64',false,3,2),
 ('C1','C1','#8067a5',false,4,2),
 ('E20','E20','#ff0000',false,5,10),
 ('AX1','Acuaexpreso','#00FF00',false,6,3),
 ('T2','T2','#43aad6',false,10,3),
 ('T4','T4','#e3591b',false,11,3),
 ('T5','T5','#e561de',false,13,3),
 ('T6','T6','#225226',false,14,3),
 ('T7','T7','#792f4d',false,15,9),
 ('T9','T9','#8c6712',false,17,3),
 ('C35','C35','#41df90',false,18,9),
 ('C36','C36','#5a82e8',false,19,9),
 ('D15','D15','#e3338d',false,20,9),
 ('D18','D18','#ea9329',false,23,9),
 ('T21','T21','#9a5bbb',false,25,9),
 ('C22','C22','#df6fa9',false,26,9),
 ('D26','D26','#2d5e86',false,27,9),
 ('D27','D27','#6d771d',false,28,9),
 ('T8','T8','#38a566',false,29,9),
 ('D13','D13','#60e26b',false,32,9),
 ('D37','D37','#af8edd',false,33,9),
 ('E40','E40','#6598dd',false,34,9),
 ('T41','T41','#454909',false,35,9),
 ('C43','C43','#8cd92f',false,36,9),
 ('C44','C44','#b69924',false,37,9),
 ('D45','D45','#bf6e29',false,38,9),
 ('C51','C51','#dd527e',false,40,9),
 ('D53','D53','#c84aa5',false,45,9),
 ('D91','D91','#8b2363',false,46,9),
 ('D92','D92','#783c18',false,47,9)
;
