/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/13/12 at 12:18 AMhs.
 */
package org.nbempire.android.tourguide.domain.wikipedia;

/**
 * Utility entity that represents a Place in the form that it is retrieved from Wikipedia.
 * <p/>
 * E.g.: {pageid":14001337,"ns":0,"title":"Pont d'I\u00e9na","lat":48.8597,"lon":2.29222,"dist":182.5,"primary":""}
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class WikipediaPlace {

    /**
     * The title of the place. It's the way that people recognise it.
     */
    private String title;

    /**
     * Latitude coordinate.
     */
    private double lat;


    /**
     * Longitude coordinate.
     */
    private double lon;

    /**
     * Getter for {@link #title} type attribute.
     *
     * @return The title of the place. It's the way that people recognise it.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter for {@link #lat} type attribute.
     *
     * @return Latitude coordinate.
     */
    public double getLat() {
        return lat;
    }

    /**
     * Getter for {@link #lon} type attribute.
     *
     * @return Longitude coordinate.
     */
    public double getLon() {
        return lon;
    }
}