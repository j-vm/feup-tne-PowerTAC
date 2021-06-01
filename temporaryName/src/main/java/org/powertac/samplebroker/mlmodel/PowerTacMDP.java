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

	private static final double TANH_REWARD_FACTOR = 2.5;


	private int currentCustomers = 0;
	private int expectedSteps;

	public int getExpectedSteps() {
		return expectedSteps;
	}

	
	private double cumulativeReward;
	private int numberOfRewards = 0;
	private double totalReward = 0;
	
	public double getCumulativeReward() {
		return cumulativeReward;
	}

	

	private ObservationGenerator observationGenerator;

	private LinkedTransferQueue<Observation> obsIn;
	private LinkedTransferQueue<PowerTAC_ACTION> actionOut;

	private Observation lastObs;
	private int[] actions = {0,0,0,0,0,0,0};

	public int[] getActions(){
		return this.actions;
	}
	public enum PowerTAC_ACTION {
		STAY, STORAGE_UP, STORAGE_DOWN, PRODUCTION_UP, PRODUCTION_DOWN, CONSUMPTION_UP, CONSUMPTION_DOWN
	}

	public PowerTacMDP(LinkedTransferQueue<Observation> obsIn, LinkedTransferQueue<PowerTAC_ACTION> actionOut,
			Observation initialState, Integer expectedSteps) {
		super();
		this.obsIn = obsIn;
		this.actionOut = actionOut;
		this.lastObs = initialState;
		this.expectedSteps = expectedSteps;
	}

	private DiscreteSpace space = new DiscreteSpace(7);

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
		
		try {
			System.out.println("Action int:" + action);
			this.actionOut.transfer(PowerTAC_ACTION.values()[action]);
			System.out.println("Action:" + PowerTAC_ACTION.values()[action]);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.actions[action]++;
		Observation obs;
		try {
			obs = this.obsIn.take();

			// Increment current Customers
			this.currentCustomers += obs.getData().getInt(1);
			
			// Calculating reward
			this.numberOfRewards++;
			double reward = reward(obs);

			// calculating cumulative reward
			this.totalReward += reward;
			this.cumulativeReward = totalReward / numberOfRewards;

			// Printing reward
			System.out.println("Reward from last action:" + reward);
			System.out.println("Cumulative Reward:" + this.cumulativeReward);

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
		double game_point = this.numberOfRewards / ((double)this.expectedSteps / 6);
		double balance_weight = Math.tanh(game_point * PowerTacMDP.TANH_REWARD_FACTOR);
		double subscriber_weight = 1 - balance_weight;
		double balanceDelta = to.getData().getDouble(0);
		double subscriptionDelta = to.getData().getDouble(1);
		System.out.println("balance_weight:" + balance_weight +"balanceDelta:" +  balanceDelta + "subscriber_weight:" +  subscriber_weight + "subscriptionDelta:" + subscriptionDelta);
		return balanceDelta * balance_weight + subscriptionDelta * subscriber_weight;
	}

	private void printObservation(Observation obs) {
		System.out.println(obs.getData().toStringFull());
	}
}
