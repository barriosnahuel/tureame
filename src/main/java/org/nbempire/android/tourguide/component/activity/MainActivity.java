/*
 * Copyright (c) 2012-2013 Nahuel Barrios <barrios.nahuel@gmail.com>.
 * No se reconocerá ningún tipo de garantía.
 */

/**
 * Created by: Nahuel Barrios.
 * On: 12/20/12 at 4:51 PMhs.
 */
package org.nbempire.android.tourguide.component.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.nbempire.android.tourguide.R;
import org.nbempire.android.tourguide.dao.impl.PlaceDaoImpl;
import org.nbempire.android.tourguide.dao.impl.WikipediaDaoImplSpring;
import org.nbempire.android.tourguide.domain.Place;
import org.nbempire.android.tourguide.service.PlaceService;
import org.nbempire.android.tourguide.service.impl.PlaceServiceImpl;
import org.nbempire.android.tourguide.service.impl.WikipediaServiceImpl;

/**
 * TODO : Javadoc for MainActivity
 *
 * @author Nahuel Barrios.
 * @since 3
 */
public class MainActivity extends Activity {

    private PlaceService placeService;

    private String[] placesArray;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        placesArray = new String[]{};
        placeService = new PlaceServiceImpl(new PlaceDaoImpl(new WikipediaServiceImpl(new WikipediaDaoImplSpring())));
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchPlacesNearCurrentLocation(null);
    }

    /**
     * @param callerView
     */
    public void searchPlacesNearCurrentLocation(View callerView) {
        //new LocationManager().requestSingleUpdate(LocationManager.GPS_PROVIDER, PendingIntent.);

        List<Place> places = placeService.findAllNearBy(34.603736, 58.381866);

        List<String> placesTitle = new ArrayList<String>();
        for (Place eachPlace : places) {
            placesTitle.add(eachPlace.getTitle());
        }

        placesArray = placesTitle.toArray(placesArray);

        ListView placesListView = (ListView) findViewById(R.id.activity_main_placesListView);
        placesListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, placesArray));
    }
}