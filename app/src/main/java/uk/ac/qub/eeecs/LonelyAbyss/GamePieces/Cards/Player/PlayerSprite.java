package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player;

import android.graphics.Bitmap;

import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.CustomGage.DrawAssist;
import uk.ac.qub.eeecs.gage.world.GameObject;
import uk.ac.qub.eeecs.gage.world.GameScreen;

/**
 * Created by James on 28/04/2017.
 */

public class PlayerSprite extends GameObject {

    public enum MovementDirection { //the types of directions the player can move the sprite across the gamescreen
        UP, DOWN, LEFT, RIGHT
    }

    protected MovementDirection movementDirection; //the direction of movement of the player sprite

    public final int numMoveSteps = 10; //num of movements to have before reaching its destination.

    private Bitmap playerRestBitmap; //the bitmap the player sprite has when not moving
    private Bitmap[] playerMovementXRightBitmaps; //the bitmaps that are used to animate movement when moving on the x axis (left or right).
    private Bitmap[] playerMovementXLeftBitmaps;
    private Bitmap[] playerMovementYUpBitmaps;
    private Bitmap[] playerMovementYDownBitmaps; //bitmaps used when moving on the y axis (up or down)
    private Bitmap[] bitmapsToAnimate; //references the currently animated bitmaps.


    private int playCount; //the number of animation bitmaps to draw.
    private int currentFrame; //the current frame of animation to draw.
    private long timeWaitEachStep; //the length of time to animate the sprite with bitmaps each step
    private long startTime; //the start time of the bitmap animation

    private float destX; //the final x position that the sprite is moved to.
    private float destY; //final y position

    private long timeToMove; //the total time to move the sprite (in milliseconds) to the destination x and y positions.

    private float xStepping; //the x movement to make each movement step
    private float yStepping; //the y movement to make each movement step

    private float currentSteps; //the movement steps currently made

    public PlayerSprite(float x, float y, float width, float height, GameScreen gameScreen, Bitmap playerRestBitmap, Bitmap[] playerMovementXBitmaps, Bitmap[] playerMovementYBitmaps) {
        super(x, y, width, height, playerRestBitmap, gameScreen);

        this.playerRestBitmap = playerRestBitmap;
        this.playerMovementXLeftBitmaps = playerMovementXBitmaps;
        this.playerMovementXRightBitmaps = generateMoveXRightBitmaps();
        this.playerMovementYDownBitmaps = playerMovementYBitmaps;
        this.playerMovementYUpBitmaps = generateMoveYUpBitmaps();

        this.playCount = 0;
        this.currentFrame = 0;
        this.startTime = -1;
        this.timeWaitEachStep = 0;
        this.currentSteps = 0;
    }

    public void update(ElapsedTime elapsedTime) {
        determineSpritePosition();
        determineBitmap();
    }

