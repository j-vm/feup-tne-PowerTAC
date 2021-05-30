
package org.powertac.samplebroker.mlmodel;

import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.deeplearning4j.rl4j.observation.Observation;

public class DQNManager extends Thread {

	private QLearningDiscreteDense<Observation> model;
	private DQNSource source;

	private PowerTacMDP mdp;

	public DQNManager(DQNSource source, PowerTacMDP mdp) {
		super();
		this.source = source;
		this.mdp = mdp;
		this.source.loadModel();
		this.model = new QLearningDiscreteDense<Observation>(mdp, source.getPredictor(),
				QLearningConfiguration.builder().batchSize(1).epsilonNbStep(120).doubleDQN(true)
						.targetDqnUpdateFreq((mdp.getExpectedSteps() / 6) - 2).build());
		model.addListener(new PowerTacTrainingListener(model, source, mdp.getExpectedSteps()));
	}

	public IDQN getModelTargetDQN() {
		return this.model.getTargetQNetwork();
	}

	public void run() {
		model.train();
	}

}
