package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.scraper.model.LatLng;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by victor on 5/5/14.
 */
class CrsConversion {

    private static final MathTransform wgs84toNad83;
    private static final CoordinateReferenceSystem nad83;

    static {
        try {
            nad83 = CRS.decode("EPSG:32161");
            wgs84toNad83 =  CRS.findMathTransform(DefaultGeographicCRS.WGS84, nad83, true);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    static List<LatLng> convertToNad83(List<LatLng> line) {
        try {
            double[] srcCoords = new double[line.size() * 2];
            double[] dstCoords = new double[line.size() * 2];
            for (int i = 0; i < line.size(); i++) {
                LatLng latLng = line.get(i);
                srcCoords[i * 2] = latLng.getLng();
                srcCoords[i * 2 + 1] = latLng.getLat();
            }

            wgs84toNad83.transform(srcCoords, 0, dstCoords, 0, line.size());

            List<LatLng> result = new ArrayList<>(line.size());
            for (int i = 0; i < dstCoords.length; i += 2) {
                result.add(new LatLng(dstCoords[i + 1], dstCoords[i]));
            }
            return result;
        } catch (TransformException e) {
            throw new RuntimeException(e);
        }
    }

    static LatLng convertToNad83(LatLng latLng) {
        return convertToNad83(Arrays.asList(latLng)).get(0);
    }
}
