package uk.ac.qub.eeecs.LonelyAbyss.LevelCreator;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.Game;
import uk.ac.qub.eeecs.gage.engine.ElapsedTime;
import uk.ac.qub.eeecs.gage.engine.graphics.IGraphics2D;
import uk.ac.qub.eeecs.gage.engine.input.Input;
import uk.ac.qub.eeecs.gage.engine.input.TouchEvent;
import uk.ac.qub.eeecs.gage.ui.ReleaseButton;
import uk.ac.qub.eeecs.gage.world.GameObject;
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
    Card[] drawCards;
    Bitmap[] unimonCardsToDraw;
    List<Card> allCards;
    List<UnimonCard> unimonCards;
    List<EnergyCard> energyCards;
    List<String> cardBitmapReference;
    protected int nextCardIndex = 0;

    protected Rect nextButtonRect; //the exit game button dimensions
    protected ReleaseButton nextButton;

    protected Rect previousButtonRect;
    protected ReleaseButton previousButton;

    protected Rect unimonSearchButtonRect;
    protected ReleaseButton unimonSearchButton;

    protected Rect energySearchButtonRect;
    protected ReleaseButton energySearchButton;




    protected final int numCardsDisplayed = 3;

    //Constructor for DeckManagement. Sets viewports and calls to methods to load cards initial cards and buttons
    public DeckManagement(Game game) {
        super("DeckManagement", game);
        mLayerViewport = new LayerViewport(game.getScreenWidth() / 2, game.getScreenHeight() / 2, game.getScreenWidth() / 2, game.getScreenHeight() / 2);
        mScreenViewport = new ScreenViewport(0, 0, game.getScreenWidth(), game.getScreenHeight());
        loadCardBitmaps();
        gettingNextCards();
        createCard();
        loadBitmaps();
        generateButtons();
        loadButtons();
    }

    //For response to a touch event
    public void update(ElapsedTime elapsedTime) {
        nextButton.update(elapsedTime);
        previousButton.update(elapsedTime);
        unimonSearchButton.update(elapsedTime);
        energySearchButton.update(elapsedTime);
        mInput = mGame.getInput();
        touchEvents = mInput.getTouchEvents();
        touchButton(touchEvents);
    }

    public void touchButton(List<TouchEvent> touchEvents) {
        for (TouchEvent t : touchEvents) {
            if (t.type == TouchEvent.TOUCH_UP) { //if the user has touched the screen
                if (nextButton.pushTriggered()) { //the status of the button is 'pressed'
                    gettingNextCards();//call the getNextCards method
                    createCard();// call the creatCards method
                    break;
                }else if(previousButton.pushTriggered()){
                    gettingPreviousCards();
                    createCard();
                    break;
                }else if(unimonSearchButton.pushTriggered()){

                }
            }
        }
    }

    //Set up the dimensions of the NextButtonRect
    public void generateNextButton(){
        int LeftDimen = mScreenViewport.width-mScreenViewport.width/5;
        int TopDimen = (int)(mScreenViewport.height/1.3f);
        int RightDimen = mScreenViewport.width - 12;
        int BottomDimen = (int) (mScreenViewport.height/1.1f);
        nextButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }
    //Sets up the dimensions of the PreviousButtonRect
    public void generatePreviousButton(){
        int LeftDimen = mScreenViewport.width - mScreenViewport.width;
        int TopDimen = (int)(mScreenViewport.height/1.3f);
        int RightDimen = mScreenViewport.width - (int)(mScreenViewport.width/1.2f);
        int BottomDimen = (int) (mScreenViewport.height/1.1f);
        previousButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }
    //Sets up the dimensions of the seachUnimonButtonRect
    public void generateSearchUnimonButton(){
        int LeftDimen = mScreenViewport.width-mScreenViewport.width/3;
        int TopDimen = 80;
        int RightDimen = mScreenViewport.width - 300;
        int BottomDimen = (mScreenViewport.height/5);
        unimonSearchButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }
    //Sets up the dimensions of the searchEnergyButtonRect
    public void generateSearchEnergyButton(){
        int LeftDimen = mScreenViewport.width-(int)(mScreenViewport.width/1.8f);
        int TopDimen = 80;
        int RightDimen = mScreenViewport.width - 700;
        int BottomDimen = (mScreenViewport.height/5);
        energySearchButtonRect = new Rect(LeftDimen, TopDimen, RightDimen, BottomDimen);
    }

    public void loadButtons(){
        nextButton = new ReleaseButton(nextButtonRect.exactCenterX(), nextButtonRect.exactCenterY(), nextButtonRect.width(), nextButtonRect.height(), "Right", "Right", "", this);
        previousButton = new ReleaseButton(previousButtonRect.exactCenterX(),previousButtonRect.exactCenterY(),previousButtonRect.width(),previousButtonRect.height(),"Previous", "Previous", "",this);
        unimonSearchButton = new ReleaseButton(unimonSearchButtonRect.exactCenterX(),unimonSearchButtonRect.exactCenterY(), unimonSearchButtonRect.width(), unimonSearchButtonRect.height(), "Unimon", "Unimon", "", this);
        energySearchButton = new ReleaseButton(energySearchButtonRect.exactCenterX(),energySearchButtonRect.exactCenterY(), energySearchButtonRect.width(), energySearchButtonRect.height(), "Stamina", "Stamina", "", this);

    }

    //Generates all the buttons for the class
    public void generateButtons(){
        generateNextButton();
        generatePreviousButton();
        generateSearchUnimonButton();
        generateSearchEnergyButton();
    }



    public void prevButtonDraw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (nextCardIndex > 3) {//If the nextCardIndex is > 3 there is previous cards to view
            previousButton.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
        }
    }

    public void nextButtonDraw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        if (nextCardIndex+3 <= cardBitmapReference.size()) {// if the nextCardIndex+3 is less than the size of the card array then there is more cards to be displayed
            nextButton.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    public void drawingCards(ElapsedTime elapsedTime,IGraphics2D graphics2D){
        for (Card drawCard : drawCards) {
            drawCard.draw(elapsedTime, graphics2D, mLayerViewport, mScreenViewport);
        }
    }

    // Sets up an array of type Bitmap which gets filled with the token of the Bitmaps using a foreach loop and counter.
    public void gettingNextCards() {

        unimonCardsToDraw = new Bitmap[3];
        int uniCard = 0;
        int j;

        for (j = nextCardIndex; j < nextCardIndex + 3; j++) {
            Bitmap bitmapReference = (selectBitmap(cardBitmapReference.get(j)));
            unimonCardsToDraw[uniCard] = bitmapReference;
            uniCard++;
        }
        nextCardIndex = j;
    }
    // selects the previous 3 cards of the cardBitmapReference arrayList
    public void gettingPreviousCards(){
        unimonCardsToDraw = new Bitmap[3];// only holds 3 cards which can be drawen to the screen
        int uniCard = 2;//need to make sure that the order of the cards being printed are in the same order as before
        nextCardIndex -= 4;// need to decrement back 4 to get to the last previous card index
        int j;

        for(j = nextCardIndex; j >= nextCardIndex-2; j--){
            Bitmap bitmapReference = (selectBitmap(cardBitmapReference.get(j)));
            unimonCardsToDraw[uniCard] = bitmapReference;
            uniCard--;
        }
        nextCardIndex = j + 4 ;//Need to leave the nextCardIndex at the start index of the next 3 cards
    }
    //Creates an Array of size numCardsDisplayed and of type GameObject

    public void createCard() {
        drawCards = new UnimonCard[numCardsDisplayed];
        Card setUpCards;
        int counter = 0;
        //Runs a for loop, creating a instance of UnimonObject, Which sets the cards location on the screen.
        //Adds all the unimonObjects to the drawCards array array.
        for (int i = 0; i < numCardsDisplayed; i++) {
            if (i == 0) {
                setUpCards = new UnimonCard(mLayerViewport.x, mLayerViewport.y, 400, 500, unimonCardsToDraw[counter], this, "0", null, null, null, "Earth Dragon",
                        UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
                        20, 30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);
                drawCards[i] = setUpCards;
            } else if (i == 2) {
                setUpCards = new UnimonCard((mLayerViewport.getLeft() + mLayerViewport.x / 3), mLayerViewport.y, 400, 500, unimonCardsToDraw[counter + 2], this, "0", null, null, null, "Earth Dragon",
                        UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
                        20, 30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);
                drawCards[i] = setUpCards;
            } else {
                setUpCards = new UnimonCard((mLayerViewport.getRight() - mLayerViewport.x / 3), mLayerViewport.y, 400, 500, unimonCardsToDraw[counter + 1], this, "0", null, null, null, "Earth Dragon",
                        UnimonEvolveType.DEMON, Element.EARTH, null, 5, 6, 7, "test Description",
                        20, 30, Element.FIRE, 50, Element.HOLY, true, Container.ACTIVE);
                drawCards[i] = setUpCards;
            }
        }
    }

    //Calling indivdual draw methods
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D) {
        graphics2D.clear(Color.BLACK);
        graphics2D.clipRect(mScreenViewport.toRect());

        drawingCards(elapsedTime, graphics2D);
        prevButtonDraw(elapsedTime, graphics2D);
        nextButtonDraw(elapsedTime, graphics2D);

        unimonSearchButton.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
        energySearchButton.draw(elapsedTime,graphics2D,mLayerViewport,mScreenViewport);
    }


    //Loads the card bitmaps from the asset manager. Adds the token of the Bitmap to an ArrayList
    public void loadCardBitmaps() {
        getGame().getAssetManager().loadAndAddBitmap("CARD", "img/Cards/Earth Dragon.png");;
        getGame().getAssetManager().loadAndAddBitmap("CARD1", "img/Cards/Knight of Fire - Spirit.png");
        cardBitmapReference = new ArrayList<>();
        cardBitmapReference.add("CARD");
        cardBitmapReference.add("CARD");
        cardBitmapReference.add("CARD");
        cardBitmapReference.add("CARD1");
        cardBitmapReference.add("CARD1");
        cardBitmapReference.add("CARD1");

    }
    //Load button bitmaps from the asset manager.
    public void loadBitmaps(){
        getGame().getAssetManager().loadAndAddBitmap("Right","img/DeckManagement/Right.png");
        getGame().getAssetManager().loadAndAddBitmap("Previous","img/DeckManagement/Previous.png");
        getGame().getAssetManager().loadAndAddBitmap("Unimon","img/DeckManagement/Unimon.png");
        getGame().getAssetManager().loadAndAddBitmap("Stamina","img/DeckManagement/Stamina.png");
    }

    //FOR SORTING WHEN TAKEN IN FROM JSON AND ARRANGING INTO ARRAYS;
/*
    public void searchingForUnimon(){
        unimonCards = new ArrayList<UnimonCard>();
        energyCards = new ArrayList<EnergyCard>();
        for(Card item: allCards){
            if(instanceCheck(item)){
                unimonCards.add((UnimonCard)(item));
            }else{
                energyCards.add((EnergyCard)(item));
            }
        }
    }

    public boolean instanceCheck(Card card){
        if(card instanceof UnimonCard){
            return true;
        }
        return false;
    }

    int partitionByStamina(ArrayList<UnimonCard>unimonCards, int left, int right)
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
                tmp = unimonCards.get(i);
                unimonCards.remove(i);
                unimonCards.add(i,unimonCards.get(j));
                unimonCards.remove(j);
                unimonCards.add(j,tmp);
                i++;
                j--;
            }
        };

        return i;
    }

    void quickSortByStamina(ArrayList<UnimonCard>unimonCards, int left, int right) {
        int index = partitionByStamina(unimonCards, left, right);
        if (left < index - 1)
            quickSortByStamina(unimonCards, left, index - 1);
        if (index < right)
            quickSortByStamina(unimonCards, index, right);
    }
*/


}
