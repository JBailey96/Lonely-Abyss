package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.Player;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.LevelCreator.PlayScreen.PlayScreen;

/**
 * Created by James on 11/04/2017.
 */

public class Battle {
    //the quantity of cards a battle setup has
    protected static final int numHandCards = 5;
    protected static final int numBenchCardsBeforePlay = 4;
    protected static final int numBenchCardsInPlay = 3;
    protected static final int numPrizeCards = 3;

    //James Bailey 40156063
    //draw a card from the player's battlesetup deck
    public static int drawFromDeck(BattleSetup battleSetup) {
        Card[] playerHandCards = battleSetup.getHandCard();
        Stack<Card> playerDeck = battleSetup.getPlayAreaDeck();

        int indexToAdd = -1; //the index of the card drawn from the deck to add to the list of hand cards
        boolean emptyHandCardPresent = false; //if there is an opening for the new hand card

        for (int i = 0; i < playerHandCards.length; i++) {
            if (playerHandCards[i] == null) { //an empty hand card
                indexToAdd = i; //the index of the handcard to be added
                emptyHandCardPresent = true; //there is an opening
                break; //adds to the first opening - no need to iterate further
            }
        }

        if (!playerDeck.isEmpty() && emptyHandCardPresent) { //there is a card to draw (deck is not empty) and an opening for a new hand card
            Card handCardToAdd = playerDeck.pop(); //draw the card from the top of the deck
            playerHandCards[indexToAdd] = handCardToAdd; //add the drawn card to the hand card list
        } else {
            indexToAdd = -1; //there is no opening for the hand card or the deck is empty.
        }

        return indexToAdd;
    }

    //James Bailey 40156063
    //checks whether removing the card is a valid move
    public static void removeFromHand(Card[] playerHandCards, int indexToRemove) {
        if (0 > indexToRemove && indexToRemove >= playerHandCards.length) { //validates the index of the hand card to remove
            return;
        }

        if (playerHandCards[indexToRemove] == null) { //validates whether the hand card is empty
            return;
        }

        playerHandCards[indexToRemove] = null; //sets the hand card to empty (null)
    }

    //James Bailey 40156063
    //Method that retreats the active unimon card and replace with a card on the bench, placing the replaced active unimon card onto the bench
    public static void retreatUnimon(BattleSetup battleSetup, UnimonCard[] benchCards, int indexNewActive) {
        if (0 > indexNewActive && indexNewActive >= benchCards.length) { //validates the index of the bench card to make active
            return;
        }

        UnimonCard benchCardChosen = benchCards[indexNewActive].copy(); //take the bench card that will replace the current active card
        benchCards[indexNewActive] = null; //make the benchCard empty, to be filled by the current active card

        UnimonCard copyActiveCard = battleSetup.getActiveCard().copy(); //take the current active card
        benchCards[indexNewActive] = copyActiveCard; //put the current active card on the bench

        battleSetup.setActiveCard(benchCardChosen); //set the battlesetup's active card to the bench card chosen
        battleSetup.setBenchCards(benchCards); //sets the battlesetup's benchcards to the new updated benchcard list
    }

    //James Bailey 40156063
    //Method that sets the active unimon card to be used initially in play.
    public static void chooseActive(BattleSetup battleSetup, UnimonCard[] benchCards, int indexActive) {
        UnimonCard activeCard = benchCards[indexActive].copy(); //the active unimon card took from the bench cards
        benchCards[indexActive] = null; //set that bench card to empty

        battleSetup.setActiveCard(activeCard); //set the active card to the chosen bench card

        UnimonCard[] newBenchCards = new UnimonCard[numBenchCardsInPlay]; //create a new array - adjusting for the size of bench in play

        int index = 0; //the first index to add unimon cards to the bench


        //iterate through the bench cards that have had a card removed and if they are not empty, add them to the new bench card list
        for (UnimonCard benchCard : benchCards) {
            if (benchCard != null) {
                newBenchCards[index] = benchCard.copy();
                index++;
            }
        }

        battleSetup.setBenchCards(newBenchCards); //updates the battlesetup's benchcards
    }

    //James Bailey 40156063
    //Method that applies the a UnimonMove from the player card to the opponent's card
    public static void applyMove(UnimonCard playerCard, UnimonCard opponentCard, UnimonMoves unimonMove) {
        unimonMove.doMove(playerCard, opponentCard, unimonMove);
    }

}
