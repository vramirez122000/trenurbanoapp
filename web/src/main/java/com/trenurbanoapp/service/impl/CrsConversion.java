package com.trenurbanoapp.service.impl;

import com.trenurbanoapp.scraper.model.LatLng;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.*;

/**
 * Copypasta from Geotools
 */
class CrsConversion {


    static List<LatLng> convertToNad83(List<LatLng> line) {
        return line.stream().map(CrsConversion::convertToNad83).collect(Collectors.toList());

    }

    public static LatLng convertToNad83(LatLng latLng) {
        final Point2D criolloPoint = transform(new Point2D.Double(latLng.getLng(), latLng.getLat()), null);
        return new LatLng(criolloPoint.getY(), criolloPoint.getX());
    }


    /**
     * Internal variables for computation.
     */
    private static final double n = 0.312888187729445;
    private static final double F = 3.358037813839668;
    private static final double rho0 = 3.0434758923179808;

    /**
     * Central longitude in <u>radians</u>. Default value is 0, the Greenwich meridian.
     * This is called '<var>lambda0</var>' in Snyder.
     * <p>
     * <strong>Consider this field as final</strong>. It is not final only
     * because some classes need to modify it at construction time.
     */
    private static double centralMeridian = -1.1594803997415664D;

    /**
     * Global scale factor. Default value {@code globalScale} is equal
     * to semiMajor &times; scaleFactor.
     * <p>
     * <strong>Consider this field as final</strong>. It is not final only
     * because some classes need to modify it at construction time.
     */
    private static final double globalScale = 6378137.0;

    /**
     * False easting, in metres. Default value is 0.
     */
    private static final double falseEasting = 200000.0;

    /**
     * False northing, in metres. Default value is 0.
     */
    private static final double falseNorthing = 200000.0;

    /**
     * Ellipsoid eccentricity. Value 0 means that the ellipsoid is spherical.
     */
    private static final double eccentricity = 0.08181919104281514;


    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     * <p>
     * This method standardizes the source {@code x} coordinate
     * by removing the {@link #centralMeridian}, before invoking
     * <code>{@link #transformNormalized transformNormalized}(x, y, ptDst)</code>.
     * It also multiplies by {@link #globalScale} and adds the {@link #falseEasting} and
     * {@link #falseNorthing} to the point returned by the {@code transformNormalized(...)} call.
     *
     * @param ptSrc the specified coordinate point to be transformed.
     *              Ordinates must be in decimal degrees.
     * @param ptDst the specified coordinate point that stores the result of transforming
     *              {@code ptSrc}, or {@code null}. Ordinates will be in metres.
     * @return the coordinate point after transforming {@code ptSrc} and storing
     * the result in {@code ptDst}.
     */
    private static Point2D transform(final Point2D ptSrc, Point2D ptDst) {
        final double x = ptSrc.getX();
        final double y = ptSrc.getY();

        /*
         * Makes sure that the longitude before conversion stay within +/- PI radians. As a
         * special case, we do not check the range if no rotation were applied on the longitude.
         * This is because the user may have a big area ranging from -180° to +180°. With the
         * slight rounding errors related to map projections, the 180° longitude may be slightly
         * over the limit. Rolling the longitude would changes its sign. For example a bounding
         * box from 30° to +180° would become 30° to -180°, which is probably not what the user
         * wanted.
         */
        ptDst = transformNormalized(centralMeridian != 0 ?
                rollLongitude(toRadians(x) - centralMeridian) : toRadians(x), toRadians(y), ptDst);
        ptDst.setLocation(globalScale * ptDst.getX() + falseEasting,
                globalScale * ptDst.getY() + falseNorthing);

        return ptDst;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    private static Point2D transformNormalized(double x, double y, Point2D ptDst) {
        double rho = F * pow(tsfn(y, sin(y)), n);
        x *= n;
        y = rho0 - rho * cos(x);
        x = rho * sin(x);
        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /**
     * Ensures that the specified longitude stay within &plusmn;&pi; radians. This method
     * is typically invoked after geographic coordinates are transformed. This method may add
     * or substract some amount of 2&pi; radians to <var>x</var>.
     *
     * @param x The longitude in radians.
     * @return The longitude in the range &plusmn;&pi; radians.
     */
    private static double rollLongitude(final double x) {
        return x - (2 * Math.PI) * Math.floor(x / (2 * Math.PI) + 0.5);
    }

    /**
     * Computes function (15-9) and (9-13) from Snyder.
     * Equivalent to negative of function (7-7).
     */
    private static double tsfn(final double phi, double sinphi) {
        sinphi *= eccentricity;
        /*
         * NOTE: change sign to get the equivalent of Snyder (7-7).
         */
        return tan(0.5 * (PI / 2 - phi)) / pow((1 - sinphi) / (1 + sinphi), 0.5 * eccentricity);
    }

}
