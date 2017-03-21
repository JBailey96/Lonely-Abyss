package uk.ac.qub.eeecs.gage.engine.io;


import android.content.Context;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.MoveResource;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.MoveType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Moves.UnimonMoves;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.Element;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;
/**
 * Created by appcamp on 14/03/2017.
 */

public class JSONReader {
    private static JSONObject unprocessedCards = null;

    public static ArrayList<UnimonCard> loadJSONUnimon(Context context) throws JSONException, IOException {
        if (unprocessedCards == null) {
            unprocessedCards = loadJSONCards(context);
        }

        JSONArray unimonJsonCards = (JSONArray) unprocessedCards.get("Unimon");

        ArrayList<UnimonCard> processedUnimonCards = new ArrayList<>();

        for (int i = 0; i < unimonJsonCards.length(); i++) {
            JSONObject unimonCardToBeProc = (JSONObject) unimonJsonCards.get(i);
            UnimonCard procUnimonCard = processUnimonCard(unimonCardToBeProc);
            processedUnimonCards.add(procUnimonCard);
        }

        return processedUnimonCards;
    }

    private static JSONObject loadJSONCards(Context context) throws IOException, JSONException {
        InputStream is = context.getAssets().open("ClassObjectJSON/Cards.json");
        String json_str = IOUtils.toString(is);
        IOUtils.closeQuietly(is);

        JSONObject json = new JSONObject(json_str);


        JSONObject jsonCards = (JSONObject) json.get("Cards");
        return jsonCards;
    }

    private static UnimonCard processUnimonCard(JSONObject unimonCard) throws JSONException {
        String ID = (String) unimonCard.get("ID");

        String name = (String) unimonCard.get("Name");

        String str_evolveType = (String) unimonCard.get("EvolveType");
        UnimonEvolveType evolveType = UnimonEvolveType.valueOf(str_evolveType);

        String str_elementType = (String) unimonCard.get("Element");
        Element elementType = Element.valueOf(str_elementType);

        String str_health = (String) unimonCard.get("Health");
        int health = Integer.parseInt(str_health);

        String str_mana = (String) unimonCard.get("Mana");
        int mana = Integer.parseInt(str_mana);

        String str_stamina = (String) unimonCard.get("Stamina");
        int stamina = Integer.parseInt(str_stamina);

        JSONArray unimonMoveJSONArr = (JSONArray) unimonCard.get("Moves");
        UnimonMoves[] unimonMoves = processUnimonMoves(unimonMoveJSONArr);

        String str_armour = (String) unimonCard.get("Armour");
        int armourVal = Integer.parseInt(str_armour);

        String str_WeaknessElement = (String) unimonCard.get("WeaknessElement");
        Element weaknessElement = Element.valueOf(str_WeaknessElement);

        String str_WeaknessVal = (String) unimonCard.get("WeaknessVal");
        int weaknessVal = Integer.parseInt(str_WeaknessVal);

        String str_AbsorbElement = (String) unimonCard.get("AbsorbElement");
        Element absorbElement = Element.valueOf(str_AbsorbElement);

        String str_AbsorbVal = (String) unimonCard.get("AbsorbVal");
        int absorbVal = Integer.parseInt(str_AbsorbVal);

        UnimonCard unimonCardProcessed = new UnimonCard(0, 0, 0, 0, null, null, ID, null, null, null, name, evolveType, elementType, unimonMoves, health, mana, stamina, "", armourVal, weaknessVal, weaknessElement, absorbVal, absorbElement, false, Container.LOADED);
        return unimonCardProcessed;
    }

