package com.example.daniaapplication;

import android.content.Context;

public class Utilities {
    public static Integer imageToResId(Context context, String imagename) {
        return context.getResources().getIdentifier(imagename, "drawable", context.getPackageName());

    }
}
