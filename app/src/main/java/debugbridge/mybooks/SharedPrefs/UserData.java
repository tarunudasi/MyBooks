package debugbridge.mybooks.SharedPrefs;

import android.content.Context;
import android.content.SharedPreferences;

import debugbridge.mybooks.Model.User;

public class UserData {

    private SharedPreferences preferences;
    private static UserData mInstance;
    private final static String NAME = "user";
    private final static String USER_NAME = "name";
    private final static String EMAIL = "email";
    private final static String LOGIN = "login";
    private final static String MOBILE = "mobile";
    private final static String VERIFIED = "verified";

    private UserData(Context context){
        this.preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserData getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserData(context);
        }
        return mInstance;
    }

    public void setLogin(User user){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_NAME, user.getName());
        editor.putString(EMAIL, user.getEmail());
        editor.putString(MOBILE, user.getMobile());
        editor.putString(VERIFIED, user.getVerified());
        editor.putBoolean(LOGIN, true);
        editor.commit();
    }

    public void setLogout(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public User getUser() {
        if (preferences.getBoolean(LOGIN, false)) {
            return new User(preferences.getString(EMAIL, null), preferences.getString(USER_NAME, null), preferences.getString(MOBILE, null), preferences.getString(VERIFIED, null));
        }else {
            return null;
        }
    }

    public boolean checkLogin(){
        return preferences.getBoolean(LOGIN, false);
    }

    public void changeMobile(String mobile){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(MOBILE, mobile);
        editor.commit();
    }

}
