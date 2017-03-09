package TestObjects.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.MoveResource;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.MoveType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import static junit.framework.Assert.assertEquals;
/**
 * Created by Patrick on 04/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UnimonMoveTesting {

    @Mock
    GameScreen aScreen = Mockito.mock(GameScreen.class);



    @Test
    public void physicalAttack_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.PHYSICAL);

        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        move.physicalAttack(opponent);
        int expected = 0;
        int actual = opponent.getHealth();
        assertEquals(expected, actual);
    }

    @Test
    public void checkAbsorptionTrue_Test(){

        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        boolean expected = true;
        boolean actual = move.checkAbsorption(player,opponent);
        assertEquals(expected,actual);

    }

    @Test
    public void checkAbsorptionFalse_Test(){

        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.WATER, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        boolean expected = false;
        boolean actual = move.checkAbsorption(player,opponent);
        assertEquals(expected,actual);

    }

    @Test
    public void checkWeaknessTrue_Test(){

        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.WATER, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        boolean expected = true;
        boolean actual = move.checkWeakness(player,opponent);
        assertEquals(expected,actual);

    }

    @Test
    public void checkWeaknessFalse_Test(){

        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        boolean expected = false;
        boolean actual = move.checkWeakness(player,opponent);
        assertEquals(expected,actual);

    }

    @Test
    public void elementalAttack_checkWeakness_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,2,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.WATER, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int expected = 70;
        move.elementalAttack(player,opponent);
        int actual = opponent.getHealth();
        assertEquals(expected,actual);
    }

    @Test
    public void elementalAttack_checkAbsorption_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        opponent.decreaseHealth(85);
        int expected = 78;
        move.elementalAttack(player,opponent);
        int actual = opponent.getHealth();
        assertEquals(expected,actual);
    }

    @Test
    public void elementalAttack_False_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,70,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.HOLY, true, Container.ACTIVE);

        int expected = 30;
        move.elementalAttack(player,opponent);
        int actual = opponent.getHealth();
    }

    @Test
    public void selectAttack_Physical_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,70,MoveType.PHYSICAL);


        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        move.selectAttack(player,opponent,move);
        int expected = 51;
        int actual = opponent.getHealth();
        assertEquals(expected, actual);


    }
    @Test
    public void selectAttack_Elemental_Test(){
            Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
            moveVal.put(MoveResource.MANA,50);
            UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.ELEMENTAL);


            UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                    "1", null, null, null, "FireCard",
                    UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                    30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

            UnimonCard opponent = new UnimonCard(0, 0, 0, 0, null, aScreen,
                    "1", null, null, null, "FireCard",
                    UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                    30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);


            opponent.decreaseHealth(85);
            int expected = 78;
            move.selectAttack(player,opponent,move);
            int actual = opponent.getHealth();
            assertEquals(expected,actual);
        }

    @Test
    public void checkingStamina_True_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.STAMINA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.ELEMENTAL);

        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        boolean expected = true;
        boolean actual = move.checkingStamina(player,move);
        assertEquals(expected,actual);



    }
    @Test
    public void checkingStamina_False_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.ELEMENTAL);

        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        boolean expected = false;
        boolean actual = move.checkingStamina(player,move);
        assertEquals(expected,actual);
    }
    @Test
    public void checkingMana_True_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.MANA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.ELEMENTAL);

        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        boolean expected = true;
        boolean actual = move.checkingMana(player,move);
        assertEquals(expected,actual);
    }

    @Test
    public void checkingMana_False_Test(){
        Map<MoveResource,Integer> moveVal = new HashMap<MoveResource,Integer>();
        moveVal.put(MoveResource.STAMINA,50);
        UnimonMoves move = new UnimonMoves("FirstMove", null,"null",moveVal,180,MoveType.ELEMENTAL);

        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        boolean expected = false;
        boolean actual = move.checkingMana(player,move);
        assertEquals(expected,actual);
    }


}






