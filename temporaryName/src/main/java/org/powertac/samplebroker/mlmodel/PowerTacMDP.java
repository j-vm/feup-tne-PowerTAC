package org.powertac.samplebroker.mlmodel;

import java.util.concurrent.LinkedTransferQueue;

import org.deeplearning4j.gym.StepReply;
import org.deeplearning4j.rl4j.mdp.MDP;
import org.deeplearning4j.rl4j.observation.Observation;
import org.deeplearning4j.rl4j.space.ArrayObservationSpace;
import org.deeplearning4j.rl4j.space.DiscreteSpace;
import org.deeplearning4j.rl4j.space.ObservationSpace;
import org.powertac.samplebroker.tariffoptimizer.ObservationGenerator;

public class PowerTacMDP implements MDP<Observation, Integer, DiscreteSpace> {

	private static final double TANH_REWARD_FACTOR = 4;
	double BALANCE_WEIGHT = 0.5;
	double SUBSCRIBER_WEIGHT = 0.5;

	private int currentCustomers = 0;
	private int expectedSteps;
	private ObservationGenerator observationGenerator;

	private LinkedTransferQueue<Observation> obsIn;
	private LinkedTransferQueue<PowerTAC_ACTION> actionOut;

	private Observation lastObs;

	public enum PowerTAC_ACTION {
		STORAGE_UP, STORAGE_DOWN, PRODUCTION_UP, PRODUCTION_DOWN, CONSUMPTION_UP, CONSUMPTION_DOWN, STAY
	}

	public PowerTacMDP(LinkedTransferQueue<Observation> obsIn, LinkedTransferQueue<PowerTAC_ACTION> actionOut,
			Observation initialState, Integer expectedSteps) {
		super();
		this.obsIn = obsIn;
		this.actionOut = actionOut;
		this.lastObs = initialState;
		this.expectedSteps = expectedSteps;
	}

	private DiscreteSpace space = new DiscreteSpace(5);

	/*
	 * 0 - do nothing 1 - up 1x 2 - down 1x 3 - up 2x 4 - down 2x
	 */

	private ObservationSpace<Observation> observationSpace = new ArrayObservationSpace(new int[] { 1 });

	@Override
	public ObservationSpace<Observation> getObservationSpace() {
		return this.observationSpace;
	}

	@Override
	public DiscreteSpace getActionSpace() {
		return this.space;
	}

	@Override
	public void close() {
	}

	@Override
	public StepReply<Observation> step(Integer action) {

		System.out.println("[MDP] EXECUTIN STEP w action: " + PowerTAC_ACTION.values()[action].name());

		try {
			this.actionOut.transfer(PowerTAC_ACTION.values()[action]);
			System.out.println("[MDP] Action out:" + action);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Observation obs;
		try {
			obs = this.obsIn.take();

			// Increment current Customers
			this.currentCustomers += obs.getData().getInt(1);

			System.out.println("[MDP] Obs In: ");
			printObservation(obs);
			double reward = reward(obs);

			System.out.println("[MDP] Reward In: " + reward);
			this.lastObs = obs;
			return new StepReply<Observation>(obs, reward, isDone(), null);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public MDP<Observation, Integer, DiscreteSpace> newInstance() {
		return new PowerTacMDP(this.obsIn, this.actionOut, this.lastObs, this.expectedSteps);
	}

	@Override
	public Observation reset() {
		return this.lastObs;
	}

	private double reward(Observation to) {
		double game_point = to.getData().getInt(8) / (double) this.expectedSteps;
		double balance_weight = Math.tanh(game_point * PowerTacMDP.TANH_REWARD_FACTOR);
		double subscriber_weight = 1 - balance_weight;
		System.out.println(
				"Expected Steps: " + this.expectedSteps + "CurrentStep: " + to.getData().getInt(8) + "GamePoint: "
						+ game_point + "Balance Weight: " + balance_weight + "Subscriber Weight: " + subscriber_weight);
		int balanceDelta = to.getData().getInt(0);
		int subscriptionDelta = to.getData().getInt(1);
		return balanceDelta * balance_weight + subscriptionDelta * subscriber_weight;
	}

	private void printObservation(Observation obs) {
		System.out.println(obs.getData().toStringFull());
	}
}
