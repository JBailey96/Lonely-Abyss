package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen;

import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Battle;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.CustomGage.DrawAssist;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.CustomGage.ReleaseButton;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;
import uk.ac.qub.eeecs.gage.CustomGage.State;

/**
 * Created by James on 18/04/2017.
 */

public class HandCardsState extends State {
    protected final int numHandCards = 5;

    public enum HandCardStateType { //the type of states this state can be have, decides which type of hand cards are presented
        SELECT_ENERGY, SELECT_UNIMON
    }

    protected HandCardStateType handCardStateType; //the type of state the state currently has

    protected int indexToDisplay = -1; //the index of the card to be displayed - first initalised to -1 so the next selected card will be the first in the list (0)
    protected Card selectedHandCard; //the current hand card presented that the user can choose to use or discard.

    protected BattleSetup playerBattleSetup;
    protected Card[] playerHandCards;
    protected Card[] stateHandCards; //the handcards to be presented in this state


    //the buttons in this state
    protected ReleaseButton nextButton;
    protected ReleaseButton prevButton;
    protected ReleaseButton useButton;
    protected ReleaseButton discardButton;

    protected boolean useButtonAvailable; //true when the user can use the card (valid evolve), false when not

    protected PlayScreen playScreen;

    //James Bailey 40156063
    public HandCardsState(ScreenViewport mScreenViewport, LayerViewport mLayerViewPort, Game mGame, GameScreen mGameScreen, Boolean active, BattleSetup battleSetup) {
        super(mScreenViewport, mLayerViewPort, mGame, mGameScreen, active);

        this.playScreen = (PlayScreen) mGameScreen;
        this.playerBattleSetup = battleSetup;
        this.playerHandCards = this.playerBattleSetup.getHandCard();
        this.handCardStateType = HandCardStateType.SELECT_ENERGY;
        this.useButtonAvailable = false;

        loadBitmaps();
        copyHandCards();
        generateHandCards();
        generateButtons();
    }

    //James Bailey 40156063
    //Refreshes the state - reinitialising the handcards that are presented to the user.
    //These maintains the consistency of the state with the other states.
    public void refresh() {
        this.indexToDisplay = -1; //first index to display will be the first (0) once this is incremented to find the next hand card to display.
        copyHandCards();
        generateHandCards();
        nextSelectedCard();
    }

    //James Bailey 40156063
    //Refreshes the hand cards after a hand card is discarded
    private void refreshAfterDiscard() {
        this.playerHandCards = this.playerBattleSetup.getHandCard();
        copyHandCards();
        generateHandCards();
    }

    @Override
    public void update(ElapsedTime elapsedTime) {
        if (active) {
            selectedHandCard.update(elapsedTime);
            updateButtons(elapsedTime);

            mInput = mGame.getInput();
            touchEvents = mInput.getTouchEvents();
            handleTouch(touchEvents);
        }
    }

    //James Bailey 40156063
    //Update all the buttons - checks if there has been a touch event on them
    private void updateButtons(ElapsedTime elapsedTime) {
        if (checkPrevCardAvailable()) { //validates that the selected hand card isn't the first of the cards to present
            prevButton.update(elapsedTime);
        }

        if (checkNextCardAvailable()) { //validates that the selected hand card isn't the last of the cards to present
            nextButton.update(elapsedTime);
        }

        if (useButtonAvailable) { //validates that the user can use the selected hand
            useButton.update(elapsedTime);
        }

        discardButton.update(elapsedTime);
    }

