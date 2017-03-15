package TestObjects.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;

import uk.ac.qub.eeecs.gage.world.GameScreen;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Patrick on 27/02/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class UnimonCardTest{

    @Mock
    GameScreen aScreen = Mockito.mock(GameScreen.class);


    @Test
    public void increaingHealth_BeyondMaxHealth_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null,100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        int value = 20;
        int expected = (player.getHealth());
        player.increaseHealth(value);
        int actual = player.getHealth();
        assertEquals(expected,actual);
    }

    @Test
    public void increaingHealth_IncreasingHealth_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        player.setHealth(50);
        int value = 20;
        int expected = (player.getHealth() + value);
        player.increaseHealth(value);
        int actual = player.getHealth();
        assertEquals(expected,actual);
    }


    @Test
    public void decreaseHealth_DecreasingHealth_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        int value = 20;
        int expected = (player.getHealth() - value);
        player.decreaseHealth(value);
        int actual = player.getHealth() ;
        assertEquals(expected,actual);
    }

    @Test
    public void decreaseHealth_BeyondZero_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 0, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        int value = 20;
        int expected = (player.getHealth());
        player.decreaseHealth(value);
        int actual = player.getHealth() ;
        assertEquals(expected,actual);
    }

    @Test
    public void increaingMana_BeyondMaxMana_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null,100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        int value = 20;
        int expected = (player.getMana());
        player.increaseMana(value);
        int actual = player.getMana();
        assertEquals(expected,actual);
    }

    @Test
    public void increaingMana_IncreasingMana_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        player.setMana(350);
        int value = 20;
        int expected = (player.getMana() + value);
        player.increaseMana(value);
        int actual = player.getMana();
        assertEquals(expected,actual);
    }


    @Test
    public void decreaseMana_DecreasingMana_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);
        int value = 20;
        int expected = (player.getMana() - value);
        player.decreaseMana(value);
        int actual = player.getMana() ;
        assertEquals(expected,actual);
    }

    @Test
    public void decreaseMana_BeyondZero_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 0, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int value = 20;
        int expected = (player.getMana());
        player.decreaseMana(value);
        int actual = player.getMana() ;
        assertEquals(expected,actual);
    }

    @Test
    public void increaingStamina_BeyondMaxStamina_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null,100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int value = 20;
        int expected = (player.getStamina());
        player.increaseStamina(value);
        int actual = player.getStamina();
        assertEquals(expected,actual);
    }

    @Test
    public void increaingStamina_IncreasingStamina_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        player.setStamina(200);
        int value = 20;
        int expected = (player.getStamina() + value);
        player.increaseStamina(value);
        int actual = player.getStamina();
        assertEquals(expected,actual);
    }


    @Test
    public void decreaseStamina_DecreasingStamina_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int value = 20;
        int expected = (player.getStamina() - value);
        player.decreaseStamina(value);
        int actual = player.getStamina();
        assertEquals(expected,actual);
    }

    @Test
    public void decreaseStamina_BeyondZero_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100,400, 0, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int value = 20;
        int expected = (player.getStamina());
        player.decreaseStamina(value);
        int actual = player.getStamina() ;
        assertEquals(expected,actual);
    }

    @Test
    public void dead_MaxHealth_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100,400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        boolean expected = false;
        boolean actual = player.dead();
        assertEquals(expected,actual);
    }

    @Test
    public void dead_ZeroHealth_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 0,400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        boolean expected = true;
        boolean actual = player.dead();
        assertEquals(expected,actual);
    }

    @Test
    public void processArmourDAM_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null,100,400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int basedDamage = 180;
        int expected = (int) (180 * 0.7f);
        int actual = player.processArmourDAM(basedDamage);
        assertEquals(expected,actual);
    }

    @Test
    public void processWeaknessDAM_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null,100,400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int basedDamage = 2;
        int expected = (int) (2 * 15.0);
        int actual = player.processWeaknessDAM(basedDamage);
        assertEquals(expected,actual);
    }

    @Test
    public void processAbsorb_Test(){
        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null,100,400, 250, null,
                30,15, Element.WATER, 35, Element.FIRE, true, Container.ACTIVE);

        int basedDamage = 180;
        int expected = (int) (6300 / 100f);
        int actual = player.processAbsorb(basedDamage);
        assertEquals(expected,actual);
    }



    /*public UnimonMoves[] settingUpArray(){
        HashMap <MoveResource, Integer> mapper = new HashMap();
        mapper.put(MoveResource.STAMINA,85);
        UnimonMoves move = new UnimonMoves("attack",null,null,mapper,35,MoveType.PHYSICAL,null);

        UnimonMoves UM[] = new UnimonMoves[3];
        UM[0] = move;

        return UM;
    }*/

}
