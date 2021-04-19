package org.powertac.samplebroker.util;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;

import java.util.ArrayList;    

@Service("pyComs")
public class PyComs {
    // Context Manager
    public static HashMap<Integer, JSONObject> energyReportMap = new HashMap<>();
    public static HashMap<Integer, JSONObject> competitionJson = new HashMap<>();

    // Market Manager Service
    public static HashMap<Integer, ArrayList<JSONObject>> clearedTradeJson = new HashMap<>();
    public static HashMap<Integer, ArrayList<JSONObject>> orderbookJson = new HashMap<>();
    public static HashMap<Integer, JSONObject> weatherForecastJson = new HashMap<>();
    public static HashMap<Integer, JSONObject> weatherJson = new HashMap<>();

    public static Map<String, String> jsonType = Map.ofEntries(
        entry("energyReportType", "energyReportType"),
        entry("competitionJsonType", "competitionJsonType"),
        entry("clearedTradeJsonType", "clearedTradeJsonType"),
        entry("orderbookJsonType", "orderbookJsonType"),
        entry("weatherForecastJsonType", "weatherForecastJsonType"),
        entry("weatherJsonType", "weatherJsonType")
    ); 

    // Agora nós queremos agrupar os dados em base.
    // Sempre que passar à base seguinte, considera-se que todas as mensagens não recebidas
    // não chegarão.

    public void trigger(JSONObject obj, String type){
        String currSlot = obj.get("timeslotIndex").toString(); 
    
        switch(type){
            case "energyReportType":
                energyReportMap.put(Integer.parseInt(currSlot), obj);
                break;
            case "competitionJsonType":
                competitionJson.put(Integer.parseInt(currSlot), obj);
                break;
            case "clearedTradeJsonType":
                // Can't be done this way.
                // clearedTradeJson.put(Integer.parseInt(currSlot), obj);
                
                break;
            case "orderbookJsonType":
                // Can't be done this way.
                // orderbookJson.put(Integer.parseInt(currSlot), obj);
                break;
            case "weatherForecastJsonType":
                weatherForecastJson.put(Integer.parseInt(currSlot), obj);
                break;
            case "weatherJsonType":
                weatherJson.put(Integer.parseInt(currSlot), obj);
                break;
        }

        // Check if we have to send the request now.
    }
}