    //James Bailey 40156063
    //handles a touch event while the state is active
    private void handleTouch(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                DrawAssist.clearMessage();
                if (touchButtons()) { //checks whether the buttons have been touched
                    mInput.resetAccumulators();
                    break;
                } else if (touchCard(t)) { //checks whether the selected hand card has been touched
                    mInput.resetAccumulators();
                    break;
                } else { //the touch is outside both of the buttons and selected hand card
                    touchDismiss();
                }
            }
        }
    }

    //James Bailey 40156063
    //method that is called when the user touches the black background
    private void touchDismiss() {
        active = false;
        mInput.resetAccumulators();

        //refresh the active unimon and play overview's card variables, may have changed in this state.
        playScreen.getActiveUnimonState().refresh();
        playScreen.getPlayOverviewState().refresh();

        playScreen.getActiveUnimonState().active = true;
    }

    //James Bailey 40156063
    //method that is called to check whether the user has touched the selected hand card
    private boolean touchCard(TouchEvent t) {
        if ((selectedHandCard.getBound().contains((int) t.x, (int) mLayerViewPort.getTop() - t.y))) {
            return true; //user has touched the selected hand card
        }
        return false; //user's touch is outside the hand card
    }

    //James Bailey 40156063
    //method that is called to check whether the user has touched one of the buttons
    private boolean touchButtons() {
    if (prevButton.isPushed()) {
        prevSelectedCard();
        return true;
    } else if (nextButton.isPushed()) {
        nextSelectedCard();
        return true;
    } else if (useButton.isPushed()) {
        useHandCard();
        return true;
    } else if (discardButton.isPushed()) {
        discardHandCard();
        return true;
    }

        return false; //user has not touched any of the buttons.
    }

    //James Bailey 40156063
    //Method that uses the selected hand card on the player's active unimon.
    //Called when the user touches the use button
    private void useHandCard() {
        if (this.handCardStateType == HandCardStateType.SELECT_ENERGY) {
            Battle.applyEnergy(playerBattleSetup, (EnergyCard) selectedHandCard); //apply the energy hand card to the active unimoncard.
        } else if (this.handCardStateType == HandCardStateType.SELECT_UNIMON) {
            Battle.evolveUnimon(playerBattleSetup, (UnimonCard) selectedHandCard); //evolve the active unimoncard to the selected unimon hand card.
        }

        removeHandCardFromList();

        touchDismiss(); //dismiss the state and return to the active unimon state
    }

    //James Bailey 40156063
    //Method that removes the selected hand card from the list of hand cards
    //Called when the user touches the discard button
    private void discardHandCard() {
        removeHandCardFromList();


        if (checkNextCardAvailable()) {
            nextSelectedCard(); //present the next hand card
        } else if (checkPrevCardAvailable()) {
            prevSelectedCard(); //draw the previous hand card if there is no next hand card
        } else {
            touchDismiss(); //no cards can be presented to the user, dismiss the state and return to the active unimon state.
        }
    }


    //James Bailey 40156063
    //Method that selects the next card to be presented to the user.
    private void nextSelectedCard() {
        for (int i = indexToDisplay + 1; i < stateHandCards.length; i++) {
            Card handCard = stateHandCards[i];

            if (checkHandCardExist(handCard)) {
                if (checkSelectedCardValid(handCard)) {
                    //hand card is not empty and is valid (type of card required by the state type)
                    selectedHandCard = handCard;
                    this.indexToDisplay = i;

                    decideUseButtonAvailable();
                    break;
                }
            }
        }
    }

    //James Bailey 40156063
    //Method that sets the use button's boolean check to true if the user can use the selected hand card on the active card
    private void decideUseButtonAvailable() {
        if (handCardStateType == HandCardStateType.SELECT_UNIMON) {
            //validate whether the selected hand card is an evolution of the player's active card
            if (Battle.handCardisEvolve((UnimonCard) selectedHandCard, playerBattleSetup.getActiveCard())) {
                //selected hand card is an evolution
                useButtonAvailable = true;
            } else {
                //selected hand card is not an evolution
                useButtonAvailable = false;
            }
        } else { //all energy cards are able to be used on active cards
            useButtonAvailable = true;
        }
    }

    //James Bailey 40156063
    //Method that checks whether the selected card is of the type of card required by the state type
    private boolean checkSelectedCardValid(Card selectedHandCard) {
        if (handCardStateType == HandCardStateType.SELECT_ENERGY) {
            if (selectedHandCard instanceof EnergyCard) {
                return true;
            }
        }
        else if (handCardStateType == HandCardStateType.SELECT_UNIMON) {
            if (selectedHandCard instanceof UnimonCard) {
                return true;
            }
        }

        return false; //card is not of the type required by the statetype.
    }

    //James Bailey 40156063
    //Method that selects the first previous selected card that is required by the state type.
    private void prevSelectedCard() {
        for (int i = indexToDisplay - 1; i >= 0; i--) {
            Card handCard = stateHandCards[i];

            if (checkHandCardExist(handCard)) {
                if (checkSelectedCardValid(handCard)) {
                    selectedHandCard = handCard;
                    this.indexToDisplay = i;
                    decideUseButtonAvailable();
                    break;
                }
            }
        }
    }

    //James Bailey 40156063
    //Copy the hand cards from the battlesetup to a new list in this state
    private void copyHandCards() {
        stateHandCards = new Card[numHandCards]; //initialise a new list of hand cards to draw and update this state.

        //iterate through and copy the state
        for (int i = 0; i < playerHandCards.length; i++) {
            Card handCardToCopy = playerHandCards[i];

            if (checkHandCardExist(handCardToCopy)) { //check if a card exists - not empty
                stateHandCards[i] = handCardToCopy.copy(); //copy the hand card element into a new array
            }
        }
    }

    //James Bailey 40156063
    //Generates all the hand cards draw and touch positions on the canvas, linking them to the playscreen
    private void generateHandCards() {
        for (int i = 0; i < stateHandCards.length; i++) {
            Card handCard = stateHandCards[i];

            if (checkHandCardExist(handCard)) {
                generateSingleHandCard(handCard);
            }
        }
    }

    //James Bailey 40156063
    //Generates the positions and links it to the gamescreen for a single hand card
    private void generateSingleHandCard(Card handCard) {
        float width;
        float x;
        float height;
        float y;

        width = mScreenViewport.width/2.5f;
        x = mScreenViewport.width/2f;
        height = mScreenViewport.height/1.1f;
        y = mScreenViewport.height/2f;

        handCard.setPosition(mGameScreen, x, y, width, height);
    }

    //James Bailey 40156063
    //Generate the states' positions and link them to the gamescreen.
    private void generateButtons() {
        generateNextButton();
        generatePrevButton();
        generateUseButton();
        generateDiscardButton();
    }

    //James Bailey 40156063
    //Generate the next button - when pressed presents the next card to the user.
    private void generateNextButton() {
        float width;
        float x;
        float height;
        float y;

        width = mScreenViewport.width/5f;
        x = mScreenViewport.width/6f;
        height = mScreenViewport.height/8f;
        y = mScreenViewport.height/1.1f-height/2f;

        nextButton = new ReleaseButton(x, y, width, height, "NextCard", "NextCard", "", mGameScreen);
    }

    //James Bailey 40156063
    //Generates the previous button - when pressed presents the previous card to the user
    private void generatePrevButton() {
        float width;
        float x;
        float height;
        float y;

        width = mScreenViewport.width/5f;
        x = mScreenViewport.width/6f;
        height = mScreenViewport.height/8f;
        y = mScreenViewport.height/1.1f-(height*2);

        prevButton = new ReleaseButton(x, y, width, height, "PrevCard", "PrevCard", "", mGameScreen);
    }

    //James Bailey 40156063
    //Generates the use handcard button
    //when pressed, if in select energy statetype, will apply the energy card to the active unimon
    //if in select unimon statetype, will evolve the active card to the selected hand card
    private void generateUseButton() {
        float width;
        float x;
        float height;
        float y;

        width = mScreenViewport.width/5f;
        x = mScreenViewport.width-mScreenViewport.width/6f;
        height = mScreenViewport.height/8f;
        y = mScreenViewport.height/2f;

        useButton = new ReleaseButton(x, y, width, height, "UseCard", "UseCard", "", mGameScreen);
    }

    //James Bailey 40156063
    //Generates the discard button - when pressed will discard the selected hand card
    private void generateDiscardButton() {
        float width;
        float x;
        float height;
        float y;

        width = mScreenViewport.width/5f;
        x = mScreenViewport.width/6f;
        height = mScreenViewport.height/8f;
        y = (mScreenViewport.height-mScreenViewport.height/1.1f)*2f;

        discardButton = new ReleaseButton(x, y, width, height, "DiscardCard", "DiscardCard", "", mGameScreen);
    }

    //James Bailey 40156063
    //Draws the buttons that provide the functionality of the state to the user.
    private void drawButtons(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (checkPrevCardAvailable()) {
            prevButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        if (checkNextCardAvailable()) {
            nextButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        if (useButtonAvailable) {
            useButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
        }

        discardButton.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport);
    }

    //James Bailey 40156063
    //Checks whether there is a card after the selected card in the hand card list available to be presented to the user.
    private boolean checkNextCardAvailable() {
        if (indexToDisplay == stateHandCards.length-1) { //validates whether the selected hand card index is the last index
            nextButton.setmIsPushed(false); //make sure the action of the next button is not called
            return false; //as the index is the last, cannot find next
        }

        for (int i = indexToDisplay+1; i < stateHandCards.length; i++) {
            Card handCard = stateHandCards[i];

            if (checkHandCardExist(handCard)) {
                if (checkSelectedCardValid(handCard)) {
                    return true; //there is a card that can be the selected card in the indexes past the current selected card's index
                }
            }
        }

        nextButton.setmIsPushed(false); //make sure the action of the next button is not called

        return false; //there exists no card past the index of the selected hand card that can be presented to the user.
    }

    //James Bailey 40156063
    //Checks whether there is a card before the selected card in the hand card list available to be presented to the user.
    private boolean checkPrevCardAvailable() {
        if (indexToDisplay == 0) { //the selected hand card is the first in the list of hand cards.
            prevButton.setmIsPushed(false); //makes sure the action of the previous button is not called
            return false; //as the index is first, cannot find previous
        }

        for (int i = indexToDisplay-1; i >= 0; i--) {
            Card handCard = stateHandCards[i];

            if (checkHandCardExist(handCard)) {
                if (checkSelectedCardValid(handCard)) {
                    return true; //there is a card that can be the selected card in the indexes before the current selected card's index
                }
            }
        }

        prevButton.setmIsPushed(false); //makes sure the action of the previous button is not called
        return false; //there exists no card before the index of the selected hand card that can be presented to the user.
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D){
        if (active) {
            DrawAssist.drawBlackBackground(mScreenViewport, graphics2D); //draw a semi-transparent black background
            selectedHandCard.draw(elapsedTime, graphics2D, mLayerViewPort, mScreenViewport, null);
            drawButtons(elapsedTime, graphics2D);
        }
    }

    //James Bailey 40156063
    //Load button bitmaps from the asset manager to be used as the bitmaps of the buttons in this state.
    private void loadBitmaps(){
        mGame.getAssetManager().loadAndAddBitmap("UseCard", "img/PlayScreenButtons/useHandButton.png");
        mGame.getAssetManager().loadAndAddBitmap("NextCard", "img/PlayScreenButtons/nextHandCardButton.png");
        mGame.getAssetManager().loadAndAddBitmap("PrevCard", "img/PlayScreenButtons/previousHandCardButton.png");
        mGame.getAssetManager().loadAndAddBitmap("DiscardCard", "img/PlayScreenButtons/discardHandCardButton.png");
    }

    //James Bailey 40156063
    //check that the hand card is not empty (null)
    private boolean checkHandCardExist(Card handCardToTest) {
        if (handCardToTest != null) {
            return true; //hand card exists
        }
        return false; //hand card does not exist.
    }

    //James Bailey 40156063
    //removes the hand card from the list of hand cards
    private void removeHandCardFromList() {
        Battle.discardHandCard(stateHandCards, indexToDisplay, playerBattleSetup);
        //list of battlesetup's hand cards may have changed in the Battle method - keep states consistent
        playScreen.getPlayOverviewState().refreshHandCards();
        refreshAfterDiscard();
    }

    public HandCardStateType getHandCardStateType() {
        return handCardStateType;
    }

    public void setHandCardStateType(HandCardStateType handCardStateType) {
        this.handCardStateType = handCardStateType;
    }

    //James Bailey 40156063
    //Display a help message to introduce the user to the game's functionality - where to
    public void showMessage() {
        String message = "";
        if (this.handCardStateType == HandCardStateType.SELECT_ENERGY) {
            message = "These are your ENERGY CARDS in your hand.";
        } else if (this.handCardStateType == HandCardStateType.SELECT_UNIMON) {
            message = "These are your UNIMON CARDS in your hand.";
        }

        DrawAssist.showMessage(mGame, message);
    }

}
