/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/13/12 at 12:09 AMhs.
 */
package org.nbempire.android.tourguide.util.wikipedia.domain;

/**
 * Utility entity that represents a Query result from Wikipedia API.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class WikipediaQuery {

    /**
     * A list of {@link WikipediaPlace} with search results.
     */
    private WikipediaPlace[] geosearch;

    /**
     * Getter for {@link #geosearch} type attribute.
     *
     * @return A list of {@link WikipediaPlace} with search results.
     */
    public WikipediaPlace[] getGeosearch() {
        return geosearch;
    }
}
