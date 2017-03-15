package uk.ac.qub.eeecs.gage.engine.io;

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
 * Created by appcamp on 12/03/2017.
 */

public class JSONWriter {

    public static JSONObject processCardObject(Card card) throws JSONException {
        JSONObject cardJSONObj = new JSONObject();
        cardJSONObj = createJSONCardObject(cardJSONObj, card);

        if (card instanceof UnimonCard) {
            cardJSONObj = createJSONUnimonCardObject(cardJSONObj, (UnimonCard) card);
        } else if (card instanceof EnergyCard) {
            cardJSONObj = createJSONEnergyCardObject(cardJSONObj, (EnergyCard) card);
        }

        return cardJSONObj;
    }

    public static JSONObject createJSONCardObject(JSONObject obj, Card card) throws JSONException {
        obj.put("ID", card.getID());
        obj.put("Name", card.getName());
        obj.put("Description", card.getDescription());

        return obj;
    }

    public static JSONObject createJSONUnimonCardObject(JSONObject obj, UnimonCard uniCard) throws JSONException {
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
    public static JSONObject createJSONEnergyCardObject(JSONObject obj, EnergyCard energyCard) throws JSONException {
        obj.put("EnergyType", energyCard.getType().toString());
        obj.put("EnergyCardEff", processEnergyCardEffects(energyCard.getEnergy()));
        return obj;
    }

    public static JSONArray processEnergyCardEffects(Map<UnimonEvolveType,Map<EnergyType, Integer>> energyEff) throws JSONException {
        JSONArray  energyEffJSONArray = new JSONArray();
        JSONObject energyEvolveTypeJSONObj = new JSONObject();

        for (UnimonEvolveType evolveType: energyEff.keySet()) {
            JSONObject evolveTypeJSON = new JSONObject();
            Map<EnergyType, Integer> evolveTypeEff = energyEff.get(evolveType);

            String health = processEnergyCardTypeEff(EnergyType.HEALTH, evolveTypeEff);
            String mana = processEnergyCardTypeEff(EnergyType.MANA, evolveTypeEff);
            String stamina = processEnergyCardTypeEff(EnergyType.STAMINA, evolveTypeEff);

            evolveTypeJSON.put("Health", health);
            evolveTypeJSON.put("Mana", mana);
            evolveTypeJSON.put("Stamina", stamina);

            energyEvolveTypeJSONObj.put(evolveType.toString(), evolveTypeJSON);
            energyEffJSONArray.put(energyEvolveTypeJSONObj);
        }

        return energyEffJSONArray;
    }

    public static JSONArray processUnimonCardMoves(UnimonMoves[] uniMoves) throws JSONException {
        JSONArray movesJSONArray = new JSONArray();

        for (UnimonMoves move: uniMoves) {
            JSONObject moveJSON = new JSONObject();
            moveJSON.put("ID", move.getID());
            moveJSON.put("Name", move.getName());
            moveJSON.put("MoveType", move.getMoveType().toString());

            String mana = processUnimonMovesReq(MoveResource.MANA, move.getMovesReq());
            String stamina = processUnimonMovesReq(MoveResource.STAMINA, move.getMovesReq());
            moveJSON.put("ManaReq", mana);
            moveJSON.put("StaminaReq", stamina);
            moveJSON.put("BaseDam", Integer.toString(move.getBaseDamage()));
            movesJSONArray.put(moveJSON);
        }

        return movesJSONArray;
    }

    public static String processUnimonMovesReq(MoveResource moveRes, Map<MoveResource, Integer> moveReq) {
        if (moveReq.containsKey(moveRes)) {
            int requirementValue = moveReq.get(moveRes);
            return Integer.toString(requirementValue);
        }
        return "-1";
    }

    public static String processEnergyCardTypeEff(EnergyType energyType, Map<EnergyType, Integer> energyEff) {
        if (energyEff.containsKey(energyType)) {
            int energyEffVal = energyEff.get(energyType);
            return Integer.toString(energyEffVal);
        }
        return "-1";
    }
}
