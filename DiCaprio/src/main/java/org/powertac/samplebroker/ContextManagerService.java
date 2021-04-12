/*
 * Copyright (c) 2012 by the original author
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.powertac.samplebroker;

import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.powertac.common.BankTransaction;
import org.powertac.common.CashPosition;
import org.powertac.common.Competition;
import org.powertac.common.msg.DistributionReport;
import org.powertac.common.msg.MarketBootstrapData;
import org.powertac.samplebroker.core.BrokerPropertiesService;
import org.powertac.samplebroker.interfaces.BrokerContext;
import org.powertac.samplebroker.interfaces.Initializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.json.JSONObject;
import org.powertac.samplebroker.util.PyComs;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Handles incoming context and bank messages with example behaviors.
 * 
 * @author John Collins
 */
@Service
public class ContextManagerService implements Initializable {
  static private Logger log = LogManager.getLogger(ContextManagerService.class);

  private PyComs pyComs;

  @Autowired
  private BrokerPropertiesService propertiesService;

  BrokerContext master;

  // current cash balance
  private double cash = 0;

  @Autowired
  public void initPyComs(PyComs pyComs){
    this.pyComs = pyComs;
  }

  // @SuppressWarnings("unchecked")
  @Override
  public void initialize(BrokerContext broker) {
    master = broker;
    propertiesService.configureMe(this);
    // --- no longer needed ---
    // for (Class<?> clazz: Arrays.asList(BankTransaction.class,
    // CashPosition.class,
    // DistributionReport.class,
    // Competition.class,
    // java.util.Properties.class)) {
    // broker.registerMessageHandler(this, clazz);
    // }
  }

  // -------------------- message handlers ---------------------
  //
  // Note that these arrive in JMS threads; If they share data with the
  // agent processing thread, they need to be synchronized.
  
  /**
   * BankTransaction represents an interest payment. Value is positive for 
   * credit, negative for debit. 
   */
  public void handleMessage (BankTransaction btx)
  {
    // TODO - handle this
  }

  /**
   * CashPosition updates our current bank balance.
   */
  public void handleMessage (CashPosition cp)
  {
    cash = cp.getBalance();
    log.info("Cash position: " + cash);
  }
  
  /**
   * DistributionReport gives total consumption and production for the timeslot,
   * summed across all brokers.
   */
  public void handleMessage(DistributionReport dr) {
    System.out.println("DistributionReport");
    HashMap<String,Object> hM = new HashMap<String, Object>();
    hM.put("Total Consumption", dr.getTotalConsumption());
    hM.put("Total Production", dr.getTotalProduction());

    var energyReport = new JSONObject(hM);
    
    System.out.println("DistributionReport1");
    

    // int timeSlotIndex = comp.getTimeslotIndex();
    // pyComs.trigger(energyReport, PyComs.jsonType.get("energyReportType")); 
  }

  /**
   * Handles the Competition instance that arrives at beginning of game. Here we
   * capture all the customer records so we can keep track of their subscriptions
   * and usage profiles.
   */
  public void handleMessage(Competition comp) {

    try {
      System.out.println("Competition");
      AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
      context.scan("org.powertac.samplebroker.util");
      context.refresh();
      
      
      int noCompetitors = comp.getBrokers().size();
      int noCustomer = comp.getCustomers().size();

      MarketBootstrapData marketBootstrapData = comp.getMarketBootstrapData();

      var hM = new HashMap<String, Object>();
      hM.put("marketBootstrapData", marketBootstrapData);
      hM.put("noCompetitors",noCompetitors);
      hM.put("noCustomer ", noCustomer);
      hM.put("timeslotIndex ", "-10");
      
      var competitionJson =  new JSONObject(hM);
      System.out.println("Competition1");

      PyComs pyComs = context.getBean(PyComs.class);

      pyComs.trigger(competitionJson, PyComs.jsonType.get("competitionJsonType")); 

      context.close();
    }
    catch(Exception E){
      System.err.println(E);
    }
    
  }

  /**
   * Receives the server configuration properties.
   */
  public void handleMessage (java.util.Properties serverProps)
  {
    // TODO - adapt to the server setup.
  }
}
