
package org.powertac.samplebroker.mlmodel;

import org.deeplearning4j.rl4j.learning.IEpochTrainer;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.configuration.QLearningConfiguration;
import org.deeplearning4j.rl4j.learning.listener.TrainingListener;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.observation.Observation;
import org.deeplearning4j.rl4j.util.IDataManager.StatEntry;

public class DQNManager extends Thread {

	private QLearningDiscreteDense<Observation> model;
	private DQNSource source;

	private PowerTacMDP mdp;

	public DQNManager(DQNSource source, PowerTacMDP mdp) {
		super();
		this.source = source;
		this.mdp = mdp;
		this.source.createModel();
		this.model = new QLearningDiscreteDense<Observation>(mdp, source.getPredictor(),
				QLearningConfiguration.builder().batchSize(1).targetDqnUpdateFreq(50).epsilonNbStep(120).doubleDQN(true)
						.targetDqnUpdateFreq(50).build());
		model.addListener(new PowerTacTrainingListener());
	}

	public void run() {
		model.train();
	}

}

class PowerTacTrainingListener implements TrainingListener {

	@Override
	public ListenerResponse onTrainingStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onTrainingEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public ListenerResponse onNewEpoch(IEpochTrainer trainer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenerResponse onEpochTrainingResult(IEpochTrainer trainer, StatEntry statEntry) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListenerResponse onTrainingProgress(ILearning learning) {
		// TODO Auto-generated method stub
		return null;
	}

}
