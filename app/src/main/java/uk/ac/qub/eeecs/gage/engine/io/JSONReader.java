package uk.ac.qub.eeecs.gage.engine.io;


import android.content.Context;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import uk.ac.qub.eeecs.gage.Game;

/**
 * Created by appcamp on 14/03/2017.
 */

public class JSONReader {
    private static JSONObject unprocessedJSON = null; //the entire JSON resource file as an JSON Object

    //James Bailey 40156063
    //Load JSON resource file from assets and process as an overall JSON object
    private static JSONObject loadJSON(Context context) throws IOException, JSONException {
        InputStream is = context.getAssets().open("ClassObjectJSON/Cards.json");
        String json_str = IOUtils.toString(is);
        IOUtils.closeQuietly(is);

        JSONObject json = new JSONObject(json_str);

        return json;
    }


    //James Bailey 40156063
    //Load and process JSON Unimon Cards that are described in the JSON.
    public static ArrayList<UnimonCard> loadJSONUnimon(Game mGame) throws JSONException, IOException {
        Context context = mGame.getActivity(); //required to access assets of the android main activity

        if (unprocessedJSON == null) { //validates whether the JSON has been loaded from assets and processed as a JSON object
            unprocessedJSON = loadJSON(context);
        }

        JSONObject cards = (JSONObject) unprocessedJSON.get("Cards"); //all the cards in the JSON object, both energy and unimon
        JSONArray unimonJsonCards = (JSONArray) cards.get("Unimon"); //the list of unimon cards in the JSON object

        ArrayList<UnimonCard> processedUnimonCards = new ArrayList<>(); //stores the JSON unimon cards objects that have been processed to UnimonCard object.

        for (int i = 0; i < unimonJsonCards.length(); i++) { //process every unimon card in the json list
            JSONObject unimonCardToBeProc = (JSONObject) unimonJsonCards.get(i); //the unprocessed unimon card
            UnimonCard procUnimonCard = processUnimonCard(unimonCardToBeProc); //the processed unimon card
            processedUnimonCards.add(procUnimonCard); //add processed unimon card to the unimoncard list
        }

        return processedUnimonCards; //returns the processed unimon card objects.
    }

    //Patrick Conway 40150555
    public static ArrayList<EnergyCard> loadJSONEnergy(Game mGame) throws JSONException, IOException{
        Context context = mGame.getActivity();

        if(unprocessedJSON == null) {
            unprocessedJSON = loadJSON(context);
        }

        JSONObject cards = (JSONObject) unprocessedJSON.get("Cards");
        JSONArray energyJsonCards = (JSONArray) cards.get("Energy");

        ArrayList<EnergyCard> processedEnergyCards = new ArrayList<>();

        for(int i = 0; i < energyJsonCards.length(); i++){
            JSONObject energyCardsToBeProc = (JSONObject)energyJsonCards.get(i);
            EnergyCard procEnergyCards = processEnergyCard(energyCardsToBeProc);
            processedEnergyCards.add(procEnergyCards);
        }

        return processedEnergyCards;
    }

