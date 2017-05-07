package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Battle;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.CustomGage.DrawAssist;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.CustomGage.ReleaseButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.CustomGage.State;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen.HandCardsState.HandCardStateType;

/**
 * Created by James on 08/03/2017.
 */

public class ActiveUnimonState extends State {
    protected final int numMoveButtons = 3; //the number of move buttons

    protected PlayScreen playScreen; //the gamescreen controlling the state

    private UnimonCard activeCard;  //player's current active UnimonCard
    private UnimonMoves[] activeCardMoves; //the moves of the active card

    //Battle Option Buttons
    private ReleaseButton applyEnergyButton;
    private ReleaseButton evolveButton;
    private ReleaseButton retreatButton;
    protected boolean touchActive;
    //Move battle options
    protected List<ReleaseButton> moveButtons = new ArrayList<>();

    private static Paint moveTextPaint; //the text used for the moves

    protected BattleSetup battleSetup; //the player's battlesetup

    public ActiveUnimonState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);
        loadButtonOptionBitmaps();
        generateBattleOptions();
        loadActiveCard(battleSetup);

        this.playScreen = (PlayScreen) mGameScreen;
        this.battleSetup = battleSetup;
        touchActive = true;
    }

    //James Bailey - 40156063
    //method that resets the generation of the active card
    //called generally when the active card could have changed
    public void refresh() {
        this.moveTextPaint = null; //resets move text
        loadActiveCard(battleSetup);
    }

    //James Bailey 40156063
    //Loads the active UnimonCard in order to draw it onto the canvas
    private void loadActiveCard(BattleSetup battleSetup) {
        initaliseActiveCard(battleSetup);
        generateActiveCard();
    }

    //James Bailey 40156063
    //Gets the variables needed from the player's battlesetup in order to draw the active UnimonCard
    private void initaliseActiveCard(BattleSetup battleSetup) {
        this.activeCard = battleSetup.getActiveCard();
        this.activeCardMoves = activeCard.getMoves();
    }

    //James Bailey 40156063
    //sets what is needed in order to draw the active UnimonCard
    private void generateActiveCard() {
        float x = mScreenViewport.width/2f;
        float y = mScreenViewport.height/2f;
        float width = mScreenViewport.width / 2.3f;
        float height = mScreenViewport.height;

        activeCard.setPosition(mGameScreen, x, y, width, height);
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

    //James Bailey 40156063
    //Updates all the battle option buttons
    private void updateButtons(ElapsedTime elapsedTime) {
        applyEnergyButton.update(elapsedTime);
        evolveButton.update(elapsedTime);
        retreatButton.update(elapsedTime);

        for (ReleaseButton moveButton: moveButtons) {
            moveButton.update(elapsedTime);
        }
    }

    //James Bailey 40156063
    //Method that handles the touching of the activeUnimonCard
    private void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                if (touchBattleOptionButtons() || touchActiveUnimon(t)) break;
            }
        }
    }

   //James Bailey 40156063
    //checks whether the active unimon has been touched
    private boolean touchActiveUnimon(TouchEvent t) {
        //load the energy card when the active unimon card is pressed
        if ((activeCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            playScreen.getActiveEnergyState().active = true;
        } else if (!(activeCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) { //dismiss the active unimon card when it outside the card is pressed
            active = false;
            mInput.resetAccumulators();
            playScreen.getPlayOverviewState().touchActive = true;
            return true;
        }
        return false;
    }

    //James Bailey 40156063
    //Handles touching battle option buttons.
    private boolean touchBattleOptionButtons() {
        int moveIndex = validateMoveTouch(); //get the index of the move if move button pressed

        if (applyEnergyButton.isPushed()) {
            handleHandCardTouch(HandCardStateType.SELECT_ENERGY);
            return true;
        } else if (evolveButton.isPushed()) {
            handleHandCardTouch(HandCardStateType.SELECT_UNIMON);
            return true;
        } else if (retreatButton.isPushed()) {
            handleRetreatTouch();
            return true;
        } else if (moveIndex != -1)  { //validates whether there has been a move button pressed
            handleMoveButtonTouch(moveIndex);
            return true;
        }
        return false;
    }

    //James Bailey 40156063
    //Handles the transition to the bench state that when the users touches the retreat button
    private void handleRetreatTouch() {
        active = false; //no longer show the active unimon state
        mInput.resetAccumulators(); //no longer accept further input this update

        playScreen.getPlayOverviewState().touchActive = false; //ensures a user cannot interact with the playoverviewstate

        playScreen.getBenchState().setCurrentStateType(BenchState.StateType.RETREAT);
        playScreen.getBenchState().refresh();
        playScreen.getBenchState().active = true;
        playScreen.getBenchState().showMessage();
    }

    //James Bailey 40156063
    //Handles the transition to the hand card state when the user touches the apply energy or evolve button
    //the parameter variable stateType sets the card type, unimon or energy
    private void handleHandCardTouch(HandCardStateType stateType) {
        if (stateType == HandCardStateType.SELECT_ENERGY) {
            if (!Battle.checkEnergyCardInHand(battleSetup.getHandCard())) {
                return; //there is no energy card in the player's handcards, cannot present any energy cards
            }
        } else if (stateType == HandCardStateType.SELECT_UNIMON) {
            if (!Battle.checkUnimonCardInHand(battleSetup.getHandCard())) {
                return; //there is no unimon card in the player's hand cards, cannot present any unimon cards
            }
        }

        active = false; //disable the active unimon card state
        mInput.resetAccumulators(); //accept no more input for this state

        playScreen.getPlayOverviewState().touchActive = false; //the playoverview state cannot be interacted with through touch events

        playScreen.getHandCardsState().setHandCardStateType(stateType); //this is used to set which type of  cards are presented in this state
        playScreen.getHandCardsState().refresh(); //the battlesetup's list of hand cards may have changed, update the state to keep it consistent
        playScreen.getHandCardsState().active = true; //enable the hand card state.
        playScreen.getHandCardsState().showMessage();
    }

    //James Bailey 40156063
    //Handles the transition to the opponent state that occurs when touching a move button.
    private void handleMoveButtonTouch(int moveIndex) {
        playScreen.getPlayOverviewState().active = false;
        mInput.resetAccumulators();
        active = false;

        playScreen.getOpponentState().opponentStateType = OpponentState.OpponentStateType.MOVE_APPLIED; //show the opponent state with the statetype of that a move has just been applied
        playScreen.getOpponentState().active = true; //make the opponent state active

        UnimonMoves unimonMove = activeCard.getMoves()[moveIndex]; //get the move the user pressed the corresponding move button for
        playScreen.getOpponentState().applyMove(activeCard, unimonMove); //apply the move onto the opponent's card

        //updates the dimensions of the unimon card - primarily for the stat bars if they have changed to be recalculated.
        playScreen.getPlayOverviewState().getActiveUnimonCard().setPositionChanged(true);
        activeCard.setPositionChanged(true);
    }

    //James Bailey 40156063
    //Validates whether the user has touched a move button, and if so which one (returns 0-2, -1 if they did not)
    private int validateMoveTouch() {
        for (int i = 0; i < moveButtons.size(); i++) {
            ReleaseButton moveButton = moveButtons.get(i);

            if (moveButton.isPushed()) {
                return i; //the index of the moveButton, corresponds to the index of the UnimonMove in the card's move list.
            }
        }
        return -1; //the user did not press any move button.
    }


    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (active) {
            DrawAssist.drawBlackBackground(mScreenViewport, graphics2D); //draw a semi-transparent black background
            drawActiveUnimon(elapsedTime, graphics2D);
            drawBattleOptions(elapsedTime, graphics2D);
        }
    }

    //James Bailey 40156063
    //Draws all the battle options and the move names onto the move buttons.
    private void drawBattleOptions(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        drawBattleOptionButtons(elapsedTime, graphics2D);
        drawMoveNames(graphics2D);
    }


    //James Bailey 40156063
    //draw active unimon card
    private void drawActiveUnimon(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        activeCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //James Bailey 40156063
    //method for drawing the battle option buttons
    private void drawBattleOptionButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        for (ReleaseButton moveButton : moveButtons) { //drawing the move buttons
            moveButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        //drawing the other buttons
        applyEnergyButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        evolveButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        retreatButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //James Bailey 40156063
    //generate battle button options
    private void generateBattleOptions() {
        generateEnergyButton();
        generateEvolveButton();
        generateRetreatButton();
        generateMoveButtons();
    }

    //James Bailey 40156063
    //generate the button to apply an energy card to the unimon card
    private void generateEnergyButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2 - mScreenViewport.height / 6;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        applyEnergyButton = new ReleaseButton(x, y, width, height, "ENERGYBUTTON", "ENERGYBUTTON", "", mGameScreen);
    }

    //James Bailey 40156063
    //generate the button to evolve the current active unimon
    private void generateEvolveButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        evolveButton = new ReleaseButton(x, y, width, height, "EVOLVEBUTTON", "EVOLVEBUTTON", "", mGameScreen);
    }

    //James Bailey 40156063
    //generate the button to retreat the current active unimon
    private void generateRetreatButton() {
        float x = mScreenViewport.width * 0.15f;
        float y = mScreenViewport.height / 2 + mScreenViewport.height / 6;
        float width = mScreenViewport.width / 5;
        float height = mScreenViewport.height / 8;

        retreatButton = new ReleaseButton(x, y, width, height, "RETREATBUTTON", "RETREATBUTTON", "", mGameScreen);
    }

    //James Bailey 40156063
    //generate the buttons to use the card's moves
    private void generateMoveButtons() {
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

    //James Bailey 40156063
    //draws the name of the move onto the move buttons
    private void drawMoveNames(IGraphics2D graphics2D) {
        ReleaseButton moveButton;
        moveTextPaint = formatMoveButtonText(); //the text style (font, size) to draw onto the buttons

        Rect moveButtonRect;
        BoundingBox moveButtonBound;
        String moveName;

        for (int i = 0; i < numMoveButtons; i++) {
            moveButton = moveButtons.get(i);
            moveButtonBound = moveButton.getmBound(); //get the dimensions of the moveButton
            moveName = activeCardMoves[i].getName(); //get the name of the UnimonMove

            moveButtonRect = new Rect((int) (moveButtonBound.x - moveButtonBound.halfWidth),
                    (int) (moveButtonBound.y - moveButtonBound.halfHeight),
                    (int) (moveButtonBound.x + moveButtonBound.halfWidth),
                    (int) (moveButtonBound.y + moveButtonBound.halfHeight)); //the dimensions and position to draw the move's text to

            graphics2D.drawText(moveName, moveButtonRect.centerX(), moveButtonRect.centerY(), moveTextPaint); //draw the move name centred onto the move button
            }
        }


    //James Bailey 40156063
    //load the bitmaps for the battle option buttons.
    private void loadButtonOptionBitmaps() {
        mGame.getAssetManager().loadAndAddBitmap("ENERGYBUTTON", "img/PlayScreenButtons/applyEnergyButton.png");
        mGame.getAssetManager().loadAndAddBitmap("EVOLVEBUTTON", "img/PlayScreenButtons/evolveUnimonButton.png");
        mGame.getAssetManager().loadAndAddBitmap("RETREATBUTTON", "img/PlayScreenButtons/retreatUnimonButton.png");
        mGame.getAssetManager().loadAndAddBitmap("MOVEBUTTON", "img/PlayScreenButtons/moveButton.png");
    }

    //James Bailey 40156063
    //formats the Paint that is used to draw the text on move buttons
    private Paint formatMoveButtonText() {
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

        float textSizeVal;

        float smallestTextSize = Float.MAX_VALUE; //the smallest text size the move button's should have drawn onto them

        for (int i = 0; i < numMoveButtons; i++) {
            moveButton = moveButtons.get(i);
            moveButtonBound = moveButton.getmBound(); //get the dimensions of the move button
            moveButtonWidth = moveButtonBound.getWidth();
            moveName = activeCardMoves[i].getName(); //get the name of the move
            textSize = DrawAssist.calculateTextSize(textSize, moveButtonWidth, moveName); //calculate the optimal text size (stored as Paint object) for that length of string and dimension of button
            textSizeVal = textSize.getTextSize(); //get the text size from the Paint object

            if (smallestTextSize > textSizeVal) {
                smallestTextSize = textSizeVal; //the new smallest text size
            }
        }
        return smallestTextSize; //return the smallest text size the buttons should have drawn onto them.
    }

}
