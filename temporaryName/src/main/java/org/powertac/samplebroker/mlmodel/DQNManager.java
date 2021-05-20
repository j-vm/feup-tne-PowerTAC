
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
		System.out.println("Training Started");
		return ListenerResponse.CONTINUE;
	}

	@Override
	public void onTrainingEnd() {
		System.out.println("Training Ended");
	}

	@Override
	public ListenerResponse onNewEpoch(IEpochTrainer trainer) {
		System.out.println("New Epoch: " + trainer.getStepCount());
		return ListenerResponse.CONTINUE;
	}

	@Override
	public ListenerResponse onEpochTrainingResult(IEpochTrainer trainer, StatEntry statEntry) {
		System.out
				.println("Training Result: " + trainer.getStepCount() + " StatEntry reward: " + statEntry.getReward());
		return ListenerResponse.CONTINUE;
	}

	@Override
	public ListenerResponse onTrainingProgress(ILearning learning) {
		System.out.println("Training Progress: " + learning.getPolicy().toString());
		return ListenerResponse.CONTINUE;
	}

}
