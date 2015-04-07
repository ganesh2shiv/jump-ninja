package com.jumpninja.game.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Utils {

	private static SharedPreferences mAppPreferences;
	private static Editor mEditor;

	public static void addPreferenceBoolean(Context context, String pref_field, Boolean pref_value){
		mAppPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mEditor = mAppPreferences.edit();
		mEditor.putBoolean(pref_field, pref_value);
		mEditor.apply();
	}

	public static void addPreferenceInt(Context context, String pref_field, int pref_value){
		mAppPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		mEditor = mAppPreferences.edit();
		mEditor.putInt(pref_field, pref_value);
		mEditor.apply();
	}

	public static int getPreferenceInt(Context context, String pref_field, int def_value){
		mAppPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mAppPreferences.getInt(pref_field, def_value);
	}

	public static boolean getPreferenceBoolean(Context context, String pref_field, Boolean def_value){
		mAppPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return mAppPreferences.getBoolean(pref_field, def_value);
	}
}
