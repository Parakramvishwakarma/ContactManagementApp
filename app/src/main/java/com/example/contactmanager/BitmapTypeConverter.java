package com.example.contactmanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class BitmapTypeConverter {

    /* -----------------------------------------------------------------------------------------
              Function: fromBitmap()
              Author: Ryan
              Description: Converts the bitmap to a byte array
       ---------------------------------------------------------------------------------------- */
    @TypeConverter
    public static byte[] fromBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    /* -----------------------------------------------------------------------------------------
              Function: toBitmap()
              Author: Ryan
              Description: Converts the byte array to a bitmap
       ---------------------------------------------------------------------------------------- */
    @TypeConverter
    public static Bitmap toBitmap(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
}
