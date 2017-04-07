package com.trenurbanoapp.util;

import com.trenurbanoapp.dao.RouteDao;
import com.trenurbanoapp.dao.VehicleDao;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.scraper.model.AvlRoute;
import com.trenurbanoapp.webapi.AssetsResponse;
import com.trenurbanoapp.webapi.AvlRouteResponse;
import com.trenurbanoapp.webapi.WebApiRestClient;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by victor on 03-29-17.
 */
public class StaticDataDownloader {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:/com/trenubanoapp/datasource-context.xml",
                "classpath:/com/trenubanoapp/dao-context.xml");

        WebApiRestClient client = new WebApiRestClient(args[0], args[1], args[2]);


        RouteDao routeDao = ctx.getBean(RouteDao.class);

        AvlRouteResponse routes = client.routes();
        for (AvlRoute r : routes.getRoutes()) {
            
        }


        VehicleDao vehDao = ctx.getBean(VehicleDao.class);
        AssetsResponse assets = client.assets();
        System.out.printf("%s,%s,%s,%s,%s", "asset_id", "name", "group_id", "plate", "routes");
        for (Asset a : assets.getAssets()) {
            vehDao.getVehicle(a.getId());
            System.out.printf("%s,%s,%s,%s,%s",
                    a.getId(), a.getDescription(), a.getGroupId(), a.getLicensePlate(), a.getIdTravel());
        }

    }
}
