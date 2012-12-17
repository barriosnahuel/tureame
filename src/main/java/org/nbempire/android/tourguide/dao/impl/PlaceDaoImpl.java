/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/12/12 at 9:34 PMhs.
 */
package org.nbempire.android.tourguide.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import org.nbempire.android.tourguide.dao.PlaceDao;
import org.nbempire.android.tourguide.domain.Place;
import org.nbempire.android.tourguide.domain.wikipedia.WikipediaPlace;
import org.nbempire.android.tourguide.service.WikipediaService;

/**
 * Implementation of the {@link PlaceDao} interface.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class PlaceDaoImpl implements PlaceDao {

    /**
     * Tag for class' log.
     */
    private static final String TAG = "PlaceDaoImpl";

    /**
     * Service for Wikipedia domain objects.
     */
    private WikipediaService wikipediaService;

    /**
     * Creates a PlaceDaoImpl with all its dependencies.
     *
     * @param aWikipediaService
     *         A {@link WikipediaService}.
     */
    public PlaceDaoImpl(WikipediaService aWikipediaService) {
        this.wikipediaService = aWikipediaService;
    }

    @Override
    public List<Place> findAllNearBy(double latitude, double longitude) {
        return transform(wikipediaService.queryGeosearch(latitude, longitude));
    }

    /**
     * Transform the specified array of {@link org.nbempire.android.tourguide.domain.wikipedia.WikipediaPlace}s into a List of domain {@link
     * org.nbempire.android.tourguide.domain.Place}s.
     *
     * @param wikipediaPlaces
     *         The WikipediaPlaces to transform.
     *
     * @return A list of domain places.
     */
    private List<Place> transform(WikipediaPlace[] wikipediaPlaces) {

        List<Place> places = new ArrayList<Place>();
        for (WikipediaPlace eachWikipediaPlace : wikipediaPlaces) {
            places.add(new Place(eachWikipediaPlace.getTitle(), eachWikipediaPlace.getLat(), eachWikipediaPlace.getLon()));
        }

        //  TODO : Functionality : Validate quantity.
        Log.d(TAG, "Transformed: " + places.size() + " places.");

        return places;
    }

}
