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
    public static int lastIndexCleared, mapLastIndexCleared, lastIndexOrderBook, mapLastIndexOrderBook = 0;

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

    public JSONObject createMockOrderbook(int slotInDay){
        var orderbook = new HashMap<String, Object>();
        orderbook.put("timeslotIndex", "none");
        orderbook.put("clearingPrice", "none");
        orderbook.put("asks", "none");
        orderbook.put("bids", "none");
        orderbook.put("slotInDay", slotInDay);
        return new JSONObject(orderbook);
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
                int slotInDay = Integer.parseInt(obj.get("slotInDay").toString()); 

                if(lastIndexCleared - 1 == slotInDay) {
                    System.out.println("Message was late");
                } else if(lastIndexCleared - 2 == slotInDay) {
                    System.out.println("Message was late");
                } else {
                    //Fills missing values
                    ArrayList<JSONObject> clearedTrades;
                    //slotInDay missing is 0
                    if(slotInDay == 1 && lastIndexCleared == 23) {
                        clearedTrades =  clearedTradeJson.get(mapLastIndexCleared); 
                        clearedTrades.add(createMockClearedTrade(0)); //0 is the last index
                        lastIndexCleared = 0;
                    }
                    if(slotInDay == 0 && lastIndexCleared == 22) {
                        clearedTrades =  clearedTradeJson.get(mapLastIndexCleared); 
                        clearedTrades.add(createMockClearedTrade(23));
                        lastIndexCleared = 0;
                    } else if(slotInDay == 1 || (slotInDay == 2 && lastIndexCleared == 0)  || (slotInDay == 3 && lastIndexCleared == 0) || (slotInDay == 4 && lastIndexCleared == 0)) {
                        clearedTrades =  clearedTradeJson.get(mapLastIndexCleared);  
                        clearedTrades = new ArrayList<>();
                        mapLastIndexCleared++;
                        for(int i=lastIndexCleared+1; i < slotInDay; i++) {
                            clearedTrades.add(createMockClearedTrade(i));
                        } 
                    } else {
                        clearedTrades =  clearedTradeJson.get(mapLastIndexCleared); 
                        for(int i=lastIndexCleared +1; i < slotInDay; i++) {
                            clearedTrades.add(createMockClearedTrade(i));
                        } 
                    }  
                    clearedTrades.add(obj);
                    clearedTradeJson.put(mapLastIndexCleared, clearedTrades);  
                    lastIndexCleared = slotInDay;
                }
                break;
            case "orderbookJsonType":
                int slotInDayOrder = Integer.parseInt(obj.get("slotInDay").toString()); 

                if(lastIndexOrderBook - 1 == slotInDayOrder) {
                    System.out.println("Message was late");
                } else if(lastIndexOrderBook - 2 == slotInDayOrder) {
                    System.out.println("Message was late");
                } else {
                    //Fills missing values
                    ArrayList<JSONObject> orderBooks;
                    //slotInDay missing is 0
                    if(slotInDayOrder == 1 && lastIndexOrderBook == 23) {
                        orderBooks =  orderbookJson.get(mapLastIndexOrderBook); 
                        orderBooks.add(createMockOrderbook(0)); //0 is the last index
                        lastIndexOrderBook = 0;
                    }
                    if(slotInDayOrder == 0 && lastIndexOrderBook == 22) {
                        orderBooks =  orderbookJson.get(mapLastIndexOrderBook); 
                        orderBooks.add(createMockOrderbook(23));
                        lastIndexOrderBook = 0;
                    } else if(slotInDayOrder == 1 || (slotInDayOrder == 2 && lastIndexOrderBook == 0)  || (slotInDayOrder == 3 && lastIndexOrderBook == 0) || (slotInDayOrder == 4 && lastIndexOrderBook == 0)) {           
                        orderBooks =  orderbookJson.get(mapLastIndexOrderBook);  
                        orderBooks = new ArrayList<>();
                        mapLastIndexOrderBook++;
                        for(int i=lastIndexOrderBook+1; i < slotInDayOrder; i++) {
                            orderBooks.add(createMockOrderbook(i));
                        } 
                    } else {
                        orderBooks =  orderbookJson.get(mapLastIndexOrderBook); 
                        for(int i=lastIndexOrderBook +1; i < slotInDayOrder; i++) {
                            orderBooks.add(createMockOrderbook(i));
                        } 
                    }  
                    orderBooks.add(obj);
                    orderbookJson.put(mapLastIndexOrderBook, orderBooks);  
                    lastIndexOrderBook = slotInDayOrder;
                }
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
