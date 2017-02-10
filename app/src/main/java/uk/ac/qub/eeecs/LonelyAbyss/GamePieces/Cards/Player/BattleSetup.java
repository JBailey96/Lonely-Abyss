package uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player;

import java.util.LinkedList;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;

/**
 * Created by appcamp on 05/02/2017.
 */

public class BattleSetup {
    private Stack<Card> playAreaDeck;

    private Card[] handCard = new Card[5];
    private Card[] prizeCards = new Card[10];
    private Card[] graveyardCards = new Card[3];
    private Card[] benchCards = new Card[3];
    private Card[] cardsWon = new Card[3];
    private Card[] cardsLost = new Card[3];

    private UnimonCard activeCard;

    public BattleSetup(Stack<Card> playAreaDeck, UnimonCard activeCard, Card[] benchCards, Card[] prizeCards) {
        this.playAreaDeck = playAreaDeck;
        this.activeCard = activeCard;
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

    public Card[] getPrizeCards() {
        return prizeCards;
    }

    public void setPrizeCards(Card[] prizeCards) {
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

    public Card[] getBenchCards() {
        return benchCards;
    }

    public void setBenchCards(Card[] benchCards) {
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
