package com.example.daniaapplication;

import android.content.Context;

public class Utilities {
    public static Integer imageToResId(Context context, String imagename) {
        if (isLong(imagename))
            return Integer.parseInt(imagename);
        return context.getResources().getIdentifier(imagename, "drawable", context.getPackageName());
    }
    public static boolean isLong(String str){
        try{
            Integer.parseInt(str);
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public static int songToResId(Context context, String file) {
        if (isLong(file))
            return Integer.parseInt(file);
        return context.getResources().getIdentifier(file, "raw", context.getPackageName());
    }
}
