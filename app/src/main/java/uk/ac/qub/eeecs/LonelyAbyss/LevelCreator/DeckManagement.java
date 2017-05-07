package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen.PlayScreen;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.CustomGage.DrawAssist;
import uk.ac.qub.eeecs.gage.engine.audio.Music;
import uk.ac.qub.eeecs.gage.engine.audio.Sound;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.CustomGage.ReleaseButton;
import uk.ac.qub.eeecs.gage.util.BoundingBox;
import uk.ac.qub.eeecs.gage.util.Vector2;
import uk.ac.qub.eeecs.gage.world.GameScreen;
import uk.ac.qub.eeecs.gage.world.LayerViewport;
import uk.ac.qub.eeecs.gage.world.ScreenViewport;

/**
 * Created by Patrick on 21/11/2016.
 */


public class DeckManagement extends GameScreen {
    protected ScreenViewport mScreenViewport;
    protected LayerViewport mLayerViewport;
    protected Input mInput;
    List<TouchEvent> touchEvents;
    int offset = 0;
    Random rand = new Random();

    private final int numPrizeCards = 3; //Max number of prize cards

    private final int numBenchCards = 4;  //Max number of bench cards
    private final int numUnimonDeckCards = 5; //Max number of Unimon Cards
    private final int numEnergyDeckCards = 13;  //Max energy cards

    private int numBenchCardsSelected = 4;  //number of bench cards needed
    private int unimonDeckSelected = 5; //number of unimon deck cards needed
    private int energyDeckSelected = 13;    //number of energy deck cards needed
    private int benchCardsCounter = 0;  // bench array counter

    protected Bitmap backGround; // the background bitmap
    protected Rect backgroundRect;  //backGround rectangle

    protected Rect counterRect; //card selectiuon counter rectangle

    protected Rect nextButtonRect; //next button rect
    protected ReleaseButton nextButton; //next button

    protected Rect previousButtonRect; // previous button rect
    protected ReleaseButton previousButton; //previous button

    protected Rect manaSearchSortButtonRect; //mana button rect
    protected ReleaseButton manaSearchButton; //mana button

    protected Rect staminaSearchSortButtonRect; //stamina button rect
    protected ReleaseButton staminaSearchButton; //stamina button

    protected Rect quickSetupRect;  //quickSetup rect
    protected ReleaseButton quickSetupButton; //quickSetupButton

    protected Music backgroundMusic;
    protected Sound buttonClick;

    /*protected Music deckManagementMusic;*/

    private Toast objMessage = null;    //toast message

    private Paint counterTextFormat;    //the selection counter text

    private static Paint formatText;

    UnimonCard[]prizeCards = new UnimonCard[numPrizeCards]; // prize card array
    UnimonCard[]benchCards = new UnimonCard[numBenchCards]; //bench cards array
    List<Card> tempDeck = new ArrayList<>();    //temp deck list
    Stack<Card> deck = new Stack<Card>();   //offical deck stack

    List<UnimonCard> oldUnimonCard = mGame.getPlayer().getUnimonCards();// hold the loaded unimon cards from JSON
    List<EnergyCard> oldEnergyCard = mGame.getPlayer().getEnergyCards();//hold the loaded energy cards from JSOM

    List<UnimonCard> newUnimonCard = new ArrayList<>(); //list of unimonCards
    List<EnergyCard> newEnergyCards = new ArrayList<>();//list of energy cards
    List<UnimonCard> unevlovedUnimonCards = new ArrayList<>();//list of unevolved unimon cards



    boolean staminaSearch = false;
    boolean manaSearch = false;
    boolean selectedBenchUnimon = false;
    boolean selectedHandUnimon = false;
    boolean playerFinishSelect = false;
    boolean manualSelection = false;
    boolean ifMessageShowen = false;

