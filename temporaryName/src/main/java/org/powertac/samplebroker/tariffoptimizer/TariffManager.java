package org.powertac.samplebroker.tariffoptimizer;
import org.powertac.common.Broker;
import org.powertac.common.Rate;
import org.powertac.common.Tariff;
import org.powertac.common.TariffSpecification;
import org.powertac.common.enumerations.PowerType;
import org.powertac.common.msg.TariffRevoke;
import org.powertac.common.repo.TariffRepo;
import org.powertac.samplebroker.interfaces.BrokerContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TariffManager {
	boolean DEBUG = true;
	double INITIAL_DECREASE = 0.2;
	double PERIODIC_DECREASE = 0.05;
	
	public TariffManager(TariffRepo tariffRepo, BrokerContext brokerContext) {
		super();
		this.tariffRepo = tariffRepo;
		this.brokerContext = brokerContext;
	}

	private Map<PowerType, List<TariffSpecification>> competingTariffs;
	private TariffRepo tariffRepo;
	private BrokerContext brokerContext;
	
	public void createInitialTariffs(Map<PowerType, List<TariffSpecification>> competingTariffs) {
		// TODO Auto-generated method stub	
		Map<PowerType, TariffSpecification> initialTariffs = new HashMap<PowerType, TariffSpecification>();
		this.competingTariffs = competingTariffs;
		for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet()) {
			if(this.DEBUG) System.out.println("Creating new Tariff: ");
			TariffSpecification newTariff= initialTariffMutator(entry.getValue());
			if(newTariff != null)
				initialTariffs.put(entry.getKey(), newTariff);
		}
		for (Map.Entry<PowerType, TariffSpecification> initialTariff : initialTariffs.entrySet()) {
			addNewTariff(initialTariff.getValue());
			if(this.DEBUG) System.out.println(initialTariff.toString());
		}
	}

	public void improveTariffs(int timeslotIndex, Map<PowerType, List<TariffSpecification>> competingTariffs) {
		
		if((timeslotIndex - 1) % 6 == 0) {
			
			if(this.DEBUG) System.out.println("");
			if(this.DEBUG) System.out.println("");
			if(this.DEBUG) System.out.println("Revision Period - Time: " + timeslotIndex);
			if(this.DEBUG) System.out.println("________________________________________________");
			if(this.DEBUG) System.out.println("Competing tariffs:");
			if(this.DEBUG) System.out.println("");
			
			for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet())
				printTariffSpecificationList(entry.getKey(), entry.getValue());
	
			if(this.DEBUG) System.out.println("");
			if(this.DEBUG) System.out.println("TNA Tariff update:");
			if(this.DEBUG) System.out.println("");
			
			var myTariffs = List.copyOf(tariffRepo.findTariffsByBroker(this.brokerContext.getBroker()));
			if(this.DEBUG) System.out.println("MyTariffs: " + myTariffs.toString());
			for (Tariff tariff : myTariffs) {
				if(this.DEBUG) System.out.println("tariff is active?" + tariff.isActive());
				if(tariff.getIsSupersededBy() == null) {

					TariffSpecification newTariffSpec = lowerTariff(tariff.getTariffSpec(), this.PERIODIC_DECREASE); //SUBSTITUIR LOWER TARIFF COM MODELO AI
					if(this.DEBUG) System.out.println(tariff.toString() + "  ->  " + newTariffSpec.toString());
					if(this.DEBUG) System.out.println("");
					
					supersedeTariff(newTariffSpec, tariff.getTariffSpec());
				}
			}
		}
		//iterar por tariffRepo
		//chamar o modelo
		//alterar tarifas
		//enviar tarifas alteradas
		if(this.DEBUG) System.out.flush();
	}
	

	private void printTariffSpecificationList(PowerType powerType, List<TariffSpecification> tariffs) {
		if(this.DEBUG) System.out.println(powerType.toString());
		for (TariffSpecification tariff : tariffs) {
			if(this.DEBUG) System.out.println(tariff.toString());
		}
	}
	
	private TariffSpecification initialTariffMutator(List<TariffSpecification> list) {
		//TODO: 
		// fce = sumt(Ce,t * -pdef) /  (sumt(Ce,t * -pv,i,t - pp,i) - psignup,i - Ff * pwithdraw,i - pwithdraw,0)
		// selecionar o melhor da lista e não 0S (Usar tariffEvaluatorHelper)
		if(list.get(0) != null) {
			if(this.DEBUG) System.out.println("Mutating tariff: " + list.get(0).toString());
			TariffSpecification competingSpec = list.get(0);
			TariffSpecification loweredSpec = lowerTariff(competingSpec, INITIAL_DECREASE, this.brokerContext.getBroker(),
                    competingSpec.getPowerType());
			loweredSpec.withEarlyWithdrawPayment(0);
			if(this.DEBUG) System.out.println("Signup Payment:" + loweredSpec.getSignupPayment());
			loweredSpec.withSignupPayment(competingSpec.getSignupPayment());
			if(this.DEBUG) System.out.println("Min Duraition:" + loweredSpec.getMinDuration());
			loweredSpec.withMinDuration(competingSpec.getMinDuration());
			if(this.DEBUG) System.out.println("Periodic Payment:" + loweredSpec.getPeriodicPayment());
			loweredSpec.withPeriodicPayment(competingSpec.getPeriodicPayment());
			return loweredSpec;
		}
		return null;
	}
	
	private TariffSpecification raiseTariff(TariffSpecification tariff) {
		//TODO: Heuristic to raise tariff
		return null;
	}

	
	private TariffSpecification lowerTariff(TariffSpecification tariff, double decrease) {
		//TODO: Heuristic to lower tariff
		TariffSpecification lowerTariff = new TariffSpecification(this.brokerContext.getBroker(),
                tariff.getPowerType());
		lowerRates(tariff, decrease, lowerTariff);
		return lowerTariff;
	}
	
	private TariffSpecification lowerTariff(TariffSpecification tariff, double decrease, Broker broker, PowerType powerType ) {
		TariffSpecification lowerTariff = new TariffSpecification(broker,
				powerType);
		lowerRates(tariff, decrease, lowerTariff);
		return lowerTariff;
	}

	private void lowerRates(TariffSpecification tariff, double decrease, TariffSpecification lowerTariff) {
		//TODO: Heuristic to lower rates. From variable tariff create more favourable fixed rate tariff
		List<Rate> rates = tariff.getRates();
		for (Rate rate : rates) {
			Rate lowerRate = new Rate();
			if(rate.isFixed()) {
				//lower fixed rates (get params from rate and add to lowerRate) 
				lowerRate.withValue(rate.getValue() * 1 - decrease);
				if(this.DEBUG) System.out.println("Fixed Rate from: " + rate.getValue() + " to: " + lowerRate.getValue());
				copyPeriods(rate, lowerRate);
				
			}
			else {
				lowerRate.withValue(rate.getExpectedMean());
				if(this.DEBUG) System.out.println("Variable Rate from: " + rate.getExpectedMean() + " to: " + lowerRate.getValue());
				copyPeriods(rate, lowerRate);
			}
			lowerTariff.addRate(rate);
		}
	}

	private void copyPeriods(Rate rate, Rate lowerRate) {
		if(this.DEBUG) System.out.println("Daily Begin:" + rate.getDailyBegin());
		lowerRate.withDailyBegin(rate.getDailyBegin());
		if(this.DEBUG) System.out.println("Daily End:" + rate.getDailyBegin());
		lowerRate.withDailyEnd(rate.getDailyEnd());
		if(this.DEBUG) System.out.println("hWeekly Begin:" + rate.getWeeklyBegin());
		lowerRate.withWeeklyBegin(rate.getWeeklyBegin());
		if(this.DEBUG) System.out.println("Weekly End:" + rate.getWeeklyEnd());
		lowerRate.withWeeklyEnd(rate.getWeeklyEnd());
	}
	
	
	private void addNewTariff(TariffSpecification spec) {
		if(this.DEBUG) System.out.print("ADDING TARIFF: " + spec.toString() + "  " + 
	this.tariffRepo.findTariffsByBroker(this.brokerContext.getBroker()).size() + "->");
		Tariff newTariff = new Tariff(spec);
		newTariff.init();
	    this.tariffRepo.addTariff(new Tariff(spec));
		if(this.DEBUG) System.out.println(this.tariffRepo.findTariffsByBroker(this.brokerContext.getBroker()).size());
	    this.brokerContext.sendMessage(spec);
	}
	
	private void supersedeTariff(TariffSpecification spec, TariffSpecification oldSpec) {
		spec.addSupersedes(oldSpec.getId());
		addNewTariff(spec);
		// revoke the old one
        TariffRevoke revoke =
          new TariffRevoke(brokerContext.getBroker(), oldSpec);
        brokerContext.sendMessage(revoke);
	}
	
}
