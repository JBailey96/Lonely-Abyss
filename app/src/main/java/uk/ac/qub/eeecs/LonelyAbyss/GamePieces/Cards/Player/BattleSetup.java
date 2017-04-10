package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player;

import java.util.LinkedList;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;

/**
 * Created by James on 05/02/2017.
 */

public class BattleSetup {
    protected final int numberOfHandCards = 5;
    protected final int numberOfPrizeCards = 3;
    protected final int numberofGraveyardCards = 3;
    protected final int numberOfBenchCards = 5;
    protected final int maxCardsWon = 3;
    protected final int maxCardsLost = 3;


    private Stack<Card> playAreaDeck; //the deck of cards the player card draw from in the game
    private Card[] handCard = new Card[numberOfHandCards]; //the cards the player has in their hand - maximum of 5
    private UnimonCard[] prizeCards = new UnimonCard[numberOfPrizeCards]; //the cards the player gives up as prize cards the opponent can win
    private Card[] graveyardCards = new Card[numberofGraveyardCards]; //the cards that have been knocked out by the opponent, no longer can be active
    private UnimonCard[] benchCards = new UnimonCard[numberOfBenchCards]; //cards on the player's bench, can be subbed on as active
    private Card[] cardsWon = new Card[maxCardsWon]; //opponent's prize cards the player has won by knocking out one of their active unimon
    private Card[] cardsLost = new Card[maxCardsLost]; //prize cards that have been lost to the opponent

    private UnimonCard activeCard; // the player's active unimon Card

    public BattleSetup(Stack<Card> playAreaDeck, UnimonCard[] benchCards, UnimonCard[] prizeCards) {
        this.playAreaDeck = playAreaDeck;
        this.benchCards = benchCards;
        this.prizeCards = prizeCards;
    }

    public Stack<Card> getPlayAreaDeck() {
        return playAreaDeck;
    }

    public void setPlayAreaDeck(Stack<Card> playAreaDeck) {
        this.playAreaDeck = playAreaDeck;
    }

    public Card[] getHandCard() {
        return handCard;
    }

    public void setHandCard(Card[] handCard) {
        this.handCard = handCard;
    }

    public UnimonCard[] getPrizeCards() {
        return prizeCards;
    }

    public void setPrizeCards(UnimonCard[] prizeCards) {
        this.prizeCards = prizeCards;
    }

    public Card[] getGraveyardCards() {
        return graveyardCards;
    }

    public void setGraveyardCards(Card[] graveyardCards) {
        this.graveyardCards = graveyardCards;
    }

    public Card[] getCardsWon() {
        return cardsWon;
    }

    public void setCardsWon(Card[] cardsWon) {
        this.cardsWon = cardsWon;
    }

    public UnimonCard[] getBenchCards() {
        return benchCards;
    }

    public void setBenchCards(UnimonCard[] benchCards) {
        this.benchCards = benchCards;
    }

    public Card[] getCardsLost() {
        return cardsLost;
    }

    public void setCardsLost(Card[] cardsLost) {
        this.cardsLost = cardsLost;
    }

    public UnimonCard getActiveCard() {
        return activeCard;
    }

    public void setActiveCard(UnimonCard activeCard) {
        this.activeCard = activeCard;
    }
}
