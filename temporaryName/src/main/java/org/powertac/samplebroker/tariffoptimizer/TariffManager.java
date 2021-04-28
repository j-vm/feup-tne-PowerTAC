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
		System.out.println("MY TARIFFS:");
		var tariffs = this.tariffRepo.findAllTariffs();
		for (Tariff tariff : tariffs) {
			System.out.println(tariff.toString());
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
			TariffSpecification spec = 
		              new TariffSpecification(this.brokerContext.getBroker(),
		                                      competingSpec.getPowerType());
			List<Rate> rates = competingSpec.getRates();
			for (Rate rate : rates) {				
				spec.addRate(rate); //needs to lowerTariff
			}
			//TariffSpecification spec = lowerTariff(list.get(0));
			spec.withEarlyWithdrawPayment(0);
			return spec;
		}
		return null;
	}
	
	private TariffSpecification raiseTariff(TariffSpecification tariff) {
		//TODO: Heuristic to raise tariff
		return null;
	}

	private TariffSpecification lowerTariff(TariffSpecification tariff) {
		//TODO: Heuristic to lower tariff
		
		return tariff;
	}
	
	private void addNewTariff(TariffSpecification spec) {
	      this.tariffRepo.addSpecification(spec);
	      this.brokerContext.sendMessage(spec);
	}
	
	private void supersedeTariff(TariffSpecification oldSpec, TariffSpecification spec) {
		spec.addSupersedes(oldSpec.getId());
		addNewTariff(spec);
	}
	
}