    //James Bailey 40156063
    //Load and process JSON Unimon Cards the player possesses that are described in the JSON.
    public static ArrayList<UnimonCard> loadPlayerUnimonCards(Game mGame) throws JSONException, IOException {
        Context context = mGame.getActivity();


        if(unprocessedJSON == null){
            unprocessedJSON = loadJSON(context);
        } else if (mGame.getAssetManager().getUnimonCards() == null) { //validates whether the game's Unimon Cards have been loaded
            mGame.getAssetManager().setUnimonCards(loadJSONUnimon(mGame)); //if not, call the method to load them
        }

        JSONObject playerCards = (JSONObject) unprocessedJSON.get("PlayerCards");
        JSONArray playerUnimonCardsJSON = (JSONArray) playerCards.get("Unimon");

        Map<String, Integer> playerCardsandNum = new LinkedHashMap<>(); //used to store the card ID the player owns and the number they own

        for (int i = 0; i < playerUnimonCardsJSON.length(); i++) { //iterate through the entire JSON array - get the IDs and the quantity owned
            JSONObject unimonToProcess = (JSONObject) playerUnimonCardsJSON.get(i);
            String ID = (String) unimonToProcess.get("ID");
            String str_number = (String) unimonToProcess.get("Num");
            int number = Integer.parseInt(str_number);
            playerCardsandNum.put(ID, number);
        }

        ArrayList<UnimonCard> loadedUnimonCards = mGame.getAssetManager().getUnimonCards(); //the game's UnimonCards currently stored in the AssetManager - sorted with ID ascending
        Iterator<UnimonCard> loadedUnimonIterator = loadedUnimonCards.iterator(); //iterator that begins at the first element of the list of the game's UnimonCards.

        ArrayList<UnimonCard> playerUnimonCards = new ArrayList<>(); //used to store the UnimonCard objects the player owns

        for (Map.Entry<String, Integer> playerCard : playerCardsandNum.entrySet()) { //go through sequentially the cards the player owns (stored as ID and quantity). Go through ID ascending.
            String ID = playerCard.getKey(); //ID of the card the player owns
            while (loadedUnimonIterator.hasNext()) {
                UnimonCard uCard = loadedUnimonIterator.next(); //one of the game's unimoncards - sorted by ID ascending
                String uCardID = uCard.getID(); //the ID of the game's unimon card
                if (ID.equals(uCardID)) { //if the id of the player's card matches the id of the game's unimon card
                    int numberCard = playerCard.getValue(); //the quantity of the card the player owns
                    for (int i = 0; i < numberCard; i++) {
                        playerUnimonCards.add(uCard.copy()); //add the card to the player's list of cards owned
                    }
                    break; //break the while loop - get the next card the player owns
                }
            }
        }

        return playerUnimonCards;
    }

    //James Bailey 40156063
    //Load and process JSON Unimon Cards the player possesses that are described in the JSON.
    public static ArrayList<EnergyCard> loadPlayerEnergyCards(Game mGame) throws JSONException, IOException {
        Context context = mGame.getActivity();

        if(unprocessedJSON == null){
            unprocessedJSON = loadJSON(context);
        } else if (mGame.getAssetManager().getEnergyCards() == null) {
            mGame.getAssetManager().setEnergyCards(loadJSONEnergy(mGame));
        }

        JSONObject playerCards = (JSONObject) unprocessedJSON.get("PlayerCards");
        JSONArray playerEnergyCardsJSON = (JSONArray) playerCards.get("Energy");

        Map<String, Integer> playerCardsandNum = new LinkedHashMap<>();
        ArrayList<EnergyCard> playerEnergyCards = new ArrayList<>();

        for (int i = 0; i < playerEnergyCardsJSON.length(); i++) {
            JSONObject energyToProcess = (JSONObject) playerEnergyCardsJSON.get(i);
            String ID = (String) energyToProcess.get("ID");
            String str_number = (String) energyToProcess.get("Num");
            int number = Integer.parseInt(str_number);
            playerCardsandNum.put(ID, number);
        }

        ArrayList<EnergyCard> loadedEnergyCards = mGame.getAssetManager().getEnergyCards();
        Iterator<EnergyCard> loadedEnergyIterator = loadedEnergyCards.iterator();

        for (Map.Entry<String, Integer> playerCard : playerCardsandNum.entrySet()) {
            String ID = playerCard.getKey();
            while (loadedEnergyIterator.hasNext()) {
                EnergyCard eCard = loadedEnergyIterator.next();
                String eCardID = eCard.getID();
                if (ID.equals(eCardID)) {
                    int numberCard = playerCard.getValue();
                    for (int i = 0; i < numberCard; i++) {
                        playerEnergyCards.add(eCard.copy());
                    }
                    break;
                }
            }
        }
        return playerEnergyCards;
    }

