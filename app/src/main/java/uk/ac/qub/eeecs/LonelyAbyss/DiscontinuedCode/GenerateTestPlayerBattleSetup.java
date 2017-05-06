package uk.ac.qub.eeecs.LonelyAbyss.DiscontinuedCode;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.gage.Game;

/**
 * Created by James on 06/05/2017.
 */

public class GenerateTestPlayerBattleSetup {
    protected Game mGame;
    protected BattleSetup playerBattleSetup;

    //James Bailey 40156063
    //Creates a test battle setup - loads in the cards used in the playarea
    public void createTestBattleSetup() {
        ArrayList<UnimonCard> unimonCards = mGame.getPlayer().getUnimonCards();
        ArrayList<EnergyCard> energyCards = mGame.getPlayer().getEnergyCards();


        //copy the player's battle setup
        ArrayList<UnimonCard> copyUnimonCards = new ArrayList<>();
        ArrayList<EnergyCard> copyEnergyCards = new ArrayList<>();

        for (UnimonCard unimonCard : unimonCards) {
            copyUnimonCards.add(unimonCard.copy());
        }

        for (EnergyCard energyCard : energyCards) {
            copyEnergyCards.add(energyCard.copy());
        }

        UnimonCard[] prizeCard = selectPrizeCards(copyUnimonCards);
        UnimonCard[] benchCard = createBenchCards(copyUnimonCards);
        Stack<Card> deck = createDeck(copyUnimonCards, copyEnergyCards);

        playerBattleSetup = new BattleSetup(deck, benchCard, prizeCard);
    }

    //James Bailey 40156063
    //Selects 3 random Unimon cards from the user's entire collection of cards to be used as prize cards
    public UnimonCard[] selectPrizeCards(ArrayList<UnimonCard> playerUnimonCards) {
        final int numberOfPrizeCards = 3;

        UnimonCard[] prizeCards = new UnimonCard[numberOfPrizeCards];

        Random random = new Random();
        for (int i = 0; i < numberOfPrizeCards; i++) {
            int randomIndex = random.nextInt(playerUnimonCards.size());
            UnimonCard randomUnimonCard = playerUnimonCards.get(randomIndex);
            prizeCards[i] = randomUnimonCard.copy();
            playerUnimonCards.remove(randomIndex);
        }

        return prizeCards;
    }

    //James Bailey 40156063
    //Selects random 3 Unimon cards from the user's entire collection of cards to be on the bench
    public UnimonCard[] createBenchCards(ArrayList<UnimonCard> playerUnimonCards) {
        final int numberOfBenchCardsBeforePlay = 4;

        UnimonCard[] benchCards = new UnimonCard[numberOfBenchCardsBeforePlay];

        Random random = new Random();
        for (int i = 0; i < numberOfBenchCardsBeforePlay; i++) {
            int randomIndex = random.nextInt(playerUnimonCards.size());
            UnimonCard randomUnimonCard = playerUnimonCards.get(randomIndex);
            benchCards[i] = randomUnimonCard.copy();
            playerUnimonCards.remove(randomIndex);
        }

        return benchCards;
    }


    //James Bailey 40156063
    //Selects a single active Unimon card - active Unimon
    public UnimonCard selectActiveCard(ArrayList<UnimonCard> playerUnimonCards) {
        int maxIndexPlayerUnimonCards = playerUnimonCards.size()-1;
        int randomIndex = (int) (Math.random() * maxIndexPlayerUnimonCards);
        UnimonCard activeCard = playerUnimonCards.get(randomIndex).copy();
        playerUnimonCards.remove(randomIndex);

        return activeCard;
    }

    //James Bailey 40156063
    //Creates a deck that will be used to take cards off
    public Stack<Card> createDeck(ArrayList<UnimonCard> playerUnimonCards, ArrayList<EnergyCard> energyCards) {
        Random random = new Random();
        Stack<Card> deck = new Stack<>(); //the deck that the cards are pushed to

        final int deck_size = 25;

        int randomUnimonIndex;
        UnimonCard randomUnimonCard;

        int randomEnergyIndex;
        EnergyCard randomEnergyCard;

        boolean arraysEmpty = false;
        while ((!arraysEmpty) && (deck_size > deck.size())) {
            int randomIndex = random.nextInt(2);
            if ((randomIndex == 0) && (playerUnimonCards.size() > 0)) {
                randomUnimonIndex = random.nextInt(playerUnimonCards.size());
                randomUnimonCard = playerUnimonCards.get(randomUnimonIndex);
                deck.push(randomUnimonCard.copy());
                playerUnimonCards.remove(randomUnimonIndex);
            } else if (energyCards.size() > 0) {
                randomEnergyIndex = random.nextInt(energyCards.size());
                randomEnergyCard = energyCards.get(randomEnergyIndex);
                deck.push(randomEnergyCard.copy());
                energyCards.remove(randomEnergyIndex);
            } else {
                arraysEmpty = true;
            }
        }

        return deck;
    }
}
