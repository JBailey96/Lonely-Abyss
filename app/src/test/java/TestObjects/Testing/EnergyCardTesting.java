package TestObjects.Testing;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
import uk.ac.qub.eeecs.gage.world.GameScreen;

import static junit.framework.Assert.assertEquals;
/**
 * Created by Patrick on 02/03/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class EnergyCardTesting {

    @Mock
    GameScreen aScreen = Mockito.mock(GameScreen.class);
    /*EnergyCard EnergyCard = Mockito.mock(EnergyCard.class);*/


    @Test
    //Patrick Conway 40150555
    public void applyEnergyWithMana_Test() {

        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.HAND);

        Map<UnimonEvolveType, Map<EnergyType, Integer>> energy = new HashMap<>();
        Map<EnergyType, Integer> check = new HashMap<>();
        check.put(EnergyType.MANA, 20);
        energy.put(UnimonEvolveType.PHANTOM, check);

        EnergyCard energyCard = new EnergyCard(0, 0, 0, 0, null, aScreen, "2", null, "null", EnergyType.MIXED, energy,"null",true,Container.HAND );

        player.decreaseMana(50);
        energyCard.applyEnergy(player);
        int expected = 370;
        int actual = player.getMana();
        assertEquals(expected, actual);
    }

    @Test
    //Patrick Conway 40150555
    public void applyEnergyWithManaAndHealth_Test() {

        UnimonCard player = new UnimonCard(0, 0, 0, 0, null, aScreen,
                "1", null, null, null, "FireCard",
                UnimonEvolveType.PHANTOM, Element.FIRE, null, 100, 400, 250, null,
                30, 15, Element.WATER, 35, Element.FIRE, true, Container.HAND);

        Map<UnimonEvolveType, Map<EnergyType, Integer>> energy = new HashMap<>();
        Map<EnergyType, Integer> check = new HashMap<>();
        check.put(EnergyType.MANA, 20);
        check.put(EnergyType.HEALTH,20);
        energy.put(UnimonEvolveType.PHANTOM, check);

        EnergyCard energyCard = new EnergyCard(0, 0, 0, 0, null, aScreen, "2", null, "null", EnergyType.MIXED, energy,"null",true,Container.HAND );

        player.decreaseMana(50);
        player.decreaseHealth(50);
        energyCard.applyEnergy(player);
        int expectedMana = 370;
        int actualMana = player.getMana();
        assertEquals(expectedMana, actualMana);

        int expectedHealth = 70;
        int actualHealth = player.getHealth();
        assertEquals(expectedHealth,actualHealth);
    }


}
