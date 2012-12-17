/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/12/12 at 9:31 PMhs.
 */
package org.nbempire.android.tourguide.dao;

import java.util.List;

import org.nbempire.android.tourguide.domain.Place;

/**
 * DAO for the {@link Place} entity.
 *
 * @author Nahuel Barrios.
 * @since 1
 */
public interface PlaceDao {

    /**
     * Find all places next to the specified location ({@code latitude}, {@code longitude}).
     *
     * @param latitude
     *         Location's latitude.
     * @param longitude
     *         Location's longitude.
     *
     * @return List of places found.
     */
    List<Place> findAllNearBy(double latitude, double longitude);
}
