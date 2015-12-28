package com.trenurbanoapp.util;

import com.trenurbanoapp.service.impl.VehicleSnapshotAlgServiceSubroute;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * Created by victor on 15/09/15.
 */
public class Funcs {

    /**
     * Darken color in html hexadecimal Rgb format
     *
     * @param hexRgb example '#34F56A' or '34F56A'
     * @return HexRgb formatted string
     */
    public static String darken(String hexRgb) {
        Color c = Color.decode(hexRgb).darker();
        return String.format("#%s%s%s",
                StringUtils.leftPad(Integer.toHexString(c.getRed()), 2, '0'),
                StringUtils.leftPad(Integer.toHexString(c.getGreen()), 2, '0'),
                StringUtils.leftPad(Integer.toHexString(c.getBlue()), 2, '0')
        );
    }

    /**
     * Darken color in html hexadecimal Rgb format
     *
     * @param hexRgb example '#34F56A' or '34F56A'
     * @return HexRgb formatted string
     */
    public static String shade(String hexRgb, double factor) {
        Color old = Color.decode(hexRgb);
        return String.format("#%s%s%s",
                StringUtils.leftPad(Integer.toHexString(Math.max((int) (old.getRed() * factor), 0)), 2, '0'),
                StringUtils.leftPad(Integer.toHexString(Math.max((int) (old.getGreen() * factor), 0)), 2, '0'),
                StringUtils.leftPad(Integer.toHexString(Math.max((int) (old.getBlue() * factor), 0)), 2, '0')
        );
    }

    public static final String[] DIRECTIONS = new String[]{
            "norte",
            "noreste",
            "noreste",
            "este",
            "este",
            "sureste",
            "sureste",
            "sur",
            "sur",
            "suroeste",
            "suroeste",
            "oeste",
            "oeste",
            "noroeste",
            "noroeste",
            "norte"
    };

    protected static String bearingToCardinal(double x) {
        double twoPi = 2 * Math.PI;
        if (x < 0) {
            x = x + twoPi;
        } else if (x >= twoPi) {
            x = x % twoPi;
        }
        int index = (int) Math.round((x / twoPi) * (DIRECTIONS.length - 1));
        return DIRECTIONS[index];
    }
}

