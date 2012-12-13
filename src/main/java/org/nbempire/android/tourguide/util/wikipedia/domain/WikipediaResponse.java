/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/13/12 at 12:12 AMhs.
 */
package org.nbempire.android.tourguide.util.wikipedia.domain;

/**
 * Utility entity that represents a response from Wikipedia API.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class WikipediaResponse {

    /**
     * Utility entity that represents a Query result from Wikipedia API.
     */
    private WikipediaQuery query;

    /**
     * Getter for {@link #query} type attribute.
     *
     * @return Utility entity that represents a Query result from Wikipedia API.
     */
    public WikipediaQuery getQuery() {
        return query;
    }

}
