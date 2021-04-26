package org.powertac.samplebroker.tariffoptimizer;
import org.powertac.common.Tariff;
import org.powertac.common.TariffSpecification;
import org.powertac.common.enumerations.PowerType;
import org.powertac.common.repo.TariffRepo;
import org.powertac.samplebroker.interfaces.BrokerContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TariffManager {

	public Map<PowerType, List<TariffSpecification>> createInitialTariffs(TariffRepo tariffRepo, Map<PowerType, List<TariffSpecification>> competingTariffs,
			BrokerContext brokerContext) {
		// TODO Auto-generated method stub
		System.out.println("Tariff Repo: ");
		var tariffs = tariffRepo.findAllTariffs();
		for (Tariff tariff : tariffs) {
			System.out.println(tariff.toString());
		}
		System.out.println("Competing Tariffs: ");
		for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet())
            printTariffList(entry.getKey(), entry.getValue());
		return null;
	}

	public Map<PowerType, List<TariffSpecification>> improveTariffs(int timeslotIndex, TariffRepo tariffRepo,
			Map<PowerType, List<TariffSpecification>> competingTariffs, BrokerContext brokerContext) {
		System.out.println("Timeslot index: " + timeslotIndex);
		// TODO Auto-generated method stub
			System.out.println("Tariff Repo: ");
			var tariffs = tariffRepo.findAllTariffs();
			for (Tariff tariff : tariffs) {
				System.out.println(tariff.toString());
			}
			System.out.println("Competing Tariffs: ");
			for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet())
	            printTariffList(entry.getKey(), entry.getValue());
			System.out.flush();
			return null;
	}
	
	private void printTariffList(PowerType powerType, List<TariffSpecification> tariffs) {
		System.out.println(powerType.toString());
		for (TariffSpecification tariff : tariffs) {
			System.out.println(tariff.toString());
		}
	}
	
}
