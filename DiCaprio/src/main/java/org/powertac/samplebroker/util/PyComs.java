package org.powertac.samplebroker.util;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;    

@Service("pyComs")
public class PyComs {
    // Context Manager
    public static HashMap<Integer, JSONObject> energyReportMap = new HashMap<>();
    public static HashMap<Integer, JSONObject> competitionJson = new HashMap<>();

    // Market Manager Service
    public static HashMap<Integer, JSONObject> clearedTradeJson = new HashMap<>();
    public static HashMap<Integer, JSONObject> orderbookJson = new HashMap<>();
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

    public void trigger(JSONObject obj, String type){
        System.out.println("\n\nGot to trigger\n\n");
        // var currSlot = obj.getString("timeslotIndex");
        var currSlot = "0";
        System.out.println(obj.toString());
        switch(type){
            case "energyReportType":
                energyReportMap.put(Integer.parseInt(currSlot), obj);
                break;
            case "competitionJsonType":
                competitionJson.put(Integer.parseInt(currSlot), obj);
                break;
            case "clearedTradeJsonType":
                clearedTradeJson.put(Integer.parseInt(currSlot), obj);
                break;
            case "orderbookJsonType":
                orderbookJson.put(Integer.parseInt(currSlot), obj);
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
