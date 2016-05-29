package dev.siniy.atmsearch;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Ryou on 5/28/2016.
 */
public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "THANHTRUNG";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_S_USERID = "s_userid";
    public static final String KEY_S_USER = "s_user";
    public static final String KEY_S_NAME = "s_name";
    public static final String KEY_S_PRIV = "s_priv";
    public static final String KEY_S_STT = "s_stt";

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String userid, String user, String name,
                                   String priv, String stt) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_S_USERID, userid);
        editor.putString(KEY_S_USER, user);
        editor.putString(KEY_S_NAME, name);
        editor.putString(KEY_S_PRIV, priv);
        editor.putString(KEY_S_STT, stt);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_S_USERID, pref.getString(KEY_S_USERID, null));
        user.put(KEY_S_USER, pref.getString(KEY_S_USER, null));
        user.put(KEY_S_NAME, pref.getString(KEY_S_NAME, null));
        user.put(KEY_S_PRIV, pref.getString(KEY_S_PRIV, null));
        user.put(KEY_S_STT, pref.getString(KEY_S_STT, null));
        return user;
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        context.startActivity(MainActivity.createIntent(context));
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
}
