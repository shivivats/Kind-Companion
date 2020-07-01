package com.shivivats.kindcompanion;

import android.net.Uri;

import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public static String fromUri(Uri uri) {
        if (uri == null) {
            return null;
        } else {
            return uri.toString();
        }
    }

    @TypeConverter
    public static Uri stringToUri(String string) {
        if (string == null) {
            return null;
        } else {
            return Uri.parse(string);
        }
    }
}
