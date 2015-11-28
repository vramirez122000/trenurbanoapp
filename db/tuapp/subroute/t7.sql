delete from ref.subroute_new where route = 'T7' and direction = 'CUPEY'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-65.953677 18.386092,-65.953595 18.386213,-65.953591 18.386325,-65.953529 18.386356,-65.953353 18.38638,-65.953255 18.386331,-65.953212 18.386249,-65.954759 18.3837,-65.954863 18.383658,-65.954999 18.383575,-65.954987 18.383438,-65.954975 18.383321,-65.956043 18.3815,-65.956091 18.380813,-65.956198 18.379827,-65.956079 18.378419,-65.956055 18.378306,-65.954985 18.376724,-65.954956 18.376628,-65.955086 18.376462,-65.955358 18.376376,-65.955872 18.376429,-65.956828 18.37666,-65.957947 18.3771,-65.958953 18.377669,-65.960067 18.378436,-65.962509 18.380287,-65.964426 18.38173,-65.966421 18.383232,-65.967569 18.384074,-65.971871 18.387224,-65.974585 18.389182,-65.975902 18.390131,-65.978316 18.391945,-65.981307 18.394112,-65.983398 18.395678,-65.985275 18.396891,-65.986966 18.397652,-65.988744 18.398245,-65.990021 18.398476,-65.990609 18.398548,-65.990807 18.398537,-65.990892 18.398371,-65.99092 18.398154,-65.990917 18.397821,-65.990934 18.397368,-65.99068 18.396606,-65.990556 18.396172,-65.990686 18.395563,-65.990838 18.395128,-65.991172 18.394753,-65.992927 18.393971,-65.993554 18.393673,-65.99409 18.393501,-65.999295 18.392242,-65.999472 18.392262,-65.999601 18.39236,-65.999681 18.392513,-65.999869 18.394124,-66.000114 18.395976,-66.000666 18.396594,-66.000739 18.39689,-66.000734 18.397208,-66.000845 18.397416,-66.001049 18.397522,-66.002901 18.397398,-66.004476 18.397345,-66.005838 18.397302,-66.011604 18.397145,-66.017258 18.396994,-66.018651 18.39695,-66.020768 18.396868,-66.021784 18.396821,-66.022807 18.396704,-66.023698 18.396571,-66.024812 18.396327,-66.026613 18.39599,-66.028364 18.395631,-66.031048 18.395096,-66.031915 18.394977,-66.033188 18.394826,-66.034357 18.394731,-66.035353 18.394711,-66.036415 18.394702,-66.037206 18.39471,-66.038991 18.39482,-66.03907 18.394823,-66.040922 18.394945,-66.041534 18.395008,-66.042039 18.395179,-66.042463 18.395382,-66.042754 18.39546,-66.04303 18.395476,-66.043351 18.395429,-66.043615 18.395338,-66.043906 18.395213,-66.044306 18.395205,-66.044814 18.395249,-66.045207 18.395267,-66.045686 18.395278,-66.046174 18.395262,-66.046655 18.395264,-66.047168 18.395324,-66.047987 18.395439,-66.048698 18.395491,-66.049683 18.39554,-66.050488 18.395613,-66.05131 18.395694,-66.051869 18.395846,-66.052224 18.396033,-66.052827 18.396417,-66.053579 18.39685,-66.054099 18.396926,-66.054538 18.396835,-66.054978 18.396543,-66.055122 18.396224,-66.055182 18.395727,-66.055326 18.395257,-66.055554 18.39501,-66.056634 18.394261,-66.058452 18.393242,-66.060728 18.392013,-66.061875 18.39123,-66.062582 18.390607,-66.062774 18.390413,-66.062945 18.390355,-66.063047 18.390275,-66.063296 18.389951,-66.063374 18.389827,-66.063353 18.389695,-66.063177 18.389528,-66.062937 18.389386,-66.062807 18.389329,-66.062685 18.389482,-66.062565 18.389647)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T7','CUPEY', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
delete from ref.subroute_new where route = 'T7' and direction = 'CAROLINA'; with line_tmp as (select ST_Transform(ST_GeomFromEWKT('SRID=4326;LINESTRING(-66.062521 18.389683,-66.062287 18.389997,-66.06232 18.390075,-66.062456 18.390161,-66.062535 18.390273,-66.062496 18.390432,-66.062312 18.390651,-66.061269 18.391462,-66.060034 18.392205,-66.058482 18.393028,-66.057436 18.39361,-66.056215 18.394401,-66.054072 18.396,-66.053597 18.396341,-66.053275 18.396443,-66.052902 18.396365,-66.051938 18.395742,-66.051588 18.395611,-66.051206 18.395565,-66.049971 18.395464,-66.047955 18.395321,-66.046938 18.395176,-66.046604 18.395142,-66.045623 18.395134,-66.044535 18.395069,-66.042063 18.394926,-66.040419 18.394814,-66.039043 18.394705,-66.037751 18.39461,-66.036993 18.394567,-66.03642 18.39456,-66.035278 18.394576,-66.034289 18.394604,-66.032953 18.394713,-66.031876 18.394871,-66.031332 18.394927,-66.030529 18.395064,-66.028305 18.395503,-66.026311 18.395892,-66.024644 18.396234,-66.023875 18.396392,-66.023156 18.396502,-66.022292 18.396611,-66.021706 18.396684,-66.02139 18.396702,-66.020835 18.39672,-66.019775 18.396732,-66.01773 18.396792,-66.015746 18.396858,-66.013156 18.396931,-66.011398 18.396962,-66.010085 18.396987,-66.00866 18.397022,-66.007419 18.397072,-66.005704 18.397078,-66.002495 18.397218,-66.001928 18.397255,-66.001593 18.397215,-66.001435 18.397205,-66.001103 18.397224,-66.000959 18.397172,-66.000852 18.397043,-66.000825 18.39677,-66.000748 18.39657,-66.000386 18.39618,-66.000181 18.395956,-66.000134 18.395785,-66.000057 18.395124,-65.999891 18.393673,-65.999747 18.392356,-65.999722 18.392236,-65.999658 18.392159,-65.999554 18.39213,-65.999301 18.392173,-65.995358 18.393138,-65.994069 18.393448,-65.993634 18.393563,-65.993491 18.39363,-65.991094 18.394717,-65.990789 18.395092,-65.990636 18.395543,-65.9905 18.396176,-65.990631 18.396626,-65.990874 18.397367,-65.99084 18.397801,-65.990738 18.398263,-65.990664 18.398359,-65.99054 18.39837,-65.989896 18.398295,-65.988816 18.398086,-65.987014 18.397516,-65.985341 18.396765,-65.983452 18.395584,-65.978377 18.391864,-65.975547 18.38977,-65.970612 18.386103,-65.964568 18.381642,-65.961708 18.3795,-65.959525 18.377899,-65.958726 18.377368,-65.957671 18.376845,-65.956343 18.376389,-65.955112 18.376184,-65.954978 18.376189,-65.954834 18.37628,-65.954817 18.376408,-65.954834 18.376631,-65.954992 18.376856,-65.956018 18.378324,-65.956035 18.378415,-65.956143 18.379821,-65.95601 18.380802,-65.95597 18.381478,-65.954924 18.383286,-65.954834 18.383343,-65.954738 18.383375,-65.954684 18.383453,-65.954695 18.383571,-65.954684 18.383683,-65.954483 18.383989,-65.954147 18.384503,-65.953991 18.384779,-65.953898 18.384945,-65.953734 18.385211,-65.953578 18.385409,-65.953429 18.385643,-65.953344 18.385914,-65.95312 18.386308,-65.953174 18.386399,-65.953279 18.386429,-65.953912 18.386335,-65.953935 18.386254,-65.953906 18.386144,-65.953731 18.386075)'), 32161) geom) insert into ref.subroute_new(route, direction, geom) select 'T7','CAROLINA', ST_AddMeasure(line_tmp.geom, 0, ST_Length(line_tmp.geom)) from line_tmp;
