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

import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyCard;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Energy.EnergyType;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Generic.Container;
import uk.ac.qub.eeecs.LonelyAbyss.GamePieces.Cards.Types.Unimon.UnimonEvolveType;

/**
 * Created by Patrick on 18/03/2017.
 */

public class JSONEnergyReader {

    private static JSONObject unprocessedEnergyCards = null;

    public static ArrayList<EnergyCard> loadJSONEnergy(Context context) throws JSONException, IOException{
        if(unprocessedEnergyCards == null){
            unprocessedEnergyCards = loadJSONEnergyCards(context);
        }

        JSONArray energyJsonCards = (JSONArray) unprocessedEnergyCards.get("Energy");

        ArrayList<EnergyCard> processedEnergyCards = new ArrayList<>();

        for(int i = 0; i < energyJsonCards.length(); i++){
            JSONObject energyCardsToBeProc = (JSONObject)energyJsonCards.get(i);
            EnergyCard procEnergyCards = processEnergyCard(energyCardsToBeProc);
            processedEnergyCards.add(procEnergyCards);
        }

        return processedEnergyCards;
    }

    private static JSONObject loadJSONEnergyCards(Context context)throws JSONException,IOException{
        InputStream inputStream = context.getAssets().open("ClassObjectJSON/EnergyCards.json");
        String json_str = IOUtils.toString(inputStream);
        IOUtils.closeQuietly(inputStream);

        JSONObject json = new JSONObject(json_str);

        JSONObject jsonCards = (JSONObject)json.get("Cards");

        return jsonCards;
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
