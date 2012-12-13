/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/13/12 at 2:02 PMhs.
 */
package org.nbempire.android.tourguide.util.wikipedia;

/**
 * Utility type to centralize Wikipedia constants.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public abstract class WikipediaConstants {

    /**
     * The URL used to navigate to a Wikipedia page. Client must append the name of any page.
     */
    public static final String PAGE_URL_PREFFIX = "http://en.wikipedia.org/wiki/";

    /**
     * Character used by Wikipedia to replace white spaces in the URL.
     */
    public static final char URL_SPACE_REPLACEMENT = '_';
}
