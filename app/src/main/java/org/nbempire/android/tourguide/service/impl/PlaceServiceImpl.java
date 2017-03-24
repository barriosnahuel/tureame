/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/12/12 at 9:23 PMhs.
 */
package org.nbempire.android.tourguide.service.impl;

import java.util.List;

import org.nbempire.android.tourguide.dao.PlaceDao;
import org.nbempire.android.tourguide.domain.Place;
import org.nbempire.android.tourguide.service.PlaceService;

/**
 * Implementation of the {@link PlaceService} interface.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public class PlaceServiceImpl implements PlaceService {

    /**
     * DAO for the {@link Place} entity.
     */
    private PlaceDao placeDao;

    /**
     * Constructor for this Service that needs a PlaceDao to handle Data Access.
     *
     * @param placeDao
     *         DAO for the {@link Place} entity.
     */
    public PlaceServiceImpl(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    @Override
    public List<Place> findAllNearBy(double latitude, double longitude) {
        //  TODO : Functionality : Add validation about numeric limits over latitude and longitude.
        return placeDao.findAllNearBy(latitude, longitude);
    }
}
