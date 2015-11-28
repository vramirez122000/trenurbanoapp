package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.dao.VehicleDao;
import com.trenurbanoapp.model.Vehicle;
import com.trenurbanoapp.scraper.model.Asset;
import com.trenurbanoapp.webapi.AssetsResponse;
import com.trenurbanoapp.webapi.WebApiRestClient2;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * Created by victor on 25/11/15.
 */
@Service
public class VehicleServiceImpl implements com.trenurbanoapp.service.VehicleService {

    @Inject
    VehicleDao vehicleDao;

    @Inject
    WebApiRestClient2 webApiClient;

    @Override
    public void updateAssets() {
        AssetsResponse assetsResp = webApiClient.assets();
        if(assetsResp.isError()) {
            return;
        }

        for (Asset asset : assetsResp.getAssets()) {
            Vehicle v = new Vehicle();
            if(asset.getGroupId() != null) {
                v.setGroupId(Integer.valueOf(asset.getGroupId()));
            }

            //filter
            if(!asset.getDescription().startsWith("200")
                    && !asset.getDescription().startsWith("201")) {
                return;
            }

            v.setName(asset.getDescription());
            v.setAssetId(asset.getId());
            if(vehicleDao.vehicleExists(v.getAssetId())) {
                vehicleDao.updateVehicle(v);
            } else {
                vehicleDao.insertVehicle(v);
            }
        }
    }
}