    //Constructor for DeckManagement. Sets viewports and calls to methods to load cards initial cards and buttons
    public DeckManagement(Game game) {
        super("DeckManagement", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        backgroundRect = new Rect(0, 0, game.getScreenWidth(), game.getScreenHeight());
        copyingUnimonCards();
        copyingUnevlovedUnimonCards();
        removingUnevolvedUnimon();
        copyingEnergyCards();
        autoGenPrizeCards();
        loadBitmaps();
        accessBitmaps();
        generateButtons();
        loadButtons();
        deckSounds();

        backgroundMusic.play();
        backgroundMusic.setLopping(true);
        backgroundMusic.isLooping();

        /*loadingSound();
        playSound();*/
    }

    //Patrick Conway 40150555
    /**
     * Places copies of oldUnimonCard elements into newUnimonCard ArrayList
     */
    public void copyingUnimonCards() {
        for (UnimonCard item : oldUnimonCard) {
            UnimonCard copyUnimon = item.copy();
            newUnimonCard.add(copyUnimon);
        }
    }

    //Patrick Conway 40150555
    /**
     * Places copies of the unevolved cards from newUnimonCard into unevolvedUnimonCard ArrayList
     */
    public void copyingUnevlovedUnimonCards(){
        for(UnimonCard item : newUnimonCard){
            if(isUnevolved(item)){
                UnimonCard unevolvedUnimon = item.copy();
                unevlovedUnimonCards.add(unevolvedUnimon);
            }
        }
    }

    //Patrick Conway 40150555
    public void removingUnevolvedUnimon(){
        for(int i = 0; i < newUnimonCard.size(); i++){
            UnimonCard unimonCard = newUnimonCard.get(i);
            if(isUnevolved(unimonCard)){
                newUnimonCard.remove(i);
                i--;
            }
        }
    }

    //Patrick Conway 40150555
    /**
     * Places copies of the oldEnergyCard ArrayList elements into newEnergyCards ArrayList
     */
    public void copyingEnergyCards(){
        for(EnergyCard item : oldEnergyCard){
            EnergyCard copyEnergy = item.copy();
            newEnergyCards.add(copyEnergy);
        }
    }

    //Patrick Conway 40150555
    //For response to a touch event
    public void update(ElapsedTime elapsedTime) {
        updateNextButtonCheck(elapsedTime);
        updatePreviousButtonCheck(elapsedTime);
        updateManaButton(elapsedTime);
        updateStaminaButton(elapsedTime);
        updateQuickSetupButton(elapsedTime);
       if(playerFinishSelect){
           toastClear();
           setPlayerBattleSetup();
       }
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        touchButton(touchEvents);
    }

    //Patrick Conway 40150555
    /**
     * Checks if the Offset + 3 is less than the ArrayList size on which it is acting upon.
     * If so update the nextButton.
     * @param elapsedTime - time elapsed from last update
     */
    public void updateNextButtonCheck(ElapsedTime elapsedTime) {
        if(!selectedBenchUnimon){
            if((offset + 3 < unevlovedUnimonCards.size())){
                nextButton.update(elapsedTime);
            }
        }
        if (!selectedHandUnimon) {
            if ((offset + 3) < (newUnimonCard.size())) {
                nextButton.update(elapsedTime);
            }
        } else {
            if ((offset + 3) < (newEnergyCards.size())) {
                nextButton.update(elapsedTime);
            }
        }
    }

    //Patrick Conway 40150555
    /**
     * Check if the offset variable is greater than zero. If so call the update method on the previousButton.
     * @param elapsedTime - time elapsed from last update
     */
    public void updatePreviousButtonCheck(ElapsedTime elapsedTime){
        if(offset != 0){
            previousButton.update(elapsedTime);
        }
    }

    //Patrick Conway 40150555
    /**
     * Check to see if the Hand Unimon have been selected. If not call the update method on the manaSearchButton.
     * @param elapsedTime - time elapsed from last update
     */
    public void updateManaButton(ElapsedTime elapsedTime){
        if(!selectedHandUnimon){
            manaSearchButton.update(elapsedTime);
        }
    }

    //Patrick Conway 40150555
    /**
     * Check to see if the Hand Unimon have been selected. If not call the update method on the staminaSearchButton.
     * @param elapsedTime - time elapsed from last update
     */
    public void updateStaminaButton(ElapsedTime elapsedTime){
        if(!selectedHandUnimon){
            staminaSearchButton.update(elapsedTime);
        }
    }

    //Patrick Conway 40150555
    /**
     * Check to see if  have been selected. If not call the update method on the manaSearchButton,
     * @param elapsedTime - time elapsed from last update
     */
    public void updateQuickSetupButton(ElapsedTime elapsedTime){
        if(!playerFinishSelect){
            quickSetupButton.update(elapsedTime);
        }
    }

    //Patrick Conway 40150555
    /**
     * Checks for touch and then calls the method associated with the touch.
     * @param touchEvents - List of touchEvents that will be iterated through.
     */
    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                if (nextButton.pushTriggered()) { //the status of the button is 'pressed'
                    next();
                    buttonClick.play();
                    break;
                } else if (previousButton.pushTriggered()) {
                    buttonClick.play();
                    previous();
                    break;
                } else if (manaSearchButton.pushTriggered()) {
                    buttonClick.play();
                    touchMana();
                    break;
                }else if(staminaSearchButton.pushTriggered()){
                    buttonClick.play();
                    touchStamina();
                    break;
                }else if(quickSetupButton.pushTriggered()){
                    buttonClick.play();
                    quickSetup();
                    break;
                }else if(touchUnimonCard(t)){
                    buttonClick.play();
                    manualSelection = true;
                    break;
                }
            }
        }
    }

    //Patrick Conway 40150555
    /**
     * Applies a check to decide which List to apply the sortMethod for Mana on. Then resets the offset back to 0,
     * so user views the List in sorted order by Mana from the start.
     */
    public void touchMana(){
        if(!selectedBenchUnimon){
            quickSortByMana(unevlovedUnimonCards, 0, unevlovedUnimonCards.size() - 1);
        }else {
            quickSortByMana(newUnimonCard, 0, newUnimonCard.size() - 1);
        }
        offset = 0;
        manaSearch = true;
        toastClear();
        toast("Sorted By Mana");
    }

    //Patrick Conway 40150555
    /**
     * Applies a check to decide which List to apply the sortMethod for Stamina on. Then resets the offset back to 0,
     * so user views the List in sorted order by Stamina from the start.
     */
    public void touchStamina(){
        if(!selectedBenchUnimon){
            quickSortByStamina(unevlovedUnimonCards, 0, unevlovedUnimonCards.size() - 1);
        }else{
            quickSortByStamina(newUnimonCard, 0, newUnimonCard.size() - 1);
        }
        offset = 0;
        staminaSearch = true;
        toastClear();
        toast("Sorted By Stamina");
    }

    //Patrick Conway 40150555
    /**
     * Set up the dimensions of the NextButtonRect
     */
    public void generateNextButton() {
        int LeftDimen = mScreenViewport.width - mScreenViewport.width / 5;
        int TopDimen = (int) (mScreenViewport.height / 1.3f);
        int RightDimen = mScreenViewport.width - 12;
        int BottomDimen = (int) (mScreenViewport.height / 1.1f);
        nextButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }

    //Patrick Conway 40150555
    /**
     * Set up the dimensions of the PreviousButtonRect
     */
    public void generatePreviousButton() {
        int LeftDimen = mScreenViewport.width - mScreenViewport.width;
        int TopDimen = (int) (mScreenViewport.height / 1.3f);
        int RightDimen = mScreenViewport.width - (int) (mScreenViewport.width / 1.2f);
        int BottomDimen = (int) (mScreenViewport.height / 1.1f);
        previousButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }

    //Patrick Conway 40150555
    /**
     * Set up the dimensions of the ManaSearchSortButtonRect
     */
    public void generateSearchManaButton() {
        int LeftDimen = mScreenViewport.width - (int) (mScreenViewport.width / 1.4f);
        int TopDimen = 80;
        int RightDimen = (int) (mScreenViewport.width/2.25f);
        int BottomDimen = (mScreenViewport.height / 5);
        manaSearchSortButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }

    //Patrick Conway 40150555
    /**
     * Set up the dimensions of the staminaSearchSortButtonRect
     */
    public void generateSearchStaminaButton() {
        int LeftDimen = mScreenViewport.width - (int) (mScreenViewport.width / 1.8f);
        int TopDimen = 80;
        int RightDimen = (int)(mScreenViewport.width/1.63);
        int BottomDimen = (mScreenViewport.height / 5);
        staminaSearchSortButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }

    //Patrick Conway 40150555
    /**
     * Set up the dimensions of the counterRect.
     */
    public void setCounter(){
        int LeftDimen = mScreenViewport.width - mScreenViewport.width / 4;
        int TopDimen = 80;
        int RightDimen = mScreenViewport.width - 200;
        int BottomDimen = (mScreenViewport.height / 5);
        counterRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }

    //Patrick Conway 40150555
    /**
     * Set up the dimensions of the quickSetupRect.
     */
    public void quickSetupButton(){
        int LeftDimen = mScreenViewport.width - mScreenViewport.width;
        int TopDimen = 80;
        int RightDimen = mScreenViewport.width - (int) (mScreenViewport.width / 1.2f);
        int BottomDimen =  (mScreenViewport.height / 5);
        quickSetupRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }

    //Patrick Conway 40150555
    /**
     * Set the Released buttons to their location on the screen by setting them within the Rectangles created.
     * Also applies the Bitmap image of the Button to the Released Buttons.
     */
    public void loadButtons() {
        nextButton = new ReleaseButton(nextButtonRect.exactCenterX(), nextButtonRect.exactCenterY(), nextButtonRect.width(), nextButtonRect.height(), "Next", "Next", "", this);
        previousButton = new ReleaseButton(previousButtonRect.exactCenterX(), previousButtonRect.exactCenterY(), previousButtonRect.width(), previousButtonRect.height(), "Previous", "Previous", "", this);
        manaSearchButton = new ReleaseButton(manaSearchSortButtonRect.exactCenterX(), manaSearchSortButtonRect.exactCenterY(), manaSearchSortButtonRect.width(), manaSearchSortButtonRect.height(), "Mana", "Mana", "", this);
        staminaSearchButton = new ReleaseButton(staminaSearchSortButtonRect.exactCenterX(), staminaSearchSortButtonRect.exactCenterY(), staminaSearchSortButtonRect.width(), staminaSearchSortButtonRect.height(), "Stamina", "Stamina", "", this);
        quickSetupButton = new ReleaseButton(quickSetupRect.exactCenterX(), quickSetupRect.exactCenterY(), quickSetupRect.width(), quickSetupRect.height(), "QuickSetup", "QuickSetup", "", this);

    }

    //Patrick Conway 40150555
    /**
     * Generates all the buttons for the screen by calling the dimensions Methods.
     */
    public void generateButtons() {
        generateNextButton();
        generatePreviousButton();
        generateSearchManaButton();
        generateSearchStaminaButton();
        quickSetupButton();
        setCounter();
    }


    //Patrick Conway 40150555
    /**
     * Check if the offset variable is greater than zero. If so call the draw method on the previousButton.
     * @param elapsedTime - time elapsed from last draw.
     * @param graphics2D - drawing to the canvas
     */
    public void prevButtonDraw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if(offset != 0){
            previousButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    //Patrick Conway 40150555
    /**
     * Checks if the Offset + 3 is less than the ArrayList size on which it is acting upon.
     * If so draw the nextButton.
     * @param elapsedTime - time elapsed from last draw
     * @param graphics2D - drawing to the canvas
     */
    public void nextButtonDraw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if(!selectedBenchUnimon){
            if ((offset + 3) < unevlovedUnimonCards.size()) {
                nextButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);

            }
        }else if(!selectedHandUnimon) {
            if ((offset + 3) < newUnimonCard.size()) {
                nextButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }else{
            if ((offset + 3) < newEnergyCards.size()) {
                nextButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
            }
        }
    }

    //Patrick Conway 40150555
    /**
     * Check to see if the Hand Unimon have been selected. If not, call the draw method on the manaSearchButton.
     * @param elapsedTime - time elapsed from last update
     * @param graphics2D - drawing to the canvas
     */
    public void manaButtonDraw(ElapsedTime elapsedTime,IGraphics2D graphics2D){
        if(!selectedHandUnimon){
            manaSearchButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    //Patrick Conway 40150555
    /**
     * Check to see if the Hand Unimon have been selected. If not call the draw method on the staminaSearchButton.
     * @param elapsedTime - time elapsed from last update
     * @param graphics2D - drawing to the canvas
     */
    public void staminaButtonDraw(ElapsedTime elapsedTime,IGraphics2D graphics2D){
        if(!selectedHandUnimon){
            staminaSearchButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    //Patrick Conway 40150555
    public void quickSetupButtonDraw(ElapsedTime elapsedTime,IGraphics2D graphics2D){
        if(!playerFinishSelect){
            quickSetupButton.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
        }
    }

    //Patrick Conway 40150555
    /**
     * Adds 3 from the offset variable.
     */
    public void next(){
        offset += 3;
    }

    //Patrick Conway 40150555
    /**
     * Subtracts 3 from the offset variable.
     */
    public void previous(){
        offset -= 3;
    }

    //Patrick Conway 40150555
    /**
     * Finds the modulus of unimonCards and adds it to the local variable 'value'.
     * @param unimonCards - List of unimonCards to check size.
     */
    public int unimonNotOfModThree(List<UnimonCard> unimonCards){
        int value = 0;
        int mod = unimonCards.size()% 3;
        value += mod;
        return value;
    }

    //Patrick Conway 40150555
    /**
     * Finds the modulus of newEnergyCards and adds it to the local variable 'value'.
     */
    public int energyNotOfModThree(){
        int value = 0;
        int mod = newEnergyCards.size()% 3;
        value += mod;
        return value;
    }


    //Patrick Conway 40150555
    /**
     * Checks if the offset + 3 is larger than the List size
     * @return - true or false depending on outcome
     */
    public boolean isOverSize(){
        if(!selectedBenchUnimon) {
            if ((offset + 3) > unevlovedUnimonCards.size()) {
                return true;
            }
        }else if(!selectedHandUnimon){
            if ((offset + 3) > newUnimonCard.size()) {
                return true;
            }
        }else{
            if((offset + 3) > newEnergyCards.size()){
                return true;
            }
        }
        return false;
    }


    //Patrick Conway 40150555
    /**
     * Generates the dimensions for the next 3 cards from the giving list to be drawn.
     * Then calls the draw method on these card instance(setUpCard)
     * @param unimonCards - The List to call in the draw
     * @param elapsedTime - the time elapsed from the last draw
     * @param graphics2D - drawing to the canvas
     */
    public void developUnimonCard(ElapsedTime elapsedTime,IGraphics2D graphics2D,List<UnimonCard> unimonCards){
        int value = 3;
        if(isOverSize()){
            value = unimonNotOfModThree(unimonCards);
        }
        for(int i = 0; i < value; i++){
            UnimonCard setUpCard = unimonCards.get(i + offset);
            if(i == 0){
                generateCardDimensions((mLayerViewport.getLeft() + mLayerViewport.x / 3),setUpCard);
                setUpCard.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
            }else if(i == 2){
                generateCardDimensions((mLayerViewport.getRight() - mLayerViewport.x / 3),setUpCard);
                setUpCard.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
            }else{
                generateCardDimensions(mLayerViewport.x,setUpCard);
                setUpCard.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
            }
        }
    }

    //Patrick Conway 40150555
    /**
     * Generates the dimensions for the next 3 cards from the newEnergyCards List to be drawn.
     * Then calls the draw method on these card instance(setUpCard)
     * @param elapsedTime - the time elapsed from the last draw
     * @param graphics2D - drawing to the canvas
     */
    public void developEnergyCard(ElapsedTime elapsedTime,IGraphics2D graphics2D){
        int value = 3;
        if(isOverSize()){
            value = energyNotOfModThree();
        }
        for(int i = 0; i < value; i++){
            EnergyCard setUpCard = newEnergyCards.get(i + offset);
            if(i == 0){
                generateCardDimensions((mLayerViewport.getLeft() + mLayerViewport.x / 3),setUpCard);
                setUpCard.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
            }else if(i == 2){
                generateCardDimensions((mLayerViewport.getRight() - mLayerViewport.x / 3),setUpCard);
                setUpCard.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
            }else{
                generateCardDimensions(mLayerViewport.x,setUpCard);
                setUpCard.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
            }
        }
    }

    //Patrick Conway 40150555
    /**
     * Checks which List to apply the touch upon. Checks if the Bounds of the cards at i + offset are on the touch area.
     * If so call the related touch methods.
     * @param t - the type of touch on the screen
     */
    public boolean touchUnimonCard(TouchEvent t){
        UnimonCard unimonCard;
        for(int i = 0; i < 3; i++){
            if(!selectedBenchUnimon) {
                unimonCard = unevlovedUnimonCards.get(i + offset);
                if (unimonCard.getBound().contains((int) t.x, (int) mLayerViewport.getTop() - t.y)) {
                    if (numBenchCardsSelected != 0) {
                        touchBench(i + offset);
                    }
                }
            }else if(!selectedHandUnimon) {
                unimonCard = newUnimonCard.get(i + offset);
                if (unimonCard.getBound().contains((int) t.x, (int) mLayerViewport.getTop() - t.y)) {
                    if (unimonDeckSelected != 0) {
                        touchUnimonDeck(i + offset);
                    }
                    return true;
                }
            }else{
                EnergyCard energyCard = newEnergyCards.get(i + offset);
                if (energyCard.getBound().contains((int) t.x, (int) mLayerViewport.getTop() - t.y)){
                    if(energyDeckSelected !=0){
                        touchEnergyDeck(i + offset);
                    }
                    return true;
                }
            }

        }
        return false;
    }

    //Patrick Conway 40150555
    /**
     *  Adds the selected unimon card to the benchCards array.
     *  Then increments the benchCards Counter and decrements the numberOfSelected unimon cards
     * @param index - index of the List card
     */
    public void touchBench(int index){
        UnimonCard unimonCard = unevlovedUnimonCards.get(index).copy();
        benchCards[benchCardsCounter] = unimonCard;
        unevlovedUnimonCards.remove(index);
        benchCardsCounter++;
        numBenchCardsSelected--;
        if(numBenchCardsSelected == 0){
            selectedBenchUnimon = true;
            offset = 0;
        }
        if(isOverSize()){
            previous();
        }
    }

    //Patrick Conway 40150555
    /**
     *  Adds the selected unimon card to the tempDeck array.
     *  Then decrements the unimonDeckSelected
     * @param index - index of the List card
     */
    public void touchUnimonDeck(int index){
        UnimonCard unimonCard = newUnimonCard.get(index).copy();
        tempDeck.add(unimonCard);
        newUnimonCard.remove(index);
        unimonDeckSelected--;
        if(unimonDeckSelected == 0){
            selectedHandUnimon = true;
            offset = 0;
        }
        if(isOverSize()){
            previous();
        }
    }

    //Patrick Conway 40150555
    /**
     *  Adds the selected Energy card to the tempDeck array.
     *  Then decrements the unimonDeckSelected
     * @param index - index of the List card
     */
    public void touchEnergyDeck(int index){
        EnergyCard energyCard = newEnergyCards.get(index).copy();
        tempDeck.add(energyCard);
        newEnergyCards.remove(index);
        energyDeckSelected--;
        if(energyDeckSelected == 0){
            playerFinishSelect = true;
        }
        if(isOverSize()){
            previous();
        }
    }


    //Patrick Conway 40150555
    /**
     * Sets the dimensions of the setUpCard and the bounding values.
     * @param x - index of the List card
     * @param setUpCard - Card selected
     */
    public void generateCardDimensions(float x, Card setUpCard){
        float width = 400;
        float height = 500;
        float y = mLayerViewport.y;
        setUpCard.setmGameScreen(this);

        BoundingBox mBound = new BoundingBox();
        mBound.x = x;
        mBound.y = y;
        mBound.halfWidth = width/2;
        mBound.halfHeight = height/2;

        setUpCard.setPosition(new Vector2(x,y));
        setUpCard.setmBound(mBound);

        setUpCard.setPositionChanged(true);

    }

    //Patrick Conway 40150555
    /**
     * Calls the methods to draw buttons or text.
     *@param elapsedTime - time elapsed from the last draw
     *@param graphics2D - drawing to the canvas
     */
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.drawBitmap(backGround, null, backgroundRect, null);
        drawCards(elapsedTime,graphics2D);

        prevButtonDraw(elapsedTime, graphics2D);
        nextButtonDraw(elapsedTime, graphics2D);

        manaButtonDraw(elapsedTime,graphics2D);
        staminaButtonDraw(elapsedTime,graphics2D);
        quickSetupButtonDraw(elapsedTime,graphics2D);

        formatText = formatText();

        drawCounterValue(graphics2D, formatText,counterRect);
    }

    //Patrick Conway 40150555
    /**
     * Calls the methods to draw cards.
     *@param elapsedTime - time elapsed from the last draw
     *@param graphics2D - drawing to the canvas
     */
    public void drawCards(ElapsedTime elapsedTime,IGraphics2D graphics2D){
        if(!selectedBenchUnimon){
            developUnimonCard(elapsedTime,graphics2D,unevlovedUnimonCards);
            if(!ifMessageShowen) {
                toast("Select 4 UNEVOLVED Unimon Cards");
            }
            ifMessageShowen = true;
        }else if(!selectedHandUnimon){
            developUnimonCard(elapsedTime,graphics2D,newUnimonCard);
            if(ifMessageShowen) {
                toastClear();
                toast("Select 5 UNIMON Cards");
            }
            ifMessageShowen = false;

        }else{
            developEnergyCard(elapsedTime,graphics2D);
            if(!ifMessageShowen) {
                toastClear();
                toast("Select 13 ENERGY Cards");
            }
            ifMessageShowen = true;
        }
    }

    //Patrick Conway 40150555
    /**
     * Load button bitmaps from the asset manager.
     */
    public void loadBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("Next", "img/DeckManagement/button_Next.png");
        getGame().getAssetManager().loadAndAddBitmap("Previous", "img/DeckManagement/button_Previous.png");
        getGame().getAssetManager().loadAndAddBitmap("Unimon", "img/DeckManagement/Unimon.png");
        getGame().getAssetManager().loadAndAddBitmap("Stamina", "img/DeckManagement/button_Stamina.png");
        getGame().getAssetManager().loadAndAddBitmap("Mana","img/DeckManagement/button_Mana.png");
        getGame().getAssetManager().loadAndAddBitmap("extra","img/DeckManagement/extra.jpg");
        getGame().getAssetManager().loadAndAddBitmap("BackGround","img/DeckManagement/BackGround.png");
        getGame().getAssetManager().loadAndAddBitmap("QuickSetup","img/DeckManagement/QuickSetup.png");
    }

    //Patrick Conway 40150555
    /**
     * Loads the BackGround bitmap from the asset manager
     */
    public void accessBitmaps(){
        backGround = mGame.getAssetManager().getBitmap("BackGround");
    }


    //Patrick Conway 40150555
    /**
     * Takes in a list of unimonCards and partitions the list between the start and end.
     * Then checks if the start cards Stamina value is less than the pivot. If so increment i.
     * Then checks if the last card is greater than the pivot. If so decrement j
     * then swap i and j elements.[Of O(nlog(n) time complexity]
     * @param unimonCards - list of unimon Cards;
     * @param left - the left most part of the unimon Card List
     * @param right - the right most part of the unimon Card List
     */
    int partitionByStamina(List<UnimonCard>unimonCards, int left, int right)
    {
        int i = left, j = right;
        UnimonCard tmp;
        int pivot = unimonCards.get((left + right) / 2).getMaxStamina();

        while (i <= j) {
            while (unimonCards.get(i).getMaxStamina() < pivot)
                i++;
            while (unimonCards.get(j).getMaxStamina() > pivot)
                j--;
            if (i <= j) {
                tmp = unimonCards.get(i).copy();
                unimonCards.remove(i);
                unimonCards.add(i,unimonCards.get(--j).copy());
                unimonCards.remove(++j);
                unimonCards.add(j,tmp);
                i++;
                j--;
            }
        };

        return i;
    }


    void quickSortByStamina(List<UnimonCard>unimonCards, int left, int right) {
        int index = partitionByStamina(unimonCards, left, right);
        if (left < index - 1)
            quickSortByStamina(unimonCards, left, index - 1);
        if (index < right)
            quickSortByStamina(unimonCards, index, right);
    }

    //Patrick Conway 40150555
    /**
     * Takes in a list of unimonCards and partitions the list between the start and end.
     * Then checks if the start cards Mana value is less than the pivot. If so increment i.
     * Then checks if the last card is greater than the pivot. If so decrement j
     * then swap i and j elements.[Of O(nlog(n) time complexity]
     * @param unimonCards - list of unimon Cards;
     * @param left - the left most part of the unimon Card List
     * @param right - the right most part of the unimon Card List
     */
    int partitionByMana(List<UnimonCard>unimonCards, int left, int right)
    {
        int i = left, j = right;
        UnimonCard tmp;
        int pivot = unimonCards.get((left + right) / 2).getMaxMana();

        while (i <= j) {
            while (unimonCards.get(i).getMaxMana() < pivot)
                i++;
            while (unimonCards.get(j).getMaxMana() > pivot)
                j--;
            if (i <= j) {
                tmp = unimonCards.get(i).copy();
                unimonCards.remove(i);
                unimonCards.add(i,unimonCards.get(--j).copy());
                unimonCards.remove(++j);
                unimonCards.add(j,tmp);
                i++;
                j--;
            }
        };

        return i;
    }


    void quickSortByMana(List<UnimonCard>unimonCards, int left, int right) {
        int index = partitionByMana(unimonCards, left, right);
        if (left < index - 1)
            quickSortByStamina(unimonCards, left, index - 1);
        if (index < right)
            quickSortByStamina(unimonCards, index, right);
    }

    //Patrick Conway 40150555
    /**
     * Goes through the List of unimonCards and using random selection selects 3 Unimon cards
     * and places them in the prizeCard array.
     */
    public void autoGenPrizeCards(){
        int arrayListSize = newUnimonCard.size();
        for(int i = 0; i < numPrizeCards; i++){
            int listIndex = rand.nextInt(arrayListSize);
            prizeCards[i] = newUnimonCard.get(listIndex).copy();
            newUnimonCard.remove(listIndex);
            arrayListSize--;
        }
    }

    //Patrick Conway 40150555
    /**
     * Checks if manual selection has taken place. If so the total number of cards need is max number subtract the number selected,
     * and if not it is the max number needed.
     * @param numCardsSelected - the number of cards manually selected
     * @param numCardsNeededOverall - the max number of cards need.
     * @return - the number of cards still to select.
     */
    public int numCardsRemaining(int numCardsSelected, int numCardsNeededOverall){
        int totalNumCardsNeeded;
        if(manualSelection){
            totalNumCardsNeeded = numCardsSelected;
        }else{
            totalNumCardsNeeded = numCardsNeededOverall;
        }
        return totalNumCardsNeeded;
    }

    //Patrick Conway 40150555
    /**
     * Sets the number of cards need. Goes through the Unevolved Unimon List and using random selection selects
     * the remaining bench cards placing them in the bench array
     * Sets the boolean variable selectedBenchUnion to true
     */
    public void autoGenBenchCards(){
        int totalNumBenchCardsNeeded = numCardsRemaining(numBenchCardsSelected,numBenchCards);
        int arrayListSize = unevlovedUnimonCards.size();

        for(int i = 0; i < totalNumBenchCardsNeeded; i++){
            int listIndex = rand.nextInt(arrayListSize);
            benchCards[i] = unevlovedUnimonCards.get(listIndex).copy();
            unevlovedUnimonCards.remove(listIndex);
            arrayListSize--;
        }

        selectedBenchUnimon = true;
    }

    //Patrick Conway 40150555
    /**
     * Sets the number of cards need. Goes through the new Unimon List and using random selection selects
     * the remaining unimon hand cards placing them in the hand List
     * Sets the boolean variable selectedHandUnion to true
     */
    public void autoGenDeckUnimon(){
        int arrayListSize = newUnimonCard.size();
        int totalNumHandCardsNeeded = numCardsRemaining(unimonDeckSelected,numUnimonDeckCards);

        for(int i = 0; i < totalNumHandCardsNeeded; i++){
            int listIndex = rand.nextInt(arrayListSize);
            UnimonCard deckCard = newUnimonCard.get(listIndex).copy();
            tempDeck.add(deckCard);
            newUnimonCard.remove(listIndex);
            arrayListSize--;
        }

        selectedHandUnimon = true;
    }

    //Patrick Conway 40150555
    /**
     * Sets the number of cards need. Goes through the new energy List and using random selection selects
     * the remaining energy hand cards placing them in the hand List
     * Sets the boolean variable playerFinishedSelect to true
     */
    public void autoGenDeckEnergy(){
        int arrayListSize = newEnergyCards.size();
        int totalNumEnergyCardsNeeded = numCardsRemaining(energyDeckSelected,numEnergyDeckCards);

        for(int i = 0; i < totalNumEnergyCardsNeeded; i++){
            int listIndex = rand.nextInt(arrayListSize);
            EnergyCard deckCard = newEnergyCards.get(listIndex).copy();
            tempDeck.add(deckCard);
            newEnergyCards.remove(listIndex);
            arrayListSize--;
        }

        playerFinishSelect = true;
    }

    //Patrick Conway 40150555
    /**
     * Based on the boolean values calls the required auto select methods
     */
    public void quickSetup(){
        if(!selectedBenchUnimon) {
            autoGenBenchCards();
        }else if(!selectedHandUnimon) {
            autoGenDeckUnimon();
        }else {
            autoGenDeckEnergy();
            shuffle();
        }
    }

    //Patrick Conway 40150555
    /**
     * Using the Collections framework method shuffle(), shuffles the tempDeck List.
     * Then pushes copies of the shuffled temp deck elements to the Stack deck.
     */
    public void shuffle(){
        Collections.shuffle(tempDeck);
        for(Card item:tempDeck){
            Card card = item.copy();
            deck.push(card);
        }
        tempDeck.clear();

    }


    public void setPlayerBattleSetup(){
        shuffle();
        BattleSetup battleSetup = new BattleSetup(deck,benchCards,prizeCards);
        mGame.getPlayer().setPlayerBattleSetup(battleSetup);
        mGame.getScreenManager().removeScreen(this.getName());
        PlayScreen playS = new PlayScreen(mGame);
        mGame.getScreenManager().addScreen(playS);

    }

    //Patrick Conway 40150555
    /**
     * Takes in the String message and using the toast calls the makeText method using the UIThread of Android.
     * @param message - Message to display to the user.
     */
    public void toast(final String message){

        getGame().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                objMessage = Toast.makeText(mGame.getActivity(),message,Toast.LENGTH_LONG);
                objMessage.show();

            }
        });

    }

    //Patrick Conway 40150555
    /**
     * Clears the toast message.
     */
    public void toastClear(){
        objMessage.cancel();
    }


    //Patrick Conway 40150555
    /**
     * Checks if the cards element type is of Spirit. The least evolved card type.
     * @param deckCard - the card being checked.
     */
    public boolean isUnevolved(UnimonCard deckCard){
        if(deckCard.getEvolveType().equals(UnimonEvolveType.SPIRIT)){
            return true;
        }
        return false;
    }

    //Patrick Conway 40150555
    /**
     * Setting the colour and format of the counter text
     */
    private static Paint formatText() {
        if (formatText == null) {
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setTextAlign(Paint.Align.CENTER);
            return paint;
        } else {
            return formatText;
        }
    }

    //Patrick Conway 40150555
    /**
     * Based on what has been selected, set the counterValue text and pass to the drawText method.
     * @param graphics2D - drawing to the canvas
     * @param formatText - the text format
     * @param counterValueRect - the rectangle to draw the text to.
     */
    public void drawCounterValue(IGraphics2D graphics2D, Paint formatText,Rect counterValueRect){
        String counterValue = "";
        if(!selectedBenchUnimon) {
            counterValue = ("Bench Unimon | " +  Integer.toString(numBenchCardsSelected) + "/ 4");
        }
        else if(!selectedHandUnimon){
            counterValue = ("Hand Unimon | " +  Integer.toString(unimonDeckSelected) + "/ 5");
        }
        else if(!playerFinishSelect){
            counterValue = ("Hand Energy | " +  Integer.toString(energyDeckSelected) + "/ 13");
        }
        counterTextFormat = DrawAssist.calculateTextSize(formatText, counterValueRect.width()*2, counterValue);
        graphics2D.drawText(counterValue,counterValueRect.centerX(), counterValueRect.centerY(), counterTextFormat);
    }

    //J Devlin 40150554: Background Sounds
    public void deckSounds(){
        getGame().getAssetManager().loadAndAddMusic("DECKBACKGROUND", "Music/Desolation.mp3");
        backgroundMusic = getGame().getAssetManager().getMusic("DECKBACKGROUND");

        getGame().getAssetManager().loadAndAddMusic("BUTTON", "Sounds/button.mp3");
        buttonClick = getGame().getAssetManager().getSound("BUTTON");
    }


    //Patrick Conway 40150555
/*   public void loadingSound(){
       getGame().getAssetManager().loadAndAddMusic("DeckManagment", "Music/GridMusic2.mp3");
       deckManagementMusic = mGame.getAssetManager().getMusic("DeckManagement");
   }

   //Patrick Conway 40150555
   public void playSound(){
       deckManagementMusic.setLopping(true);
       deckManagementMusic.setVolume(10);
       deckManagementMusic.play();
   }*/

}




