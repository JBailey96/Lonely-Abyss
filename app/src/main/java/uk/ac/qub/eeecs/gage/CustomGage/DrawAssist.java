package uk.ac.qub.eeecs.gage.CustomGage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Toast;

import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by James on 10/04/2017.
 */

public class DrawAssist {
    //black bitmap drawn in the background
    protected static Bitmap blackBitmap;
    protected static Paint blackBitmapPaint;
    protected static Rect blackBitmapRect; //the position and dimensions to draw the black bitmap to
    protected static Toast messageObj = null;

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
        matrix.postRotate(90);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    //James Bailey 40156063
    //flip a bitmap horizontally
    public static Bitmap flipBitmapHorizontally(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1f, 1f);
        Bitmap flippedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return flippedBitmap;
    }

    //James Bailey 40156063
    //Draws a semi-transparent black bitmap background
    public static void drawBlackBackground(ScreenViewport mScreenViewport, IGraphics2D graphics2D) {
        if (blackBitmapRect == null) {
            blackBitmapRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height); //set black bitmap to fill whole screenviewport
        }
        if (blackBitmap == null) {
            //creates the black bitmap
            blackBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            blackBitmap.eraseColor(Color.BLACK);
        }
        if (blackBitmapPaint == null) {
            blackBitmapPaint = new Paint();
            blackBitmapPaint.setAlpha(200); //make the background behind the hand card semi-opaque
        }

        graphics2D.drawBitmap(blackBitmap, null, blackBitmapRect, blackBitmapPaint);
    }

    //James Bailey 40156063
    //Display a toast message
    //Used to assist the player in using the game's features.
    public static void showMessage(final Game mGame, final String message) {
        mGame.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageObj = Toast.makeText(mGame.getActivity(), message, Toast.LENGTH_LONG);
                messageObj.show();
            }
        });
    }

    //James Bailey 40156063
    //Clear a toast message that is currently displaying.
    public static void clearMessage() {
        messageObj.cancel();
    }


}
