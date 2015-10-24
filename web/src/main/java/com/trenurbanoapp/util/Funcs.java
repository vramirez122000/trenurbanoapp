package com.trenurbanoapp.util;

import org.apache.commons.lang3.StringUtils;

import java.awt.*;

/**
 * Created by victor on 15/09/15.
 */
public class Funcs {

    public static String darken(String hexRgb) {
        Color c = Color.decode(hexRgb).darker();
        return String.format("#%s%s%s",
                StringUtils.leftPad(Integer.toHexString(c.getRed()), 2, '0'),
                StringUtils.leftPad(Integer.toHexString(c.getGreen()), 2, '0'),
                StringUtils.leftPad(Integer.toHexString(c.getBlue()), 2, '0')
        );
    }
}
