package org.powertac.samplebroker.tariffoptimizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;

import org.apache.commons.math3.util.Pair;
import org.deeplearning4j.rl4j.observation.Observation;
import org.powertac.common.Broker;
import org.powertac.common.Rate;
import org.powertac.common.Tariff;
import org.powertac.common.TariffSpecification;
import org.powertac.common.enumerations.PowerType;
import org.powertac.common.msg.TariffRevoke;
import org.powertac.common.repo.TariffRepo;
import org.powertac.samplebroker.interfaces.BrokerContext;
import org.powertac.samplebroker.mlmodel.DQNManager;
import org.powertac.samplebroker.mlmodel.DQNSource;
import org.powertac.samplebroker.mlmodel.PowerTacMDP;
import org.powertac.samplebroker.mlmodel.PowerTacMDP.PowerTAC_ACTION;

public class TariffManager {
	boolean DEBUG = false;
	double INITIAL_DECREASE = 0.2;
	double PERIODIC_DECREASE = -0.05;
	private LinkedTransferQueue<Observation> obsIn = new LinkedTransferQueue<>();
	private LinkedTransferQueue<PowerTAC_ACTION> actionOut = new LinkedTransferQueue<>();
	private DQNManager dqnManager;
	private DQNSource dqnSource = new DQNSource();
	private PowerTacMDP mdp;
	private double[] previousObs = new double[8];

	public TariffManager(TariffRepo tariffRepo, BrokerContext brokerContext) {
		super();
		this.tariffRepo = tariffRepo;
		this.brokerContext = brokerContext;
		this.dqnSource.createModel();
	}

	public void initialize(Observation initialState) {
		this.mdp = new PowerTacMDP(this.obsIn, this.actionOut, initialState);
		this.dqnManager = new DQNManager(dqnSource, mdp);
		this.dqnManager.start();
	}

	private Map<PowerType, List<TariffSpecification>> competingTariffs;
	private TariffRepo tariffRepo;
	private BrokerContext brokerContext;
	private Set<PowerType> powerTypes = new HashSet<>();

	public Observation observe(Integer balance, Integer subscriptions, Double storageConsumption,
			Integer storageSubscriptions, Double productionConsumption, Integer productionSubscriptions,
			Double consumptionConsumption, Integer consumptionSubscriptions) {
		System.out.println("MADE OBSERVATION!");
		double[] obs = new double[8];
		obs[0] = (double) balance;
		obs[1] = (double) subscriptions;
		obs[2] = (double) storageConsumption;
		obs[3] = (double) storageSubscriptions;
		obs[4] = (double) productionConsumption;
		obs[5] = (double) productionSubscriptions;
		obs[6] = (double) consumptionConsumption;
		obs[7] = (double) consumptionSubscriptions;

		double[] changeObservation = new double[8];

		changeObservation[0] = this.previousObs[0] - obs[0];
		changeObservation[1] = this.previousObs[1] - obs[1];
		changeObservation[2] = this.previousObs[2] - obs[2];
		changeObservation[3] = this.previousObs[3] - obs[3];
		changeObservation[4] = this.previousObs[4] - obs[4];
		changeObservation[5] = this.previousObs[5] - obs[5];
		changeObservation[6] = this.previousObs[6] - obs[6];
		changeObservation[7] = this.previousObs[7] - obs[7];

		this.previousObs = obs;

		return ObservationGenerator.generate(changeObservation);

	}

