package com.trenurbanoapp.service;

import com.trenurbanoapp.model.VehiclePositionView;
import com.trenurbanoapp.scraper.model.AssetPosition;

/**
 * Created by victor on 4/23/14.
 */
public interface VehicleSnapshotAlgService {

    VehiclePositionView getVehicleSnapshotView(AssetPosition assetSnapshot);

    void updateVehicleState(AssetPosition assetSnapshot);

}
