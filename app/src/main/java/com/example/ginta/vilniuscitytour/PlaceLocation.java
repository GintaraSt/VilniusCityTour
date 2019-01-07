package com.example.ginta.vilniuscitytour;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlaceLocation {
    private String address;
    private Location placeLocation = new Location("");
    private static Location usersLocation = new Location("");
    /**
     * if location or location providers status will change, LocationListener will be notified
     */
    private static final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //if location changed - change current users location to new location
            usersLocation = location;
            Log.v("LocationListener: ", "Current Users Location - " + location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //if status changed to unavailable or temporary unavailable - notify user
            if(status == LocationProvider.OUT_OF_SERVICE){
                Toast toast = Toast.makeText(ActivityMain.getContext(), R.string.location_unavailable, Toast.LENGTH_SHORT);
                toast.show();
            } else if (status == LocationProvider.TEMPORARILY_UNAVAILABLE){
                Toast toast = Toast.makeText(ActivityMain.getContext(), R.string.location_temporary_unavailable, Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast toast = Toast.makeText(ActivityMain.getContext(), R.string.location_provider_enabled, Toast.LENGTH_SHORT);
            toast.show();
            Log.v("LocationListener: ", "Provider: " + provider + " - enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast toast = Toast.makeText(ActivityMain.getContext(), R.string.location_provider_disabled, Toast.LENGTH_SHORT);
            toast.show();
            Log.v("LocationListener: ", "Provider: " + provider + " - disabled");
        }
    };

    /**
     * requests users location updates - location will be updated every 15sec, or  every 50m location change
     * location updates will be sent to LocationListener locationListener
     */
    public static void useUsersLocation(){
        long LOCATION_REFRESH_TIME = 2000;
        float LOCATION_REFRESH_DISTANCE = 50;
        try{
            //get inaccurate location from network provider
            usersLocation = ActivityMain.getLocationManager().getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            //set  up a location listener for GPS provider, so location would became more accurate as soon as gps is ready
            ActivityMain.getLocationManager().requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, locationListener);
        } catch (SecurityException e){
            //notify user if Security exception thrown
            Toast toast = Toast.makeText(ActivityMain.getContext(), R.string.failed_to_get_location, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * PlaceLocation constructor
     * @param Latitude - latitude of place added to the list
     * @param Longitude - longitude of place added to the list
     */
    public PlaceLocation(double Latitude, double Longitude){
        placeLocation.setLatitude(Latitude);
        placeLocation.setLongitude(Longitude);
    }

    /**
     * get address of place
     * @return - Full address line of place
     */
    public String getAddress(){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(ActivityMain.getContext(), Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(placeLocation.getLatitude(), placeLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (IOException e){
            Log.w("Location", "Exception at getFromLocation()" + e);
        }
        Log.i("Location: ", "address is - " + address);
        return address;
    }

    public float getDistanceFromUser(){
        return usersLocation.distanceTo(placeLocation);
    }

    public Location getPlaceLocation(){return placeLocation;}


}