    //James Bailey 40156063/Patrick Conway 40150555
    //Process a unimon card represented in the JSON from the generic JSON Object to UnimonCard object
    private static UnimonCard processUnimonCard(JSONObject unimonCard) throws JSONException {
        //general identifiers
        String ID = (String) unimonCard.get("ID");

        String name = (String) unimonCard.get("Name");

        String str_evolveType = (String) unimonCard.get("EvolveType");
        UnimonEvolveType evolveType = UnimonEvolveType.valueOf(str_evolveType);

        String str_elementType = (String) unimonCard.get("Element");
        Element elementType = Element.valueOf(str_elementType);

        //3 key stat attributes and their type and value
        String str_health = (String) unimonCard.get("Health");
        int health = Integer.parseInt(str_health);

        String str_mana = (String) unimonCard.get("Mana");
        int mana = Integer.parseInt(str_mana);

        String str_stamina = (String) unimonCard.get("Stamina");
        int stamina = Integer.parseInt(str_stamina);

        //the card's three moves
        JSONArray unimonMoveJSONArr = (JSONArray) unimonCard.get("Moves");
        UnimonMoves[] unimonMoves = processUnimonMoves(unimonMoveJSONArr);

        //The armour and weakness, and absorption stats of the card
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

        //the processed UnimonCard using the values parsed from the JSON
        UnimonCard unimonCardProcessed = new UnimonCard(0, 0, 0, 0, null, null, ID, null, null, null, name, evolveType, elementType, unimonMoves, health, mana, stamina, "", armourVal, weaknessVal, weaknessElement, absorbVal, absorbElement, false, Container.LOADED);
        return unimonCardProcessed;
    }

    //James Bailey 40156063/Patrick Conway 40150555
    //Process a unimon card's moves
    private static UnimonMoves[] processUnimonMoves(JSONArray unimonMoves) throws JSONException {
        final int numberOfMoves = 3; //the number of moves a UnimonCard can have
        UnimonMoves[] processedUnimonMoves = new UnimonMoves[numberOfMoves]; //used to store the list of unimonCard moves.

        for (int i = 0; i < numberOfMoves; i++) {
            JSONObject unimonMovesToBeProc = (JSONObject) unimonMoves.get(i); //the JSON Object of the unprocessed single UnimonMove
            //general identifiers
            String ID = (String) unimonMovesToBeProc.get("ID");
            String name = (String) unimonMovesToBeProc.get("Name");

            String str_MoveType = (String) unimonMovesToBeProc.get("MoveType");
            MoveType moveType = MoveType.valueOf(str_MoveType);

            //the stat requirements of the move
            Map<MoveResource, Integer> moveReq = processMoveReq(unimonMovesToBeProc);

            //the base damage of the move
            String str_basedDam = (String) unimonMovesToBeProc.get("BaseDam");
            int baseDam = Integer.parseInt(str_basedDam);

            UnimonMoves move = new UnimonMoves(ID, name, moveReq, baseDam, moveType); //the single processed move
            processedUnimonMoves[i] = move; //add the single move to the list of moves the card has
        }
        return processedUnimonMoves;
    }

    //James Bailey 40156063
    //Process the move requirements of a unimon card's move
    private static Map<MoveResource, Integer> processMoveReq(JSONObject unimonMovesToBeProc) throws JSONException {
        Map<MoveResource, Integer> moveReq = new HashMap<>(); //used to store the requirements - the stat type and value needed

        //stamina requirement
        String str_manaReq = (String) unimonMovesToBeProc.get("ManaReq");
        int manaReq = Integer.parseInt(str_manaReq);

        //mana requirement
        String str_staminaReq = (String) unimonMovesToBeProc.get("StaminaReq");
        int staminaReq = Integer.parseInt(str_staminaReq);


        //if the requirement for a particular stat type does not exist for the move then it is represent by value -1
        if (manaReq != -1) {
            moveReq.put(MoveResource.MANA, manaReq);
        }
        if (staminaReq != -1) {
            moveReq.put(MoveResource.STAMINA, staminaReq);
        }

        return moveReq;
    }

    //Patrick Conway 40150555
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

    //Patrick Conway 40150555
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