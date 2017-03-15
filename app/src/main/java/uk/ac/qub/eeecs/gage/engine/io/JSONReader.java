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




}