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

	double BALANCE_WEIGHT = 0.5;
	double SUBSCRIBER_WEIGHT = 0.5;

	private ObservationGenerator observationGenerator;
	private LinkedTransferQueue<Observation> obsIn;

	private LinkedTransferQueue<PowerTAC_ACTION> actionOut;

	private Observation lastObs;

	public enum PowerTAC_ACTION {
		STORAGE_UP, STORAGE_DOWN, PRODUCTION_UP, PRODUCTION_DOWN, CONSUMPTION_UP, CONSUMPTION_DONW, STAY
	}

	public PowerTacMDP(LinkedTransferQueue<Observation> obsIn, LinkedTransferQueue<PowerTAC_ACTION> actionOut,
			Observation initialState) {
		super();
		this.obsIn = obsIn;
		this.actionOut = actionOut;
		this.lastObs = initialState;
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
			System.out.println("[MDP] Obs In:" + obs.toString());
			double reward = reward(lastObs, obs);
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
		return new PowerTacMDP(this.obsIn, this.actionOut, this.lastObs);
	}

	@Override
	public Observation reset() {
		return this.lastObs;
	}

	private double reward(Observation from, Observation to) {
		int balanceDelta = to.getData().getInt(0) - from.getData().getInt(0);
		int subscriptionDelta = to.getData().getInt(1) - from.getData().getInt(1);
		return balanceDelta * BALANCE_WEIGHT + subscriptionDelta * SUBSCRIBER_WEIGHT;
	}

}
