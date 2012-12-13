/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/13/12 at 2:41 PMhs.
 */
package org.nbempire.android.tourguide.service.impl;

import org.nbempire.android.tourguide.service.WikipediaService;
import org.nbempire.android.tourguide.util.wikipedia.WikipediaConstants;

/**
 * Implementation of the {@link WikipediaService} interface.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class WikipediaServiceImpl implements WikipediaService {

    @Override
    public String getPageUrl(String pageTitle) {
        String transformedTitle = pageTitle.replace(' ', WikipediaConstants.URL_SPACE_REPLACEMENT);
        return WikipediaConstants.PAGE_URL_PREFFIX + transformedTitle;
    }
}
