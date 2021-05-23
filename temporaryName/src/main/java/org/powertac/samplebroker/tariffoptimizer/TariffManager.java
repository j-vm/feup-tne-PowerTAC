package org.powertac.samplebroker.tariffoptimizer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;

import org.apache.commons.math3.util.Pair;
import org.deeplearning4j.rl4j.observation.Observation;
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
	double TARIFF_CHANGE = 0.05;
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

	public void initialize(Observation initialState, int expectedSteps) {
		this.mdp = new PowerTacMDP(this.obsIn, this.actionOut, initialState, expectedSteps);
		this.dqnManager = new DQNManager(dqnSource, mdp);
		this.dqnManager.start();
	}

	private Map<PowerType, List<TariffSpecification>> competingTariffs;
	private TariffRepo tariffRepo;
	private BrokerContext brokerContext;
	private Set<PowerType> powerTypes = new HashSet<>();

	public Observation observe(Double balance, Double subscriptions, Double storageConsumption,
			Double storageSubscriptions, Double productionConsumption, Double productionSubscriptions,
			Double consumptionConsumption, Double consumptionSubscriptions, Integer timeSlot) {
		System.out.println("MADE OBSERVATION!");
		double[] obs = new double[9];
		obs[0] = (double) balance;
		obs[1] = (double) subscriptions;
		obs[2] = (double) storageConsumption;
		obs[3] = (double) storageSubscriptions;
		obs[4] = (double) productionConsumption;
		obs[5] = (double) productionSubscriptions;
		obs[6] = (double) consumptionConsumption;
		obs[7] = (double) consumptionSubscriptions;
		obs[8] = (double) timeSlot;

		double[] changeObservation = new double[9];

		changeObservation[0] = obs[0] - this.previousObs[0];
		changeObservation[1] = obs[1] - this.previousObs[1];
		changeObservation[2] = obs[2] - this.previousObs[2];
		changeObservation[3] = obs[3] - this.previousObs[3];
		changeObservation[4] = obs[4] - this.previousObs[4];
		changeObservation[5] = obs[5] - this.previousObs[5];
		changeObservation[6] = obs[6] - this.previousObs[6];
		changeObservation[7] = obs[7] - this.previousObs[7];
		changeObservation[8] = obs[8];
		this.previousObs = obs;

		return ObservationGenerator.generate(changeObservation);

	}

	// Generates [storageConsumption; storageSubscriptions; productionConsumption;
	// productionSubscriptions; consumptionConsumption; consumptionSubscriptions;]

	private double[] generateMetricsByPowerType() {
		double[] consumptions = new double[6];
		return consumptions;
	}

	public List<TariffSpecification> createNewTariffs(Map<PowerType, List<TariffSpecification>> competingTariffs) {

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

			for (TariffSpecification spec : specs) {
				switch (action) {
				case STORAGE_UP:
					if (spec.getPowerType().isStorage()) {
						var newSpec = this.alterTariff(spec, TARIFF_CHANGE);
						if (newSpec != null)
							newTariffSpecs.add(new Pair<>(spec, newSpec));
					}
					break;
				case STORAGE_DOWN:
					if (spec.getPowerType().isStorage()) {
						var newSpec = this.alterTariff(spec, -TARIFF_CHANGE);
						if (newSpec != null)
							newTariffSpecs.add(new Pair<>(spec, newSpec));
					}
					break;
				case PRODUCTION_UP:
					if (spec.getPowerType().isProduction()) {
						var newSpec = this.alterTariff(spec, TARIFF_CHANGE);
						if (newSpec != null)
							newTariffSpecs.add(new Pair<>(spec, newSpec));
					}
					break;
				case PRODUCTION_DOWN:
					if (spec.getPowerType().isProduction()) {
						var newSpec = this.alterTariff(spec, -TARIFF_CHANGE);
						if (newSpec != null)
							newTariffSpecs.add(new Pair<>(spec, newSpec));
					}
					break;
				case CONSUMPTION_UP:
					if (spec.getPowerType().isConsumption()) {
						var newSpec = this.alterTariff(spec, TARIFF_CHANGE);
						if (newSpec != null)
							newTariffSpecs.add(new Pair<>(spec, newSpec));
					}
					break;
				case CONSUMPTION_DOWN:
					if (spec.getPowerType().isConsumption()) {
						var newSpec = this.alterTariff(spec, -TARIFF_CHANGE);
						if (newSpec != null)
							newTariffSpecs.add(new Pair<>(spec, newSpec));
					}
					break;
				case STAY:
					System.out.println("No Tariffs Changed");
					break;
				default:
					throw new Exception("Unknown action: " + action);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.DEBUG)
			System.out.println("NEWTARIFFSPEC spec: " + newTariffSpecs);
		if (this.DEBUG)
			System.out.flush();
		return newTariffSpecs;
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

	private TariffSpecification alterTariff(TariffSpecification tariff, double alterRatio) {

		TariffSpecification lowerTariff = new TariffSpecification(this.brokerContext.getBroker(),
				tariff.getPowerType());
		alterRates(tariff, alterRatio, lowerTariff);
		return lowerTariff;
	}

	private void alterRates(TariffSpecification tariff, double alterRatio, TariffSpecification lowerTariff) {
		// TODO: Heuristic to lower rates. From variable tariff create more favourable
		// fixed rate tariff
		List<Rate> rates = tariff.getRates();
		for (Rate rate : rates) {
			Rate alteredRate = new Rate();
			if (rate.isFixed()) {
				// lower fixed rates (get params from rate and add to alteredRate)
				alteredRate.withValue(rate.getValue() * (1 + alterRatio));

				System.out.println("Fixed Rate from: " + rate.getValue() + " to: " + alteredRate.getValue());
				// copyPeriods(rate, alteredRate);

			} else {
				alteredRate.withValue(rate.getExpectedMean() * (1 + alterRatio));

				System.out.println("Variable Rate from: " + rate.getExpectedMean() + " to: " + alteredRate.getValue());
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
