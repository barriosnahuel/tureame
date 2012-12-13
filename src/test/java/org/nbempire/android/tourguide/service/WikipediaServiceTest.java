/*
 * Copyright (c) 2012 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/13/12 at 2:40 PMhs.
 */
package org.nbempire.android.tourguide.service;

import junit.framework.Assert;
import org.junit.Test;
import org.nbempire.android.tourguide.service.impl.WikipediaServiceImpl;
import org.nbempire.android.tourguide.util.wikipedia.WikipediaConstants;

/**
 * Test for the {@link WikipediaService}.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class WikipediaServiceTest {

    /**
     * Test method for getPageUrl.
     */
    @Test
    public void getPageUrl_validPageTitle_returnUrl() {
        WikipediaService wikipediaService = new WikipediaServiceImpl();

        String url = wikipediaService.getPageUrl("Un Titulo");
        Assert.assertNotNull(url);
        Assert.assertEquals(WikipediaConstants.PAGE_URL_PREFFIX + "Un_Titulo", url);
    }
}
