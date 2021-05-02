package org.powertac.samplebroker.mlmodel;

import java.util.List;

import org.deeplearning4j.rl4j.learning.sync.Transition;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm.DoubleDQN;

public class DQNManager {

	private DoubleDQN doubleDQN;
	private DQNSource source;

	private Transition<Integer> lastTransition;
	private List<Transition<Integer>> transitions;
	
	public DQNManager(DQNSource source) {
		super();
		this.source = source;
		this.doubleDQN = new DoubleDQN(source, 1, 1); //VIEW OTHER PARAMS
	}
	
	public void predict() {
		
	}
	
	public void fit() {
		
	}
	
	public void sync() {
		this.doubleDQN.computeTDTargets(null);
	}
	
	public void loadModel() {
		
	}

	public void saveModel() {
		
	}
	
	
}
