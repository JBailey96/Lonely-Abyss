package uk.ac.qub.eeecs.gage.CustomGage;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import uk.ac.qub.eeecs.gage.engine.AssetStore;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.engine.input.TouchHandler;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Initial starter for a release button.
 * <p>
 * Note: This is an incomplete class. It is assumed you will complete/refine the functionality.
 * <p>
 * Important: Current this button is assumed to be defined in screen space (not world space)
 */
public class ReleaseButton extends GameObject {

    ///////////////////////////////////////////////////////////////////////////
    // Class data: PushButton look and sound                                 //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Name of the graphical asset used to represent the default button state
     */
    protected Bitmap mDefaultBitmap;

    /**
     * Name of the graphical asset used to represent the pushed button state
     */
    protected Bitmap mPushBitmap;

    /**
     * Name of the sound asset to be played whenever the button is clicked
     */
    protected Sound mReleaseSound;

    protected Bitmap glowEffect;
    protected Rect glowEffectDimen;
    protected boolean enableGlow;


    ///////////////////////////////////////////////////////////////////////////
    // Constructors                                                          //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Create a new release button.
     *
     * @param x             Centre y location of the button
     * @param y             Centre x location of the button
     * @param width         Width of the button
     * @param height        Height of the button
     * @param defaultBitmap Bitmap used to represent this control
     * @param pushBitmap    Bitmap used to represent this control
     * @param releaseSound  Bitmap used to represent this control
     * @param gameScreen    Gamescreen to which this control belongs
     */
    public ReleaseButton(float x, float y, float width, float height,
                         String defaultBitmap,
                         String pushBitmap,
                         String releaseSound,
                         GameScreen gameScreen) {
        super(x, y, width, height,
                gameScreen.getGame().getAssetManager().getBitmap(defaultBitmap), gameScreen);

        //load the bitmap for glow
        AssetStore assetStore = gameScreen.getGame().getAssetManager();
        assetStore.loadAndAddBitmap("GLOW", "img/Particles/gloweffect.png");

        //set the bitmap glow effect and its dimensions (surrounds the card)
        glowEffect = assetStore.getBitmap("GLOW");
        glowEffectDimen = new Rect((int) (x-mBound.halfWidth-15), (int) (y-mBound.halfHeight-15), (int) (x+mBound.halfWidth+15), (int) (y+mBound.halfHeight+15));

        mDefaultBitmap = assetStore.getBitmap(defaultBitmap);
        mPushBitmap = assetStore.getBitmap(pushBitmap);

        mReleaseSound = (releaseSound == null) ? null : assetStore.getSound(releaseSound);
        this.enableGlow = true;
    }

    /**
     * Create a new release button.
     *
     * @param x             Centre y location of the button
     * @param y             Centre x location of the button
     * @param width         Width of the button
     * @param height        Height of the button
     * @param defaultBitmap Bitmap used to represent this control
     * @param pushBitmap    Bitmap used to represent this control
     * @param gameScreen    Gamescreen to which this control belongs
     */
    public ReleaseButton(float x, float y, float width, float height,
                         String defaultBitmap,
                         String pushBitmap,
                         GameScreen gameScreen) {
        this(x, y, width, height, defaultBitmap, pushBitmap, null, gameScreen);
    }


    // /////////////////////////////////////////////////////////////////////////
    // Methods
    // /////////////////////////////////////////////////////////////////////////


    private boolean mPushTriggered;
    private boolean mIsPushed;

    /**
     * Update the button
     *
     * @param elapsedTime Elapsed time information
     */
    public void update(ElapsedTime elapsedTime) {
        // Consider any touch events occurring in this update
        Input input = mGameScreen.getGame().getInput();
        BoundingBox bound = getBound(); //


        // Check for a press release on this button
        for (TouchEvent touchEvent : input.getTouchEvents()) {
            if (touchEvent.type == TouchEvent.TOUCH_UP
                    && bound.contains(touchEvent.x, touchEvent.y)) {
                // A touch up has occurred in this control
                mPushTriggered = true;
                // TODO: Also play sound here if it's available.
                return;
            }
        }

        // Check if any of the touch events were on this control
        for (int idx = 0; idx < TouchHandler.MAX_TOUCHPOINTS; idx++) {
            if (input.existsTouch(idx)) {
                if (bound.contains(input.getTouchX(idx), input.getTouchY(idx))) {
                    if (!mIsPushed) {
                        mBitmap = mPushBitmap;
                        mIsPushed = true;
                    }

                    return;
                }
            }
        }

        // If we have not returned by this point, then there is no touch event on the button
        if (mIsPushed) {
            mBitmap = mDefaultBitmap;
            mIsPushed = false;
        }
    }

    public boolean isEnableGlow() {
        return enableGlow;
    }

    public void setEnableGlow(boolean enableGlow) {
        this.enableGlow = enableGlow;
    }



    /**
     * Return true if the button has been triggered.
     * <p>
     * Note: This method will return true once, and only once, per push event.
     *
     * @return True if there has been an unconsumed push event, false otherwise.
     */
    public boolean pushTriggered() {
        if (mPushTriggered) {
            mPushTriggered = false;
            return true;
        }
        return false;
    }

    /**
     * Return the current state of the button.
     *
     * @return True if the button is pushed, otherwise false.
     */
    public boolean isPushed() {
        return mIsPushed;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * uk.ac.qub.eeecs.gage.world.GameObject#draw(uk.ac.qub.eeecs.gage.engine
     * .ElapsedTime, uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D,
     * uk.ac.qub.eeecs.gage.world.LayerViewport,
     * uk.ac.qub.eeecs.gage.world.ScreenViewport)
     */
    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D,
                     LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Assumed to be in screen space so just draw the whole thing
        drawScreenRect.set((int) (position.x - mBound.halfWidth),
                (int) (position.y - mBound.halfHeight),
                (int) (position.x + mBound.halfWidth),
                (int) (position.y + mBound.halfHeight));


        if (mIsPushed && enableGlow) { //validates whether the button has been touched
            drawGlow(graphics2D);
        }

        graphics2D.drawBitmap(mBitmap, null, drawScreenRect, null);
    }

    //James Bailey 40156063
    //draw the glow effect
    private void drawGlow(IGraphics2D graphics2D) {
        graphics2D.drawBitmap(glowEffect, null, glowEffectDimen, null);
    }


    public void setmIsPushed(boolean mIsPushed) {
        this.mIsPushed = mIsPushed;
    }
}
