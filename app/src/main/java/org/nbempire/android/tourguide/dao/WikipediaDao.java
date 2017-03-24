/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/17/12 at 1:46 PMhs.
 */
package org.nbempire.android.tourguide.dao;

import org.nbempire.android.tourguide.domain.wikipedia.WikipediaPlace;

/**
 * DAO for Wikipedia domain objects.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public interface WikipediaDao {

    /**
     * Find all places from Wikipedia API that are near to the specified location.
     *
     * @param latitude
     *         Location's latitude.
     * @param longitude
     *         Location's longitude.
     *
     * @return An array of WikipediaPlace.
     */
    WikipediaPlace[] queryByGeosearch(double latitude, double longitude);
}