    private static UnimonMoves[] processUnimonMoves(JSONArray unimonMoves) throws JSONException {
        UnimonMoves[] processedUnimonMoves = new UnimonMoves[3];

        for (int i = 0; i < processedUnimonMoves.length; i++) {
            JSONObject unimonMovesToBeProc = (JSONObject) unimonMoves.get(i);
            String ID = (String) unimonMovesToBeProc.get("ID");

            String name = (String) unimonMovesToBeProc.get("Name");

            String str_MoveType = (String) unimonMovesToBeProc.get("MoveType");
            MoveType moveType = MoveType.valueOf(str_MoveType);

            Map<MoveResource, Integer> moveReq = processMoveReq(unimonMovesToBeProc);

            String str_basedDam = (String) unimonMovesToBeProc.get("BaseDam");
            int baseDam = Integer.parseInt(str_basedDam);

            UnimonMoves move = new UnimonMoves(ID, name, moveReq, baseDam, moveType);
            processedUnimonMoves[i] = move;
        }

        return processedUnimonMoves;
    }

    private static Map<MoveResource, Integer> processMoveReq(JSONObject unimonMovesToBeProc) throws JSONException {
        Map<MoveResource, Integer> moveReq = new HashMap<>();

        String str_manaReq = (String) unimonMovesToBeProc.get("ManaReq");
        int manaReq = Integer.parseInt(str_manaReq);

        String str_staminaReq = (String) unimonMovesToBeProc.get("StaminaReq");
        int staminaReq = Integer.parseInt(str_staminaReq);


        if (manaReq != -1) {
            moveReq.put(MoveResource.MANA, manaReq);
        }
        if (staminaReq != -1) {
            moveReq.put(MoveResource.STAMINA, manaReq);
        }

        return moveReq;
    }

    public static ArrayList<EnergyCard> loadJSONEnergy(Context context) throws JSONException, IOException{
        if(unprocessedCards == null){
            unprocessedCards = loadJSONCards(context);
        }

        JSONArray energyJsonCards = (JSONArray) unprocessedCards.get("Energy");

        ArrayList<EnergyCard> processedEnergyCards = new ArrayList<>();

        for(int i = 0; i < energyJsonCards.length(); i++){
            JSONObject energyCardsToBeProc = (JSONObject)energyJsonCards.get(i);
            EnergyCard procEnergyCards = processEnergyCard(energyCardsToBeProc);
            processedEnergyCards.add(procEnergyCards);
        }

        return processedEnergyCards;
    }

    private static EnergyCard processEnergyCard(JSONObject energyCards) throws JSONException{
        String ID = (String)energyCards.get("ID");

        String name = (String)energyCards.get("Name");

        String str_energyType = (String)energyCards.get("EnergyType");
        EnergyType energyType = EnergyType.valueOf(str_energyType);

        JSONArray energiesJSONArr = (JSONArray)energyCards.get("Energies");
        Map<UnimonEvolveType,Map<EnergyType, Integer>> energies = processEnergyValues(energiesJSONArr);

        EnergyCard energyCardProcessed = new EnergyCard(0,0,0,0,null,null,ID,null,name,energyType,energies,"",false, Container.LOADED);

        return energyCardProcessed;
    }

    private static Map<UnimonEvolveType,Map<EnergyType, Integer>> processEnergyValues(JSONArray energiesJSONArr) throws JSONException{

        Map<UnimonEvolveType,Map<EnergyType, Integer>> energies = new HashMap<>();
        Map<EnergyType,Integer> value = new HashMap<>();


        for (int i = 0; i < energiesJSONArr.length(); i++){

            JSONObject energyCards = (JSONObject)energiesJSONArr.get(i);
            String str_evolveType = (String) energyCards.get("EvolveType");
            UnimonEvolveType evolveType = UnimonEvolveType.valueOf(str_evolveType);

            String str_statTypeMana = (String) energyCards.get("Mana");
            int manaVal = Integer.parseInt(str_statTypeMana);

            String str_statTypeStamina = (String) energyCards.get("Stamina");
            int staminaVal = Integer.parseInt(str_statTypeStamina);

            String str_statTypeHealth = (String) energyCards.get("Health");
            int healthVal = Integer.parseInt(str_statTypeHealth);



            if(manaVal != -1){
                value.put(EnergyType.MANA,manaVal);
            }
            if(staminaVal != -1){
                value.put(EnergyType.STAMINA,staminaVal);
            }
            if(healthVal != -1){
                value.put(EnergyType.HEALTH,healthVal);
            }

            energies.put(evolveType,value);

        }

        return energies;
    }


}