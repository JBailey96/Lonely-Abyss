package uk.ac.qub.eeecs.gage.engine.graphics;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by James on 10/04/2017.
 */

public class DrawAssist {
    //James Bailey 40156063
    //scale a string to a required width boundary, calculating an optimum text size
    public static Paint calculateTextSize(Paint paint, float width, String text) {
        float optimumTextSize = 60; //optimum text size - used to test the text's boundaries
        paint.setTextSize(optimumTextSize);

        Rect bounds = new Rect(); //the boundary for the text - smallest rectangle to contain all the characters in the string
        paint.getTextBounds(text, 0, text.length(), bounds); //calculate smallest rectangles

        float textSize = optimumTextSize * width/bounds.width(); //calculates text size that will fit into the boundary of the required width

        paint.setTextSize(textSize); //set the paint's text size to the calculated size
        return paint;
    }

    //James Bailey 40156063
    //rotate a bitmap x degrees clockwise
    public static Bitmap rotateBitmap(Bitmap bitmap, float rotateVal) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateVal);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
