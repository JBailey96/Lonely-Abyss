package TestObjects.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Stack;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Battle;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player.BattleSetup;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Player.Player;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import static junit.framework.Assert.assertEquals;
/**
 * Created by James on 04/05/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class BattleTest {
    final int numHandCards = 5;
    final int numPrizeCards = 3;
    final int numBenchCards = 4;

    @Mock
    GameScreen aScreen = Mockito.mock(GameScreen.class);

    //James Bailey 40156063
    //Setup a dummy battlesetup to use in tests.
    public BattleSetup setupBattleSetup() {
        //set up a deck with 3 elements of each type of card added to it.
        Stack<Card> testDeck = new Stack();

        for (int i = 0; i < 3; i++) {
            testDeck.push(Mockito.mock(UnimonCard.class));
            testDeck.push(Mockito.mock(EnergyCard.class));
        }

        //empty arrays that are required by the battlesetup's parameters.
        UnimonCard[] dummyBenchCards = new UnimonCard[numBenchCards];
        UnimonCard[] dummyPrizeCards = new UnimonCard[numPrizeCards];


        //set up dummy hand cards filled with mock card objects.
        Card[] dummyHandCards = new Card[numHandCards];

        for (int i = 0; i < 3; i++) {
            dummyHandCards[i] = Mockito.mock(Card.class);
        }

        UnimonCard dummyActiveCard = Mockito.mock(UnimonCard.class);

        //create the test BattleSetup
        BattleSetup testBattleSetup = new BattleSetup(testDeck, dummyBenchCards, dummyPrizeCards);
        testBattleSetup.setHandCard(dummyHandCards);
        testBattleSetup.setActiveCard(dummyActiveCard);


        return testBattleSetup;
    }

    public UnimonCard setupUnimonCard() {
        UnimonCard activeCard = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.HAND);

        return activeCard;

    }

    //James Bailey 40156063
    //test drawing a card from a deck that has hand cards filled from indexes 0-2. (null (empty) beyond that).
    @Test
    public void drawingCardFromDeck() {
        BattleSetup testBattleSetup = setupBattleSetup();

        final int expectedIndex = 3;
        int index = Battle.drawFromDeck(testBattleSetup);

        assertEquals(expectedIndex, index);
    }

    //James Bailey 40156063
    //test drawing a card from a deck with no elements.
    @Test
    public void drawingCardFromEmptyDeck() {
        BattleSetup testBattleSetup = setupBattleSetup();

        //set up an empty deck and set it as the battlesetup's deck
        Stack<Card> emptyDeck = new Stack<>();
        testBattleSetup.setPlayAreaDeck(emptyDeck);

        final int expectedIndex = -1;
        int index = Battle.drawFromDeck(testBattleSetup);

        assertEquals(expectedIndex, index);
    }

    //James Bailey 40156063
    //test drawing a card from a deck to hand cards with no opening (entire list is filled with elements that are not null).
    @Test
    public void drawingCardWhileHandFull() {
        BattleSetup testBattleSetup = setupBattleSetup();
        Card[] dummyHandCards = testBattleSetup.getHandCard();

        //fill the remainder of hand cards in the battlesetup in order to have a full list of hand cards.
        for (int i = 3; i < numHandCards; i++) {
            dummyHandCards[i] = Mockito.mock(Card.class);
        }

        final int expectedIndex = -1;
        int index = Battle.drawFromDeck(testBattleSetup);

        assertEquals(expectedIndex, index);
    }

    //James Bailey 40156063
    //test that the losing condition of a player not being able to draw from the deck is handled
    @Test
    public void playerLostEmptyDeck() {
        Player player = new Player();

        BattleSetup testBattleSetup = setupBattleSetup();

        //set up an empty deck and set it as the player's deck in the battlesetup
        Stack<Card> emptyDeck = new Stack();
        testBattleSetup.setPlayAreaDeck(emptyDeck);

        player.setPlayerBattleSetup(testBattleSetup);

        final boolean expectedLoseStatus = true;
        boolean playerLosingStatus = Battle.hasPlayerLost(player);
        assertEquals(expectedLoseStatus, playerLosingStatus);
    }

    //James Bailey 40156063
    //test that the losing condition of a player losing all three of their prize cards is handled
    @Test
    public void playerLost3PrizeCards() {
        Player player = new Player();
        BattleSetup testBattleSetup = setupBattleSetup();

        //create a card lost list with 3 elements and set it to the battlesetup's list of prize cards cards lost.
        Card[] testCardsLost = new Card[numPrizeCards];
        for (int i = 0; i < numPrizeCards; i++) {
            testCardsLost[i] = Mockito.mock(Card.class);
        }

        testBattleSetup.setCardsLost(testCardsLost);

        player.setPlayerBattleSetup(testBattleSetup);

        final boolean expectedLoseStatus = true;
        boolean playerLosingStatus = Battle.hasPlayerLost(player);
        assertEquals(expectedLoseStatus, playerLosingStatus);
    }

    //James Bailey 40156063
    //test that the losing conditions are not fulfilled
    //player has a deck stack that has a number of card objects
    //player has no cards lost (list of cards lost is empty)
    @Test
    public void playerNotLost() {
        Player player = new Player();
        BattleSetup testBattleSetup = setupBattleSetup();

        //create a card lost list with 3 elements but no elements initalised, all set to null (empty)
        Card[] testCardsLost = new Card[numPrizeCards];

        testBattleSetup.setCardsLost(testCardsLost);

        player.setPlayerBattleSetup(testBattleSetup);

        final boolean expectedLoseStatus = false;
        boolean playerLosingStatus = Battle.hasPlayerLost(player);
        assertEquals(expectedLoseStatus, playerLosingStatus);
    }

    //James Bailey 40156063
    //tests that a unimon card object being in the deck stack returns true.
    @Test
    public void unimonCardInHand() {
        //Create a list of hand cards and add energy card objects
        Card[] testHandCards = new Card[numHandCards];

        for (int i = 0; i < numHandCards - 1; i++) {
            testHandCards[i] = Mockito.mock(EnergyCard.class);
        }

        testHandCards[numHandCards - 1] = Mockito.mock(UnimonCard.class); //adds an unimon card at the end of the list of hand cards

        final boolean expectedResult = true;
        boolean result = Battle.checkUnimonCardInHand(testHandCards);
        assertEquals(expectedResult, result);
    }

    //James Bailey 40156063
    //tests that an energy card object being in the deck stack returns true.
    @Test
    public void energyCardInHand() {
        //Create a list of hand cards and add unimon card objects
        Card[] testHandCards = new Card[numHandCards];

        for (int i = 0; i < numHandCards - 1; i++) {
            testHandCards[i] = Mockito.mock(UnimonCard.class);
        }
        testHandCards[numHandCards - 1] = Mockito.mock(EnergyCard.class); //adds an energy card at the end of the list of hand cards

        final boolean expectedResult = true;
        boolean result = Battle.checkEnergyCardInHand(testHandCards);
        assertEquals(expectedResult, result);
    }

    //James Bailey 40156063
    //tests that with no cards in the player's hand the checks for unimon and energy cards returns none exist (false)
    @Test
    public void noCardInHand() {
        Card[] testHandCards = new Card[numHandCards];

        final boolean expectedResult = false;
        boolean noEnergyResult = Battle.checkEnergyCardInHand(testHandCards);
        boolean noUnimonResult = Battle.checkUnimonCardInHand(testHandCards);

        assertEquals(expectedResult, noEnergyResult || noUnimonResult);
    }

    //James Bailey 40156063
    //test whether with no energy card in the player's hand the method to check for energy cards existing returns none exist (false)
    @Test
    public void noEnergyCardInHand() {
        Card[] testHandCards = new Card[numHandCards];

        for (int i = 0; i < numHandCards; i++) {
            testHandCards[i] = Mockito.mock(UnimonCard.class);
        }

        final boolean expectedResult = false;
        boolean result = Battle.checkEnergyCardInHand(testHandCards);

        assertEquals(expectedResult, result);
    }

    //James Bailey 40156063
    //test whether with no unimon card in the player's hand the method to check for unimon card existing returns none exist (false)
    @Test
    public void noUnimonCardInHand() {
        Card[] testHandCards = new Card[numHandCards];

        for (int i = 0; i < numHandCards; i++) {
            testHandCards[i] = Mockito.mock(EnergyCard.class);
        }

        final boolean expectedResult = false;
        boolean result = Battle.checkUnimonCardInHand(testHandCards);

        assertEquals(expectedResult, result);
    }

    //James Bailey 40156063
    //test whether with the hand card being a greater evolution of the player's active card the method returns it is a valid evolve (true)
    @Test
    public void handCardIsEvolveActive() {
        UnimonCard dummyActiveCard = setupUnimonCard();
        UnimonCard dummyHandCard = setupUnimonCard();

        //set the evolve types, phantom is an evolution of spirit.
        dummyActiveCard.setEvolveType(UnimonEvolveType.SPIRIT);
        dummyHandCard.setEvolveType(UnimonEvolveType.PHANTOM);

        boolean expectedResult = true;
        boolean result = Battle.handCardisEvolve(dummyHandCard, dummyActiveCard);

        assertEquals(expectedResult, result);
    }

    //James Bailey 40156063
    //test whether with the hand card being a lesser evolution of the player's active card the method returns it's not a valid evolve (false)
    @Test
    public void handCardIsNotEvolveActive() {
        UnimonCard dummyActiveCard = setupUnimonCard();
        UnimonCard dummyHandCard = setupUnimonCard();

        //set the evolve types, phantom is not an evolution of unique.
        dummyActiveCard.setEvolveType(UnimonEvolveType.UNIQUE);
        dummyHandCard.setEvolveType(UnimonEvolveType.PHANTOM);

        boolean expectedResult = false;
        boolean result = Battle.handCardisEvolve(dummyHandCard, dummyActiveCard);

        assertEquals(expectedResult, result);
    }
}






