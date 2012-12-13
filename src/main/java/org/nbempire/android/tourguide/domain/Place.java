/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/12/12 at 9:16 PMhs.
 */
package org.nbempire.android.tourguide.domain;

/**
 * Domain object that represents a Place.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class Place {

    /**
     * The title of the place. It's the way that people recognise it.
     */
    private String title;

    /**
     * Latitude coordinate.
     */
    private double latitude;

    /**
     * Longitude coordinate.
     */
    private double longitude;

    /**
     * Creates a new Place with all its required attributes.
     *
     * @param aTitle
     *         The title of the place. It's the way that people recognise it. Can't be {@code null}.
     * @param aLatitude
     *         Latitude coordinate.
     * @param aLongitude
     *         Longitude coordinate.
     */
    public Place(String aTitle, double aLatitude, double aLongitude) {
        this.title = aTitle;
        this.latitude = aLatitude;
        this.longitude = aLongitude;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Place");
        sb.append("{title='").append(title).append('\'');
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append('}');
        return sb.toString();
    }
}
