package org.powertac.samplebroker.mlmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.deeplearning4j.rl4j.learning.sync.Transition;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm.DoubleDQN;
import org.deeplearning4j.rl4j.observation.Observation;
import org.nd4j.linalg.api.ndarray.INDArray;

public class DQNManager {

	private DoubleDQN doubleDQN;
	private DQNSource source;

	private Map<Integer, Observation> Observations;
	
	private List<Transition<Integer>> transitions = new ArrayList<Transition<Integer>>();

	public DQNManager(DQNSource source) {
		super();
		this.source = source;
		this.doubleDQN = new DoubleDQN(source, 1, 1); // VIEW OTHER PARAMS
	}

	public void predict(INDArray inputs, int timeSlot) {
		Observation obs = new Observation(inputs);
		this.source.getQNetwork().output(obs);
	}

	public void fit() {
		
	}

	public void sync() {
		this.doubleDQN.computeTDTargets(this.transitions);
	}

}
