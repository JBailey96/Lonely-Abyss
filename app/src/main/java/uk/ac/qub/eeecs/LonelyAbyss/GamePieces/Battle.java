package uk.ac.qub.eeecs.LonelyAbyss.GamePieces;

import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.Player;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
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

    //James Bailey 40156063
    //Method that checks whether the player has lost
    //Losing conditions
    public static boolean hasPlayerLost(Player player) {
        if (player.getPlayerBattleSetup().getPlayAreaDeck().size() == 0) {
            return true;
        } else if (player.getPlayerBattleSetup().getCardsLost().length == 3) {
            return true;
        }
        return false;
    }

    //James Bailey 40156063
    //Method that checks whether there is at least one energy card in the player's hand cards
    public static boolean checkEnergyCardInHand(Card[] handCards) {
        for (Card handCard: handCards) { //iterate through the list of handcards
            if (handCard != null) { //validates the hand card exists
                if (handCard instanceof EnergyCard) { //validates the hand card is of type energy card
                    return true; //there exists an energy card in the player's hand cards
                }
            }
        }
        return false; //there does not exist an energy card in the player's hand cards
    }

    //James Bailey 40156063
    //Method that checks whether there is at least one unimon card in the player's hand cards
    public static boolean checkUnimonCardInHand(Card[] handCards, UnimonCard activeCard) {
        for (int i = 0; i < handCards.length; i++) {
            Card handCard = handCards[i];

            if (handCard != null) {
                if (handCard instanceof UnimonCard) {
                    return true;
                }
            }
        }
        return false;
    }

    //James Bailey 40156063
    //Checks whether the hand card is an evolution of the player's active Unimoncard
    //Unimon cards can only evolve one step from its own evolve type
    //Spirit > Phantom > Demon > Unique
    public static boolean handCardisEvolve(UnimonCard handCard, UnimonCard activeCard) {
        if (activeCard.getEvolveType() == UnimonEvolveType.UNIQUE) {
            return false; //unique evolve type is the highest evolve type, cannot be evolved further.
        }

        //validates whether the active card and hand card share the same name
        if (activeCard.getName().equals(handCard.getName())) {
            if (activeCard.getEvolveType() == UnimonEvolveType.SPIRIT) {
                if (handCard.getEvolveType() == UnimonEvolveType.PHANTOM) {
                    return true;
                }
            } else if (activeCard.getEvolveType() == UnimonEvolveType.PHANTOM) {
                if (handCard.getEvolveType() == UnimonEvolveType.DEMON) {
                    return true;
                }
        }
        } else if (activeCard.getEvolveType() == UnimonEvolveType.DEMON) {
            //if the evolve is demon to unique the active and hand card do not need to share the same name.
            if (handCard.getEvolveType() == UnimonEvolveType.UNIQUE) {
                return true;
            }
        }
        return false; //the hand card is not an evolution of the player's active unimoncard
    }

    //James Bailey 40156063
    //Method that removes a hand card from the player's list of hand cards
    public static void discardHandCard(Card[] handCards, int indexSelectedCard, BattleSetup battleSetup) {
        if (removeFromHandValid(handCards, indexSelectedCard)) {
            handCards[indexSelectedCard] = null; //removes the hand card from the list of hand cards

            Card[] handCardsBattleSetup = new Card[numHandCards]; //create a new list of hand cards

            //iterate through and add copies of the current elements of list of hand cards to the newly created list.
            for (int i = 0; i < numHandCards; i++) {
                Card handCardtoCopy = handCards[i];

                if (handCardtoCopy != null) { //validates whether the hand card exists, otherwise leave as null
                    handCardsBattleSetup[i] = handCardtoCopy.copy();
                }
            }

            battleSetup.setHandCard(handCardsBattleSetup); //set the battlesetup list of hand cards to the newly created list
        }
    }

    //James Bailey 40156063
    //checks whether removing the card is a valid move
    public static boolean removeFromHandValid(Card[] playerHandCards, int indexToRemove) {
        if (0 > indexToRemove && indexToRemove >= playerHandCards.length) { //validates the index of the hand card to remove
            return false;
        }
        return true;
    }

    //James Bailey 40156063
    //Applies the selected energy card to the battlesetup's active card
    public static void applyEnergy(BattleSetup battleSetup, EnergyCard energyCard) {
        energyCard.applyEnergy(battleSetup.getActiveCard());
    }

    //James Bailey 40156063
    //Evolve the selected energy card to the battlesetup's active card
    public static void evolveUnimon(BattleSetup battleSetup, UnimonCard evolveToCard) {
        battleSetup.setActiveCard(evolveToCard);
    }


}