	public List<TariffSpecification> createNewTariffs(Map<PowerType, List<TariffSpecification>> competingTariffs) {
		// TODO Auto-generated method stub
		List<TariffSpecification> initialTariffs = new ArrayList<TariffSpecification>();
		this.competingTariffs = competingTariffs;
		for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet()) {
			if (!this.powerTypes.contains(entry.getKey())) {
				if (this.DEBUG)
					System.out.println("Creating new Tariff: ");
				TariffSpecification newTariff = initialTariffMutator(entry.getValue());
				if (newTariff != null) {
					initialTariffs.add(newTariff);
					this.powerTypes.add(newTariff.getPowerType());
				}
			}
		}
		return initialTariffs;
	}

	private boolean firstAction = true;

	public List<Pair<TariffSpecification, TariffSpecification>> alterTariffs(int timeslotIndex,
			List<TariffSpecification> specs, Observation obs) {

		List<Pair<TariffSpecification, TariffSpecification>> newTariffSpecs = new ArrayList<Pair<TariffSpecification, TariffSpecification>>();
		System.out.println("[TM] Altering Tariffs at timeslot: " + timeslotIndex);
		if (firstAction) {
			this.firstAction = false;
		} else {
			try {
				this.obsIn.transfer(obs);
				System.out.println("[TM] Obs in:" + obs.toString());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (this.DEBUG)
			System.out.println("");
		if (this.DEBUG)
			System.out.println("");
		if (this.DEBUG)
			System.out.println("Revision Period - Time: " + timeslotIndex);
		if (this.DEBUG)
			System.out.println("________________________________________________");
		if (this.DEBUG)
			System.out.println("My tariffs:");
		if (this.DEBUG)
			System.out.println("");

		try {
			PowerTAC_ACTION action = this.actionOut.take();
			System.out.println("[TM] Action Out: " + action.name());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (TariffSpecification spec : specs) {
			System.out.println("Updating spec: " + spec.getId());
			if (spec.getPowerType().isConsumption()) {
				var newSpec = this.alterConsumptionTariff(spec, PERIODIC_DECREASE); // TODO: change periodic decrease by
																					// dqn
				if (newSpec != null)
					newTariffSpecs.add(new Pair<>(spec, newSpec));

			} else if (spec.getPowerType().isProduction()) {
				var newSpec = this.alterProductionTariff(spec, PERIODIC_DECREASE);
				if (newSpec != null)
					newTariffSpecs.add(new Pair<>(spec, newSpec));

			} else if (spec.getPowerType().isStorage()) {
				var newSpec = this.alterStorageTariff(spec, PERIODIC_DECREASE);
				if (newSpec != null)
					newTariffSpecs.add(new Pair<>(spec, newSpec));

			} else if (spec.getPowerType().isInterruptible()) {
				var newSpec = this.alterInterruptableTariff(spec, PERIODIC_DECREASE);
				if (newSpec != null)
					newTariffSpecs.add(new Pair<>(spec, newSpec));
			}

		}
//			for (Map.Entry<PowerType, List<TariffSpecification>> entry : competingTariffs.entrySet())
//				printTariffSpecificationList(entry.getKey(), entry.getValue());
//	
//			if(this.DEBUG) System.out.println("");
//			if(this.DEBUG) System.out.println("TNA Tariff update:");
//			if(this.DEBUG) System.out.println("");
//			
//			var myTariffs = List.copyOf(tariffRepo.findTariffsByBroker(this.brokerContext.getBroker()));
//			if(this.DEBUG) System.out.println("MyTariffs: " + myTariffs.toString());
//			for (Tariff tariff : myTariffs) {
//				System.out.println("tarif broker: " + tariff.getBroker());
//				System.out.println("tarif state: " + tariff.getState());
//				System.out.println("tariff expired: " + tariff.isExpired());
//				System.out.println("tarif is subscribable: " + tariff.isSubscribable());
//				System.out.println("tarif is expired: " + tariff.isExpired());
//				if(this.DEBUG) System.out.println("tariff is active? " + tariff.isActive());
//				if(tariff.getIsSupersededBy() == null) {
//
//					TariffSpecification newTariffSpec = lowerTariff(tariff.getTariffSpec(), this.PERIODIC_DECREASE); //SUBSTITUIR LOWER TARIFF COM MODELO AI
//					if(this.DEBUG) System.out.println(tariff.toString() + "  ->  " + newTariffSpec.toString());
//					if(this.DEBUG) System.out.println("");
//					
//					supersedeTariff(newTariffSpec, tariff.getTariffSpec());
//				}
		// ************************************testing************************************************
		// */
		// "mytariffs" from line 66 arent the same that entered the function, wich ones
		// should we use???
		/*
		 * for(List<TariffSpecification> lists : competingTariffs.values()){
		 * System.out.println("--------------------------------------------\n");
		 * for(TariffSpecification tarif : lists){ TariffSpecification newTariff =
		 * tarif;
		 * 
		 * System.out.println("competing tariffs: " + tarif.toString());
		 * System.out.println("broker: " + tarif.getBroker());
		 * System.out.println("is valid: " + tarif.isValid()); List<Rate> rates =
		 * tarif.getRates(); for (Rate rate : rates){ rate.withValue(-0.8);
		 * rate.withWeeklyBegin(1); rate.withWeeklyEnd(7); System.out.println("rates: "
		 * + rate.toString()); newTariff.addRate(rate); }
		 * newTariff.addSupersedes(tarif.getId()); }
		 * System.out.println("--------------------------------------------\n"); }
		 */

		if (this.DEBUG)
			System.out.println("NEWTARIFFSPEC spec: " + newTariffSpecs);
		if (this.DEBUG)
			System.out.flush();
		return newTariffSpecs;
	}

	private TariffSpecification alterInterruptableTariff(TariffSpecification spec, double change) {
		return this.alterTariff(spec, change);
	}

	private TariffSpecification alterStorageTariff(TariffSpecification spec, double change) {
		// TODO: Add storage alteration heuristic here
		return null;
	}

	private TariffSpecification alterProductionTariff(TariffSpecification spec, double change) {
		return this.alterTariff(spec, -change);
	}

	private TariffSpecification alterConsumptionTariff(TariffSpecification spec, double change) {
		return this.alterTariff(spec, change);
	}

	private void printTariffSpecificationList(PowerType powerType, List<TariffSpecification> tariffs) {
		if (this.DEBUG)
			System.out.println(powerType.toString());
		for (TariffSpecification tariff : tariffs) {
			if (this.DEBUG)
				System.out.println(tariff.toString());
		}
	}

	private TariffSpecification initialTariffMutator(List<TariffSpecification> list) {
		// TODO:
		// fce = sumt(Ce,t * -pdef) / (sumt(Ce,t * -pv,i,t - pp,i) - psignup,i - Ff *
		// pwithdraw,i - pwithdraw,0)
		// selecionar o melhor da lista e não 0S (Usar tariffEvaluatorHelper)
		if (list.get(0) != null) {
			if (this.DEBUG)
				System.out.println("Mutating tariff: " + list.get(0).toString());
			TariffSpecification competingSpec = list.get(0);

			TariffSpecification loweredSpec = alterTariff(competingSpec, INITIAL_DECREASE);
			loweredSpec.withEarlyWithdrawPayment(0);
			if (this.DEBUG)
				System.out.println("Signup Payment:" + loweredSpec.getSignupPayment());
			loweredSpec.withSignupPayment(competingSpec.getSignupPayment());
			if (this.DEBUG)
				System.out.println("Min Duraition:" + loweredSpec.getMinDuration());
			loweredSpec.withMinDuration(competingSpec.getMinDuration());
			if (this.DEBUG)
				System.out.println("Periodic Payment:" + loweredSpec.getPeriodicPayment());
			loweredSpec.withPeriodicPayment(competingSpec.getPeriodicPayment());
			return loweredSpec;
		}
		return null;
	}

	private TariffSpecification alterTariff(TariffSpecification tariff, double decrease) {

		TariffSpecification lowerTariff = new TariffSpecification(this.brokerContext.getBroker(),
				tariff.getPowerType());
		alterRates(tariff, decrease, lowerTariff);
		return lowerTariff;
	}

	private TariffSpecification alterTariff(TariffSpecification tariff, double decrease, Broker broker,
			PowerType powerType) {
		TariffSpecification lowerTariff = new TariffSpecification(broker, powerType);
		alterRates(tariff, decrease, lowerTariff);
		return lowerTariff;
	}

	private void alterRates(TariffSpecification tariff, double decrease, TariffSpecification lowerTariff) {
		// TODO: Heuristic to lower rates. From variable tariff create more favourable
		// fixed rate tariff
		List<Rate> rates = tariff.getRates();
		for (Rate rate : rates) {
			Rate alteredRate = new Rate();
			if (rate.isFixed()) {
				// lower fixed rates (get params from rate and add to alteredRate)
				alteredRate.withValue(rate.getValue() * 1 - decrease);
				if (this.DEBUG)
					System.out.println("Fixed Rate from: " + rate.getValue() + " to: " + alteredRate.getValue());
				// copyPeriods(rate, alteredRate);

			} else {
				alteredRate.withValue(rate.getExpectedMean() * 1 - decrease);
				if (this.DEBUG)
					System.out.println(
							"Variable Rate from: " + rate.getExpectedMean() + " to: " + alteredRate.getValue());
				// copyPeriods(rate, alteredRate);
			}
			lowerTariff.addRate(rate);
		}
	}

	private void copyPeriods(Rate rate, Rate alteredRate) {
		if (this.DEBUG)
			System.out.println("Daily Begin:" + rate.getDailyBegin());
		alteredRate.withDailyBegin(rate.getDailyBegin());
		if (this.DEBUG)
			System.out.println("Daily End:" + rate.getDailyBegin());
		alteredRate.withDailyEnd(rate.getDailyEnd());
		if (this.DEBUG)
			System.out.println("hWeekly Begin:" + rate.getWeeklyBegin());
		alteredRate.withWeeklyBegin(rate.getWeeklyBegin());
		if (this.DEBUG)
			System.out.println("Weekly End:" + rate.getWeeklyEnd());
		alteredRate.withWeeklyEnd(rate.getWeeklyEnd());
	}

	private void addNewTariff(TariffSpecification spec) {
		if (this.DEBUG)
			System.out.print("ADDING TARIFF: " + spec.toString() + "  "
					+ this.tariffRepo.findTariffsByBroker(this.brokerContext.getBroker()).size() + "->");
		Tariff newTariff = new Tariff(spec);
		newTariff.init();
		this.tariffRepo.addTariff(newTariff);
		if (this.DEBUG)
			System.out.println(this.tariffRepo.findTariffsByBroker(this.brokerContext.getBroker()).size());
		this.brokerContext.sendMessage(newTariff);
	}

	private void supersedeTariff(TariffSpecification spec, TariffSpecification oldSpec) {
		spec.addSupersedes(oldSpec.getId());
		addNewTariff(spec);
		// revoke the old one
		TariffRevoke revoke = new TariffRevoke(brokerContext.getBroker(), oldSpec);
		brokerContext.sendMessage(revoke);
	}

}
