package org.powertac.samplebroker;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class PyComs {
    // Context Manager
    public static HashMap<Integer, JSONObject> energyReportMap = new HashMap<>();
    public static HashMap<Integer, JSONObject> competitionJson = new HashMap<>();

    // Market Manager Service
    public static HashMap<Integer, JSONObject> clearedTradeJson = new HashMap<>();
    public static HashMap<Integer, JSONObject> orderbookJson = new HashMap<>();
    public static HashMap<Integer, JSONObject> weatherForecastJson = new HashMap<>();
    public static HashMap<Integer, JSONObject> weatherJson = new HashMap<>();

    public void trigger(JSONObject obj, JSONType type){
        var currSlot = obj.getString("timeslotIndex");
        System.out.println("\n\nGot to trigger\n\n");
        switch(type){
            case energyReportType:
                energyReportMap.put(Integer.parseInt(currSlot), obj);
                break;
            case competitionJsonType:
                competitionJson.put(Integer.parseInt(currSlot), obj);
                break;
            case clearedTradeJsonType:
                clearedTradeJson.put(Integer.parseInt(currSlot), obj);
                break;
            case orderbookJsonType:
                orderbookJson.put(Integer.parseInt(currSlot), obj);
                break;
            case weatherForecastJsonType:
                weatherForecastJson.put(Integer.parseInt(currSlot), obj);
                break;
            case weatherJsonType:
                weatherJson.put(Integer.parseInt(currSlot), obj);
                break;
        }

        // Check if we have to send the request now.
    }
}
