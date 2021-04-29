package org.powertac.samplebroker.tariffoptimizer;
import org.powertac.common.Rate;
import org.powertac.common.Tariff;
import org.powertac.common.TariffSpecification;
import org.powertac.common.enumerations.PowerType;
import org.powertac.common.repo.TariffRepo;
import org.powertac.samplebroker.interfaces.BrokerContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TariffManager {

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
		System.out.println("Competing Tariffs: ");
		for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet()) {
			//printTariffList(entry.getKey(), entry.getValue());
			TariffSpecification newTariff= initialTariffMutator(entry.getValue());
			if(newTariff != null)
				initialTariffs.put(entry.getKey(), newTariff);
		}
		for (Map.Entry<PowerType, TariffSpecification> initialTariff : initialTariffs.entrySet()) {
			addNewTariff(initialTariff.getValue());
		}
	}

	public void improveTariffs(int timeslotIndex, Map<PowerType, List<TariffSpecification>> competingTariffs) {
		System.out.println("IMPROVE TARIFFS: " + timeslotIndex);
		// TODO Auto-generated method stub
		this.competingTariffs = competingTariffs;
		System.out.println("TARIFFS:");
		System.out.println(tariffRepo.findAllTariffSpecifications().size());
		var tariffs = this.tariffRepo.findAllTariffSpecifications();
		for (TariffSpecification tariff : tariffs) {
			System.out.println(tariff.toString());
		}
		if((timeslotIndex - 1) % 6 == 0) {
			var myTariffs = tariffRepo.findTariffsByBroker(this.brokerContext.getBroker());
			for (Tariff tariff : myTariffs) {
				if(tariff.isActive()) {
					supersedeTariff(lowerTariff(tariff.getTariffSpec(), this.PERIODIC_DECREASE), tariff.getId());
					System.out.println("SUPERSEDED TARIFF");
				}
			}
		}
		//iterar por tariffRepo
		//chamar o modelo
		//alterar tarifas
		//enviar tarifas alteradas
		System.out.flush();
	}
	
	private void printTariffList(PowerType powerType, List<TariffSpecification> tariffs) {
		System.out.println(powerType.toString());
		for (TariffSpecification tariff : tariffs) {
			System.out.println(tariff.toString());
		}
	}
	
	private TariffSpecification initialTariffMutator(List<TariffSpecification> list) {
		//TODO: 
		// fce = sumt(Ce,t * -pdef) /  (sumt(Ce,t * -pv,i,t - pp,i) - psignup,i - Ff * pwithdraw,i - pwithdraw,0)
		// selecionar o melhor da lista e não 0S (Usar tariffEvaluatorHelper)
		if(list.get(0) != null) {
			TariffSpecification competingSpec = list.get(0);
			TariffSpecification copiedSpec = 
		              new TariffSpecification(this.brokerContext.getBroker(),
		                                      competingSpec.getPowerType());
			List<Rate> rates = competingSpec.getRates();
			for (Rate rate : rates) {				
				copiedSpec.addRate(rate); //needs to lowerTariff
			}
			TariffSpecification loweredSpec = lowerTariff(copiedSpec, INITIAL_DECREASE);
			loweredSpec.withEarlyWithdrawPayment(0);
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
		List<Rate> rates = tariff.getRates();
		for (Rate rate : rates) {
			Rate lowerRate = new Rate();
			if(rate.isFixed()) {
				//lower fixed rates (get params from rate and add to lowerRate) 
				lowerRate.withValue(rate.getValue() * 1 - decrease);
				lowerRate.withDailyBegin(rate.getDailyBegin());
				lowerRate.withDailyEnd(rate.getDailyEnd());
			}
			else {
				//lower Variable rates (get params from rate and add to lowerRate)
			}
			lowerTariff.addRate(rate);
		}
		return lowerTariff;
	}
	
	private void addNewTariff(TariffSpecification spec) {
	      this.tariffRepo.addSpecification(spec);
	      this.brokerContext.sendMessage(spec);
	}
	
	private void supersedeTariff(TariffSpecification spec, long oldTariffId) {
		spec.addSupersedes(oldTariffId);
		addNewTariff(spec);
	}
	
}
