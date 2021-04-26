package org.powertac.samplebroker.tariffoptimizer;
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
	
	private Map<PowerType, List<TariffSpecification>> competingTariffs;
	
	public Map<PowerType, TariffSpecification> createInitialTariffs(Map<PowerType, List<TariffSpecification>> competingTariffs, BrokerContext brokerContext) {
		// TODO Auto-generated method stub	
		Map<PowerType, TariffSpecification> initialTariffs = new HashMap<PowerType, TariffSpecification>();
		this.competingTariffs = competingTariffs; 
		System.out.println("Competing Tariffs: ");
		for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet()) {
			//printTariffList(entry.getKey(), entry.getValue());
			TariffSpecification newTariff= initialTariffMutator(entry.getValue());
			initialTariffs.put(entry.getKey(), newTariff);
		}
		
		return initialTariffs;
	}

	public Map<PowerType, TariffSpecification> improveTariffs(int timeslotIndex, TariffRepo tariffRepo,
			Map<PowerType, List<TariffSpecification>> competingTariffs, BrokerContext brokerContext) {
		System.out.println("Timeslot index: " + timeslotIndex);
		// TODO Auto-generated method stub

		this.competingTariffs = competingTariffs;
		System.out.println("Tariff Repo: ");
		var tariffs = tariffRepo.findAllTariffs();
		for (Tariff tariff : tariffs) {
			System.out.println(tariff.toString());
		}
		//iterar por tariffRepo
		//chamar o modelo
		//alterar tarifas
		//enviar tarifas alteradas
		System.out.flush();
		return null;
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
		// selecionar o melhor da lista
		// Remove exit cost then lowerTariff(tariff)
		return null;
	}
	
	private TariffSpecification raiseTariff(TariffSpecification tariff) {
		//TODO: Heuristic to raise tariff
		return null;
	}

	private TariffSpecification lowerTariff(TariffSpecification tariff) {
		//TODO: Heuristic to lower tariff
		return null;
	}
}
