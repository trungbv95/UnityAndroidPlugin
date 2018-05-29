package com.androidplugin.unity.moon.unityandroidplugin2;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

public class UnityPath
{
    public static final String SD_CARD = "sdCard";
    public static final String EXTERNAL_SD_CARD = "externalSdCard";
    private static final String ENV_SECONDARY_STORAGE = "SECONDARY_STORAGE";

    public static String[] getAllStorageLocations(Context context) {
        File[] strSDCardPath = ContextCompat.getExternalCacheDirs(context);
        Log.d("Unity", "System.getenv(ENV_SECONDARY_STORAGE): " + strSDCardPath.toString());
        if(strSDCardPath != null){
            String[] storagePath = new String[strSDCardPath.length];
            for (int i = 0; i < strSDCardPath.length; i++){
                Log.d("Unity", strSDCardPath[i].toString());
                storagePath[i] = strSDCardPath[i].toString();
            }
            return storagePath;
        }
        return null;
    }

    public static String GetExternalStorage(Context context)
    {
        String[] arrayPath = getAllStorageLocations(context);
        if(arrayPath != null)
            return arrayPath[1];
        return null;
    }

}
