package com.trenurbanoapp.util;

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
}

