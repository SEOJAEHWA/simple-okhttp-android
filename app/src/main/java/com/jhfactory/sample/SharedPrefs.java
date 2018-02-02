package com.jhfactory.sample;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SharedPrefs {

    /**
     *
     * @param context context
     * @return return SharedPreferences object
     */
    public static SharedPreferences getDefault(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean getDefaultPrefBoolean(Context context, String key, boolean defaultValue) {
        return getDefault(context).getBoolean(key, defaultValue);
    }

    public static String getDefaultPrefString(Context context, String key, String defaultValue) {
        return getDefault(context).getString(key, defaultValue);
    }

    public static int getDefaultPrefInt(Context context, String key, int defaultValue) {
        return getDefault(context).getInt(key, defaultValue);
    }

    public static float getDefaultPrefFloat(Context context, String key, float defaultValue) {
        return getDefault(context).getFloat(key, defaultValue);
    }

    public static long getDefaultPrefLong(Context context, String key, long defaultValue) {
        return getDefault(context).getLong(key, defaultValue);
    }

    public static Set<String> getDefaultPrefStringSet(Context context, String key, Set<String> defaultValue) {
        return getDefault(context).getStringSet(key, defaultValue);
    }

    public static Map<String, ?> getAll(Context context) {
        return getDefault(context).getAll();
    }

    public static boolean putDefaultPrefString(Context context, String key, String val) {
        return getDefault(context).edit().putString(key, val).commit();
    }

    public static boolean putDefaultPrefBoolean(Context context, String key, boolean val) {
        return getDefault(context).edit().putBoolean(key, val).commit();
    }

    public static boolean putDefaultPrefFloat(Context context, String key, float val) {
        return getDefault(context).edit().putFloat(key, val).commit();
    }

    public static boolean putDefaultPrefInt(Context context, String key, int val) {
        return getDefault(context).edit().putInt(key, val).commit();
    }

    public static boolean putDefaultPrefLong(Context context, String key, long val) {
        return getDefault(context).edit().putLong(key, val).commit();
    }

    public static boolean putDefaultPrefStringSet(Context context, String key, Set<String> val) {
        return getDefault(context).edit().putStringSet(key, val).commit();
    }

    /**
     *
     * @param context context
     * @param pName preferenceName
     * @return return SharedPreferences object
     */
    public static SharedPreferences getPref(Context context, String pName) {
        return context.getSharedPreferences(pName, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String pName, String key) {
        return getString(context, pName, key, null);
    }

    public static String getString(Context context, String pName, String key, String defVal) {
        return getPref(context, pName).getString(key, defVal);
    }

    public static boolean getBoolean(Context context, String pName, String key) {
        return getBoolean(context, pName, key, false);
    }

    public static boolean getBoolean(Context context, String pName, String key, boolean defVal) {
        return getPref(context, pName).getBoolean(key, defVal);
    }

    public static float getFloat(Context context, String pName, String key) {
        return getFloat(context, pName, key, 0f);
    }

    public static float getFloat(Context context, String pName, String key, float defVal) {
        return getPref(context, pName).getFloat(key, defVal);
    }

    public static int getInt(Context context, String pName, String key) {
        return getInt(context, pName, key, 0);
    }

    public static int getInt(Context context, String pName, String key, int defVal) {
        return getPref(context, pName).getInt(key, defVal);
    }

    public static long getLong(Context context, String pName, String key) {
        return getLong(context, pName, key, 0);
    }

    public static long getLong(Context context, String pName, String key, long defVal) {
        return getPref(context, pName).getLong(key, defVal);
    }

    public static Set<String> getStringSet(Context context, String pName, String key) {
        return getStringSet(context, pName, key, new HashSet<String>());
    }

    public static Set<String> getStringSet(Context context, String pName, String key,
                                           HashSet<String> defVal) {
        return getPref(context, pName).getStringSet(key, defVal);
    }

    public static Map<String, ?> getAll(Context context, String pName) {
        return getPref(context, pName).getAll();
    }

    public static boolean contains(Context context, String pName, String key) {
        return getPref(context, pName).contains(key);
    }

    public static SharedPreferences.Editor getPrefEditor(Context context, String pName) {
        return getPref(context, pName).edit();
    }

    public static boolean putString(Context context, String pName, String key, String val) {
        return getPrefEditor(context, pName).putString(key, val).commit();
    }

    public static boolean putBoolean(Context context, String pName, String key, boolean val) {
        return getPrefEditor(context, pName).putBoolean(key, val).commit();
    }

    public static boolean putFloat(Context context, String pName, String key, float val) {
        return getPrefEditor(context, pName).putFloat(key, val).commit();
    }

    public static boolean putInt(Context context, String pName, String key, int val) {
        return getPrefEditor(context, pName).putInt(key, val).commit();
    }

    public static boolean putLong(Context context, String pName, String key, long val) {
        return getPrefEditor(context, pName).putLong(key, val).commit();
    }

    public static boolean putStringSet(Context context, String pName, String key, Set<String> val) {
        return getPrefEditor(context, pName).putStringSet(key, val).commit();
    }

    public static boolean remove(Context context, String pName, String key) {
        return getPref(context, pName).edit().remove(key).commit();
    }

    public static boolean clear(Context context, String pName) {
        return getPref(context, pName).edit().clear().commit();
    }
}
