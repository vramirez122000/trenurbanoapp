package com.trenurbanoapp.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trenurbanoapp.model.VehiclePositionView;
import com.trenurbanoapp.service.VehicleSnapshotAlgService;
import com.trenurbanoapp.service.VehicleSnapshotService;
import com.trenurbanoapp.scraper.model.AssetPosition;
import com.trenurbanoapp.scraper.parser.AssetSnapshotParseListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

/**
 * Created by victor on 5/17/14.
 */
@Component
@Scope(value = "prototype")
class AssetSnapshotParseListenerVehicleSnapshots implements AssetSnapshotParseListener {

    private static final Logger log = LogManager.getLogger(AssetSnapshotParseListenerVehicleSnapshots.class);

    private StringBuilder out = new StringBuilder();

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private VehicleSnapshotService vehicleSnapshotService;

    @Inject
    private VehicleSnapshotAlgService vehicleSnapshotAlgService;

    @Override
    public void parseBegin(Date timeStamp) {
        try {
            StringWriter jsonOut = new StringWriter(20);
            objectMapper.writeValue(jsonOut, timeStamp);
            out.append("{\"timeStamp\":")
                    .append(jsonOut.toString())
                    .append(",\"d\":[");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void assetSnapshotParsed(AssetPosition assetSnapshot, int index) {
        try {
            if (index > 0) {
                out.append(",");
            }
            VehiclePositionView view = vehicleSnapshotAlgService.getVehicleSnapshotView(assetSnapshot);
            String jsonOut = objectMapper.writer().writeValueAsString(view);
            out.append(jsonOut);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void parseEnd() {
        log.debug("parse end");
        out.append("]}");
        vehicleSnapshotService.saveVehicleSnapshots(out.toString());
    }
}