    //James Bailey 40156063
    //Sets the current bitmap to be drawn
    public void determineBitmap() {
        long currentTime = System.currentTimeMillis();

        if (this.startTime == -1) { //validates whether start time has not been initialised
            this.startTime = currentTime;
        }

        if (playCount > 0) { //validates whether there is to bitmaps to be drawn during an animation process
            if ((currentTime - this.startTime) > playCount * timeWaitEachStep) { //validates whether the time for processing is over.
                handleBitmapAnimationOver();
            }
        }

        if (playCount == 0) {  //validates whether there is no animation process.
            return;
        } else {
            determineCurrentBitmap(currentTime);
        }

        this.mBitmap = bitmapsToAnimate[currentFrame]; //set the bitmap to be drawn.

        //time to wait for each step of the animation.
        try {
            Thread.sleep(timeWaitEachStep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //James Bailey 40156063
    //Method that handles the end of a bitmap movement animation.
    private void handleBitmapAnimationOver() {
        playCount = 0; //no more bitmaps to be drawn for the animation process
        this.mBitmap = playerRestBitmap; //set the bitmap to the rest bitmap - player sprite is no longer shown to be moving.
    }

    //James Bailey 40156063
    //Method that sets the current bitmap to be drawn as part of the movement animation.
    private void determineCurrentBitmap(long currentTime) {
        long timeIntoAnimationPeriod = (currentTime - this.startTime) % timeWaitEachStep; //calculates the time into the animation period.
        //calculates the bitmap index in the list of animations bitmaps to be drawn
        this.currentFrame = (int) (timeIntoAnimationPeriod * (long) bitmapsToAnimate.length / timeWaitEachStep);
    }

    //James Bailey 40156063
    //Method that moves the player sprite to a destination x and y on the PlayScreen.
    public void moveSprite(float destX, float destY, long timeToMove, MovementDirection movementDirection) {
        //set the position x and y to move the player sprite to.
        this.destX = destX;
        this.destY = destY;

        //set the total time to move the player sprite.
        this.timeToMove = timeToMove;

        this.currentSteps = numMoveSteps;
        this.playCount = numMoveSteps;

        this.startTime = -1;

        //set the direction the player sprite is moving to.
        this.movementDirection = movementDirection;

        setBitmapsToAnimate();
        calculateStepping();
    }

    //James Bailey 40156063
    //Sets the array of bitmaps that are used to animate the player sprite's movement
    public void setBitmapsToAnimate() {
        if (this.movementDirection == MovementDirection.LEFT) {
            this.bitmapsToAnimate = this.playerMovementXLeftBitmaps;
        } else if (this.movementDirection == MovementDirection.RIGHT) {
            this.bitmapsToAnimate = this.playerMovementXRightBitmaps;
        } else if (this.movementDirection == MovementDirection.UP) {
            this.bitmapsToAnimate = this.playerMovementYUpBitmaps;
        } else if (this.movementDirection == MovementDirection.DOWN) {
            this.bitmapsToAnimate = this.playerMovementYDownBitmaps;
        }
    }

    //James Bailey 40156063
    //Calculates the difference between the initial x, y and the destination x, y and what quantity to move each step.
    public void calculateStepping() {
        float xDifference = this.destX-getPosition().x;
        float yDifference = this.destY-getPosition().y;

        //set the steppings by the number of move steps.
        this.xStepping = xDifference / this.numMoveSteps;
        this.yStepping =  yDifference/ this.numMoveSteps;

        //set the time per step.
        this.timeWaitEachStep = timeToMove / numMoveSteps;
    }

    //James Bailey 40156063
    //Sets the new x, y position of the player sprite.
    public void determineSpritePosition() {
        if (currentSteps == 0) { //validates whether there is an move process
            return;
        }

        //calculate the new x and y to move the sprite to in this step.
        float newX = getPosition().x + this.xStepping;
        float newY = getPosition().y + this.yStepping;

        currentSteps--; //a movement step has been made, reduce the remaining steps to be made.

        setPosition(mGameScreen, newX, newY, mBound.getWidth(), mBound.getHeight()); //set the new position of the sprite.
    }

    //James Bailey 40156063
    //generates the player's movement right on the x axis bitmaps
    //iterate through the list of bitmaps to animate and flip them
    //provides the perception the player sprite is moving right.
    private Bitmap[] generateMoveXRightBitmaps() {
        final int numberOfPlayerMoveXBitmaps = 4;
        Bitmap[] playerMovementXRightBitmaps = new Bitmap[numberOfPlayerMoveXBitmaps];

        for (int i = 0; i < numberOfPlayerMoveXBitmaps; i++) {
            playerMovementXRightBitmaps[i] = DrawAssist.flipBitmapHorizontally(playerMovementXLeftBitmaps[i]);
        }
        return playerMovementXRightBitmaps;
    }

    //James Bailey 40156063
    //generates the player's movement up on the y axis bitmaps
    //rotate the bitmaps 180 degrees, gives the perception the player sprite is walking upwards.
    //iterate through the list of bitmaps to animate and rotate them
    private Bitmap[] generateMoveYUpBitmaps() {
        final int numberOfPlayerMoveYBitmaps = 2;
        Bitmap[] playerMovementYUpBitmaps = new Bitmap[numberOfPlayerMoveYBitmaps];

        final float moveTranslateDegree = 180;

        for (int i = 0; i < numberOfPlayerMoveYBitmaps; i++) {
            playerMovementYUpBitmaps[i] = DrawAssist.rotateBitmap(playerMovementYDownBitmaps[i], moveTranslateDegree);
        }

        return playerMovementYUpBitmaps;
    }

}
