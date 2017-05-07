package uk.ac.qub.eeecs.LonelyAbyss.DiscontinuedCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.MoveResource;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Card;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;

/**
 * Created by James on 12/03/2017.
 */

public class JSONWriter {

    //James Bailey 40156063
    //converts a card object to a JSON object that can be written to a JSON file.
    public static JSONObject processCardObject(Card card) throws JSONException {
        JSONObject cardJSONObj = new JSONObject(); //the generic json object for fields to be put in
        cardJSONObj = createJSONCardObject(cardJSONObj, card); //put in generic details about the card.

        //validates the type of card and creates the json object of it.
        if (card instanceof UnimonCard) {
            cardJSONObj = createJSONUnimonCardObject(cardJSONObj, (UnimonCard) card);
        } else if (card instanceof EnergyCard) {
            cardJSONObj = createJSONEnergyCardObject(cardJSONObj, (EnergyCard) card);
        }

        return cardJSONObj; //the processed card object
    }

    //James Bailey 40156063
    //put in generic fields that every type of card has to the json object.
    private static JSONObject createJSONCardObject(JSONObject obj, Card card) throws JSONException {
        obj.put("ID", card.getID());
        obj.put("Name", card.getName());
        obj.put("Description", card.getDescription());

        return obj;
    }

    //James Bailey 40156063
    //Put in the fields unique to a unimon card type to the json object.
     private static JSONObject createJSONUnimonCardObject(JSONObject obj, UnimonCard uniCard) throws JSONException {
        obj.put("EvolveType", uniCard.getEvolveType().toString());
        obj.put("Element", uniCard.getCardElement().toString());

        obj.put("Moves", processUnimonCardMoves(uniCard.getMoves()));

        obj.put("Health", Integer.toString(uniCard.getMaxHealth()));
        obj.put("Mana", Integer.toString(uniCard.getMaxMana()));
        obj.put("Stamina", Integer.toString(uniCard.getMaxStamina()));

        obj.put("Armour", Integer.toString(uniCard.getArmourValue()));

        obj.put("WeaknessElement", uniCard.getWeaknessElement().toString());
        obj.put("WeaknessVal", Float.toString(uniCard.getWeaknessValue()));

        obj.put("AbsorbElement", uniCard.getAbsorptionElement().toString());
        obj.put("AbsorbValue", uniCard.getAbsorptionValue());

        return obj;
    }

    //James Bailey 40156063
    //Put in the fields unique to an energy card type to the json object
    private static JSONObject createJSONEnergyCardObject(JSONObject obj, EnergyCard energyCard) throws JSONException {
        obj.put("EnergyType", energyCard.getType().toString());
        obj.put("EnergyCardEff", processEnergyCardEffects(energyCard.getEnergy()));
        return obj;
    }

    //James Bailey 40156063
    //Processes each of the energy card's energy effects and puts them into the JSON object
    private static JSONArray processEnergyCardEffects(Map<UnimonEvolveType,Map<EnergyType, Integer>> energyEff) throws JSONException {
        JSONArray  energyEffJSONArray = new JSONArray(); //holds the list of energy effects
        JSONObject energyEvolveTypeJSONObj = new JSONObject(); //holds the effects for a specific energy evolve type

        //iterate through each of the card's energy effects for a specific evolve type
        for (UnimonEvolveType evolveType: energyEff.keySet()) {
            JSONObject evolveTypeJSON = new JSONObject(); //holds the evolve type
            Map<EnergyType, Integer> evolveTypeEff = energyEff.get(evolveType); //holds the energy type and effect quantifier

            //process the effect for each stat type
            String health = processEnergyCardTypeEff(EnergyType.HEALTH, evolveTypeEff);
            String mana = processEnergyCardTypeEff(EnergyType.MANA, evolveTypeEff);
            String stamina = processEnergyCardTypeEff(EnergyType.STAMINA, evolveTypeEff);

            //put the health type and quantifier into the holding JSON object
            evolveTypeJSON.put("Health", health);
            evolveTypeJSON.put("Mana", mana);
            evolveTypeJSON.put("Stamina", stamina);

            //add the energy effects to the list of energy effects for the evolve type.
            energyEvolveTypeJSONObj.put(evolveType.toString(), evolveTypeJSON);
            energyEffJSONArray.put(energyEvolveTypeJSONObj);
        }

        return energyEffJSONArray;
    }

    //James Bailey 40156063
    //Processes the moves a unimon card has, including the effect on the opponent's card and the requirements.
    private static JSONArray processUnimonCardMoves(UnimonMoves[] uniMoves) throws JSONException {
        JSONArray movesJSONArray = new JSONArray(); //holds the list of JSON unimon card's moves

        //iterate through the unimon card's moves and add them to the JSON object
        for (UnimonMoves move: uniMoves) {
            JSONObject moveJSON = new JSONObject(); //holds the single unimon move
            moveJSON.put("ID", move.getID());
            moveJSON.put("Name", move.getName());
            moveJSON.put("MoveType", move.getMoveType().toString());

            //process the stat requirements
            String mana = processUnimonMovesReq(MoveResource.MANA, move.getMovesReq());
            String stamina = processUnimonMovesReq(MoveResource.STAMINA, move.getMovesReq());

            moveJSON.put("ManaReq", mana);
            moveJSON.put("StaminaReq", stamina);
            moveJSON.put("BaseDam", Integer.toString(move.getBaseDamage()));

            movesJSONArray.put(moveJSON); //add the move to the list of moves JSON array
        }
        return movesJSONArray; //return the list of moves in a JSON array
    }

    //James Bailey 40156063
    //return the stat value the move requires
    private static String processUnimonMovesReq(MoveResource moveRes, Map<MoveResource, Integer> moveReq) {
        if (moveReq.containsKey(moveRes)) { //validates whether the stat type is in the move requirements.
            int requirementValue = moveReq.get(moveRes);
            return Integer.toString(requirementValue);
        }
        return "-1"; //move does not have this stat type as a requirement
    }

    //James Bailey 40156063
    //return the energy effect value the energy card applies
    private static String processEnergyCardTypeEff(EnergyType energyType, Map<EnergyType, Integer> energyEff) {
        if (energyEff.containsKey(energyType)) { //validates whether the stat type is in the energy effect list
            int energyEffVal = energyEff.get(energyType);
            return Integer.toString(energyEffVal);
        }
        return "-1"; //move does not apply this stat type
    }
}
