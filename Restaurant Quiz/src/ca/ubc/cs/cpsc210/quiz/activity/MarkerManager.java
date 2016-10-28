package ca.ubc.cs.cpsc210.quiz.activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;

import java.util.*;

/**
 * Created by pcarter on 14-11-06.
 *
 * Manager for markers plotted on map
 */
public class MarkerManager {
    private GoogleMap map;
    private List<Marker> visibleMarkers;
    private Marker marker;


    /**
     * Constructor initializes manager with map for which markers are to be managed.
     *
     * @param map the map for which markers are to be managed
     */
    public MarkerManager(GoogleMap map) {
        this.map = map;
        this.visibleMarkers = new ArrayList<Marker>();
    }

    /**
     * Get last marker added to map
     *
     * @return last marker added
     */
    public Marker getLastMarker() {
        return visibleMarkers.get(visibleMarkers.size() - 1);
    }

    /**
     * Add green marker to show position of restaurant
     *
     * @param point the point at which to add the marker
     * @param title the marker's title
     */
    public void addRestaurantMarker(LatLng point, String title) {

        marker = map.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        visibleMarkers.add(marker);

    }

    /**
     * Add a marker to mark latest guess from user.  Only the most recent two positions selected
     * by the user are marked.  The distance from the restaurant is used to create the marker's title
     * of the form "xxxx m away" where xxxx is the distance from the restaurant in metres (truncated to
     * an integer).
     * <p/>
     * The colour of the marker is based on the distance from the restaurant:
     * - red, if the distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param latLng the latitude and longitude of the marker guess position
     * @param distance distance away from restaurant
     */
    public void addMarker(LatLng latLng, double distance) {

        int integerDistance = (int) distance;
        marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(integerDistance + "meters away")
                .icon(BitmapDescriptorFactory.defaultMarker(this.getColour(distance))));
        visibleMarkers.add(marker);

        for (int i = 0; i < visibleMarkers.size() - 2; i++) {
            this.visibleMarkers.get(i).remove();
        }
    }

    /**
     * Remove markers that mark user guesses from the map
     */
    public void removeMarkers() {
        for (Marker visibleMarker : visibleMarkers) {
            visibleMarker.remove();
        }
    }

    /**
     * Produce a colour on a linear scale between red and green based on distance:
     * <p/>
     * - red, if distance is 3km or more
     * - somewhere between red (at 3km) and green (at 0m) (on a linear scale) for other distances
     *
     * @param distance distance from restaurant
     * @return colour of marker
     */

    private float getColour(double distance) {
        if (distance > 3000) {
            return BitmapDescriptorFactory.HUE_RED;
        }

        if ((distance >= 50) && (distance < 3000)) {
            double scale = ((3000 - distance) / 3000);
            float fscale = (float) scale;
            return (BitmapDescriptorFactory.HUE_GREEN + BitmapDescriptorFactory.HUE_RED) * fscale;
        }
            else
            return BitmapDescriptorFactory.HUE_GREEN;
    }

}