package debugbridge.mybooks.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import debugbridge.mybooks.Model.Location;

public class LocationPrefs {

    private SharedPreferences preferences;
    private final static String NAME = "location";
    private final static String LATKEY = "latitude";
    private final static String LONGKEY = "longitude";
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

    public void setLocation(Location location){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(LATKEY, (float) location.getLatitude());
        editor.putFloat(LONGKEY, (float) location.getLongitude());
        editor.commit();
    }

    public Location getLocation(){
        return new Location(preferences.getFloat(LATKEY, 0.0f), preferences.getFloat(LONGKEY, 0.0f));
    }

    public void setDistance(int distance) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(DISTANCE, distance);
        editor.commit();
    }

    public int getDistance(){
        return preferences.getInt(DISTANCE, 5);
    }
}
