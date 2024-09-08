package com.stdi;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static SharedPreferences sharedPreferencesAd;
    private static SharedPreferences.Editor Ad_editor;
    private static final String ADTime = "prefsAdTimes";
    private static final String Last_Time_AD_Show = "Last_Time_AD_Show";


    public static void init(Context context){
        sharedPreferencesAd = context.getSharedPreferences(ADTime, Context.MODE_PRIVATE);
        Ad_editor = sharedPreferencesAd.edit();
    }

   /* public static void setIsAdsEnable(boolean flag){
        Ad_editor = sharedPreferencesAd.edit();
        Ad_editor.putBoolean(IS_ADS_ENABLE, flag);
        Ad_editor.commit();
    }

    public static boolean getIsAdsEnable(){
        return sharedPreferencesAd.getBoolean(IS_ADS_ENABLE, false);
    }*/

    public static void setLastAdTime(Long adTime){
        Ad_editor = sharedPreferencesAd.edit();
        Ad_editor.putLong(Last_Time_AD_Show, adTime);
        Ad_editor.commit();
    }

    public static Long getLasttimewithAdtime(){
        Long Adtimewithsecond = sharedPreferencesAd.getLong(Last_Time_AD_Show, 0L);
        return Adtimewithsecond;
    }
}
