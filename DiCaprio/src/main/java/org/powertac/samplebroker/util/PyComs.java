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
    public static int lastIndex = 0;
    public static int mapLastIndex = 0;

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

    public JSONObject createMockClearedTrade(int slotInDay){
        var clearedTrade = new HashMap<String, Object>();
        clearedTrade.put("timeslotIndex", "none");
        clearedTrade.put("executionMWh", "none");
        clearedTrade.put("executionPrice", "none");
        clearedTrade.put("dateExecuted", "none");
        clearedTrade.put("slotInDay", slotInDay);
        return new JSONObject(clearedTrade);
    }

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
                System.out.println(lastIndex);
                int slotInDay = Integer.parseInt(obj.get("slotInDay").toString()); 

                if(lastIndex - 1 == slotInDay) {
                    System.out.println("Message was late");
                } else if(lastIndex - 2 == slotInDay) {
                    System.out.println("Message was late");
                } else {
                    //Fills missing values
                    ArrayList<JSONObject> clearedTrades;
                    //slotInDay missing is 0

                    if(slotInDay == 1 && lastIndex == 23) {
                        clearedTrades =  clearedTradeJson.get(mapLastIndex); 
                        clearedTrades.add(createMockClearedTrade(0)); //0 is the last index
                        lastIndex = 0;
                    }
                    if(slotInDay == 0 && lastIndex == 22) {
                        clearedTrades =  clearedTradeJson.get(mapLastIndex); 
                        clearedTrades.add(createMockClearedTrade(23));
                        lastIndex = 0;
                    } else if(slotInDay == 1 || (slotInDay == 2 && lastIndex == 0)  || (slotInDay == 3 && lastIndex == 0) || (slotInDay == 4 && lastIndex == 0)) {
                        clearedTrades =  clearedTradeJson.get(mapLastIndex);  
                        clearedTrades = new ArrayList<>();
                        mapLastIndex++;
                        for(int i=lastIndex+1; i < slotInDay; i++) {
                            clearedTrades.add(createMockClearedTrade(i));
                        } 
                    } else {
                        clearedTrades =  clearedTradeJson.get(mapLastIndex); 
                        for(int i=lastIndex+1; i < slotInDay; i++) {
                            clearedTrades.add(createMockClearedTrade(i));
                        } 
                    }  
                    clearedTrades.add(obj);
                    clearedTradeJson.put(mapLastIndex, clearedTrades);  
                    lastIndex = slotInDay;
                }
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
