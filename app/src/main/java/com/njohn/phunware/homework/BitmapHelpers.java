package com.njohn.phunware.homework;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

/**
 * Created by njohn on 10/9/16.
 */

public class BitmapHelpers {
    public static RoundedBitmapDrawable createRoundedBitmapDrawable(Resources resources, Bitmap original){
        Bitmap bitmap = Bitmap.createScaledBitmap(original, 300, 300 * (original.getWidth()/original.getHeight()), true);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        // Calculate the bitmap radius
        int bitmapRadius = Math.min(bitmapWidth,bitmapHeight)/2;
        int bitmapSquareWidth = Math.min(bitmapWidth,bitmapHeight);

        //Initializing a new empty bitmap. Set the bitmap size from source bitmap
        Bitmap roundedBitmap = Bitmap.createBitmap(bitmapSquareWidth, bitmapSquareWidth, Bitmap.Config.ARGB_8888);

        // Initialize a new Canvas to draw empty bitmap
        Canvas canvas = new Canvas(roundedBitmap);

        // Draw a solid color to canvas
        canvas.drawColor(Color.BLACK);

        // Calculation to draw bitmap at the circular bitmap center position
        int x = bitmapSquareWidth - bitmapWidth;
        int y = bitmapSquareWidth - bitmapHeight;

        //Now draw the bitmap to canvas. Bitmap will draw its center to circular bitmap center by keeping border spaces
        canvas.drawBitmap(bitmap, x, y, null);

        // Create a new RoundedBitmapDrawable
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(resources,roundedBitmap);

        // Set the corner radius of the bitmap drawable
        roundedBitmapDrawable.setCornerRadius(bitmapRadius);
        //Set anti aliasing to true. There could be a performance hit
        roundedBitmapDrawable.setAntiAlias(true);

        // Return the RoundedBitmapDrawable
        return roundedBitmapDrawable;
    }
}
