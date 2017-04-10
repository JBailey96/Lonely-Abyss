package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.DrawAssist;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.ReleaseButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.world.State;

/**
 * Created by James on 08/03/2017.
 */

public class ActiveUnimonState extends State {
    protected final int numMoveButtons = 3; //the number of move buttons

    private UnimonCard activeCard;  //player's current active UnimonCard
    private UnimonMoves[] activeCardMoves; //the moves of the active card

    //Battle Option Buttons
    private ReleaseButton applyEnergyButton;
    private ReleaseButton evolveButton;
    private ReleaseButton retreatButton;

    //Move battle options
    List<ReleaseButton> moveButtons = new ArrayList<>();

    private static Paint moveTextPaint; //the text used for the moves

    protected Bitmap blackBitmap; //black background
    protected Paint blackBitmapPaint;

    public ActiveUnimonState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        loadButtonOptionBitmaps();
        generateBattleOptions();
        loadActiveCard(battleSetup);
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (active) {
            activeCard.update(elapsedTime);
            updateButtons(elapsedTime);
            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();
            touchButton(touchEvents);
        }
    }

    public void updateButtons(ElapsedTime elapsedTime) {
        applyEnergyButton.update(elapsedTime);
        evolveButton.update(elapsedTime);
        retreatButton.update(elapsedTime);

        for (ReleaseButton moveButton: moveButtons) {
            moveButton.update(elapsedTime);
        }
    }

    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                touchActiveUnimon(t);
            }
        }
    }

    //James Bailey 40156063
    //checks whether the active unimon has been touched
    public void touchActiveUnimon(TouchEvent t) {

        //load the energy card when the active unimon card is pressed
        if ((activeCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            PlayScreen playScreen = (PlayScreen) mGameScreen;
            playScreen.getActiveEnergyState().active = true;
        }

        //hide the active unimon card when it outside the card is pressed
        if (!(activeCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            active = false;
        }

        //displaying the energy card when the energy button is pressed
        if (applyEnergyButton.isPushed()){
                PlayScreen playScreen = (PlayScreen) mGameScreen;
                playScreen.getActiveEnergyState().active = true;
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (active) {
            drawActiveUnimon(elapsedTime, graphics2D);
            drawBattleOptions(elapsedTime, graphics2D);
        }
    }

    public void drawBattleOptions(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        drawBattleOptionButtons(elapsedTime, graphics2D);
        drawMoveNames(graphics2D);
    }


    //James Bailey 40156063
    //draw active unimon card
    public void drawActiveUnimon(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        Rect paintRect = new Rect(0, 0, mScreenViewport.width, mScreenViewport.height); //set background black semi-opaque background to fill whole screenviewport

        //create the semi-transparent black bitmap in the background of the active unimon card
        if (blackBitmap == null) {
            blackBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            blackBitmap.eraseColor(Color.BLACK);
        }
        if (blackBitmapPaint == null) {
            blackBitmapPaint = new Paint();
            blackBitmapPaint.setAlpha(200); //make the background behind the unimon card semi-opaque
        }

        graphics2D.drawBitmap(blackBitmap, null, paintRect, blackBitmapPaint);
        activeCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //James Bailey 40156063
    //method for drawing the battle option buttons
    public void drawBattleOptionButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (ReleaseButton moveButton : moveButtons) { //drawing the move buttons
            moveButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        //drawing the other buttons
        applyEnergyButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        evolveButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        retreatButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //generate battle button options
    public void generateBattleOptions() {
        generateEnergyButton();
        generateEvolveButton();
        generateRetreatButton();
        generateMoveButtons();
    }
    
    //generate the button to apply an energy card to the unimon card
    public void generateEnergyButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2 - mScreenViewport.height / 6;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        applyEnergyButton = new ReleaseButton(x, y, width, height, "ENERGYBUTTON", "ENERGYBUTTON", "", mGameScreen);
    }

    //generate the button to evolve the current active unimon
    public void generateEvolveButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        evolveButton = new ReleaseButton(x, y, width, height, "EVOLVEBUTTON", "EVOLVEBUTTON", "", mGameScreen);
    }

    //generate the button to retreat the current active unimon
    public void generateRetreatButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2 + mScreenViewport.height / 6;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        retreatButton = new ReleaseButton(x, y, width, height, "RETREATBUTTON", "RETREATBUTTON", "", mGameScreen);
    }

    //generate the buttons to use the card's moves
    public void generateMoveButtons() {
        float x = mScreenViewport.width * 0.85f;
        float y;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        for (int i = 0; i < numMoveButtons; i++) {
            y = mScreenViewport.height / 2 + ((i - 1) * mScreenViewport.height / 6);
            ReleaseButton unimonMoveButton = new ReleaseButton(x, y, width, height, "MOVEBUTTON", "MOVEBUTTON", "", mGameScreen);
            moveButtons.add(unimonMoveButton);
        }
    }

    //draws the name of the move onto the move buttons
    public void drawMoveNames(IGraphics2D graphics2D) {
        ReleaseButton moveButton;
        moveTextPaint = formatMoveButtonText();

        Rect moveButtonRect;
        BoundingBox moveButtonBound;
        String moveName;

            for (int i = 0; i < numMoveButtons; i++) {
                moveButton = moveButtons.get(i);
                moveButtonBound = moveButton.getmBound();
                moveName = activeCardMoves[i].getName();
                moveButtonRect = new Rect((int) (moveButtonBound.x - moveButtonBound.halfWidth),
                        (int) (moveButtonBound.y - moveButtonBound.halfHeight),
                        (int) (moveButtonBound.x + moveButtonBound.halfWidth),
                        (int) (moveButtonBound.y + moveButtonBound.halfHeight));

                graphics2D.drawText(moveName, moveButtonRect.centerX(), moveButtonRect.centerY(), moveTextPaint);
            }
        }


    //James Bailey 40156063
    //load the bitmaps for the battle option buttons.
    public void loadButtonOptionBitmaps() {
        mGame.getAssetManager().loadAndAddBitmap("ENERGYBUTTON", "img/PlayScreenButtons/applyEnergyButton.png");
        mGame.getAssetManager().loadAndAddBitmap("EVOLVEBUTTON", "img/PlayScreenButtons/evolveUnimonButton.png");
        mGame.getAssetManager().loadAndAddBitmap("RETREATBUTTON", "img/PlayScreenButtons/retreatUnimonButton.png");
        mGame.getAssetManager().loadAndAddBitmap("MOVEBUTTON", "img/PlayScreenButtons/moveButton.png");
    }

    //James Bailey 40156063
    //Loads the active UnimonCard in order to draw it onto the canvas
    public void loadActiveCard(BattleSetup battleSetup) {
        initaliseActiveCard(battleSetup);
        generateActiveCard();
    }

    //James Bailey 40156063
    //Gets the variables needed from the player's battlesetup in order to draw the active UnimonCard
    public void initaliseActiveCard(BattleSetup battleSetup) {
        this.activeCard = battleSetup.getActiveCard();
        this.activeCardMoves = activeCard.getMoves();
    }

    //James Bailey 40156063
    //sets what is needed in order to draw the active UnimonCard
    public void generateActiveCard() {
        float x = mScreenViewport.width/2f;
        float y = mScreenViewport.height/2f;
        float width = mScreenViewport.width / 2.3f;
        float height = mScreenViewport.height;

        activeCard.setPosition(mGameScreen, x, y, width, height);
    }

    //James Bailey 40156063
    //formats the Paint that is used to draw the text on move buttons
    public Paint formatMoveButtonText() {
        if (moveTextPaint == null) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextAlign(Paint.Align.CENTER);

            //calculate the smallest text size the buttons should have
            float smallestTextSize = calculateMoveButtonTextSize();
            paint.setTextSize(smallestTextSize);

            return paint;
        } else {
            return moveTextPaint;
        }
    }

    //James Bailey 40156063
    //calculates the largest text size the buttons could have without overrunning and still ensuring a uniform size
    private float calculateMoveButtonTextSize() {
        Paint textSize = new Paint();

        ReleaseButton moveButton;
        BoundingBox moveButtonBound;
        float moveButtonWidth;
        String moveName;

        float smallestTextSize = Float.MAX_VALUE;

        for (int i = 0; i < numMoveButtons; i++) {
            moveButton = moveButtons.get(i);
            moveButtonBound = moveButton.getmBound();
            moveButtonWidth = moveButtonBound.getWidth();
            moveName = activeCardMoves[i].getName();
            textSize = DrawAssist.calculateTextSize(textSize, moveButtonWidth, moveName);
            float textSizeVal = textSize.getTextSize();

            if (smallestTextSize > textSizeVal) {
                smallestTextSize = textSizeVal; //the new smallest text size
            }
        }
        return smallestTextSize;
    }
}
