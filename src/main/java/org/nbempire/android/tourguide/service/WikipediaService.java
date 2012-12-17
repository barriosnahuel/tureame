/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/13/12 at 2:30 PMhs.
 */
package org.nbempire.android.tourguide.service;

import org.nbempire.android.tourguide.domain.wikipedia.WikipediaPlace;

/**
 * Service for Wikipedia domain objects.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public interface WikipediaService {

    /**
     * Gets the URL of the specified {@code pageTitle}.
     *
     * @param pageTitle
     *         The title of a valid page in Wikipedia.
     *
     * @return A valid URL.
     */
    String getPageUrl(String pageTitle);

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
    WikipediaPlace[] queryGeosearch(double latitude, double longitude);
}
