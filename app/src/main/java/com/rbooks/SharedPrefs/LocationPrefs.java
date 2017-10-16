package com.rbooks.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.rbooks.Model.LocationModel;

public class LocationPrefs {

    private SharedPreferences preferences;
    private final static String NAME = "location";
    private final static String LATKEY = "latitude";
    private final static String LONGKEY = "longitude";
    private final static String CITY = "city";
    private final static String STATE = "state";
    private final static String COUNTRY = "country";
    private final static String DISTANCE = "distance";
    private static LocationPrefs mInstance;

    private LocationPrefs(Context context){
        this.preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static synchronized LocationPrefs getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocationPrefs(context);
        }
        return mInstance;
    }

    public void setLocation(LocationModel locationModel){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(LATKEY, (float) locationModel.getLatitude());
        editor.putFloat(LONGKEY, (float) locationModel.getLongitude());
        editor.commit();
    }

    public void setCity(LocationModel locationModel){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CITY, locationModel.getCity());
        editor.putString(STATE, locationModel.getState());
        editor.putString(COUNTRY, locationModel.getCountry());
        editor.commit();
    }

    public LocationModel getCity(){
        return new LocationModel(preferences.getString(CITY, null), preferences.getString(STATE, null), preferences.getString(COUNTRY, null));
    }

    public LocationModel getLocation(){
        return new LocationModel(preferences.getFloat(LATKEY, 0.0f), preferences.getFloat(LONGKEY, 0.0f));
    }

    public void setDistance(int distance) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(DISTANCE, distance);
        editor.commit();
    }

    public int getDistance(){
        return preferences.getInt(DISTANCE, 5);
    }

    public void clearLocation(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
