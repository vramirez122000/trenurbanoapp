package com.trenurbanoapp.service.impl;

import com.google.common.base.Joiner;
import com.trenurbanoapp.scraper.model.LatLng;

import java.util.List;

/**
 * Created by victor on 4/23/14.
 */
abstract class Constants {

    static final Joiner COMMA_JOINER = Joiner.on(",");

    static boolean trailsAreDifferent(List<LatLng> a, List<LatLng> b) {
        if(a == null && b == null)  {
            throw new IllegalArgumentException("Don't pass me no nulls, asshole");
        }

        if(a == null || b == null) {
            return false;
        }

        if(a.size() != b.size()) {
            return false;
        }

        for (int i = 0; i < a.size(); i++) {
            LatLng latLng = a.get(i);
            if(!latLng.equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }

}
