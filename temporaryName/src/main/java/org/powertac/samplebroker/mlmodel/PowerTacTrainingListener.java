package org.powertac.samplebroker.mlmodel;

import org.deeplearning4j.rl4j.learning.IEpochTrainer;
import org.deeplearning4j.rl4j.learning.ILearning;
import org.deeplearning4j.rl4j.learning.listener.TrainingListener;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense;
import org.deeplearning4j.rl4j.observation.Observation;
import org.deeplearning4j.rl4j.util.IDataManager.StatEntry;

class PowerTacTrainingListener implements TrainingListener {

	private int trainingSteps = 0;
	private double cumulativeReward = 0;
	private QLearningDiscreteDense<Observation> model;
	private DQNSource source;
	private int expectedSteps;

	public PowerTacTrainingListener(QLearningDiscreteDense<Observation> model, DQNSource source, int expectedSteps) {
		this.model = model;
		this.source = source;
		this.expectedSteps = expectedSteps;
	}

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
		this.cumulativeReward += statEntry.getReward();
		this.trainingSteps++;
		System.out.println("Taining Step: " + this.trainingSteps + "/" + this.expectedSteps);
		System.out.println("Training Result: " + trainer.getStepCount() + " StatEntry reward: " + statEntry.getReward()
				+ " Reward average: " + this.cumulativeReward / trainingSteps);

		if (this.trainingSteps == this.expectedSteps - 2) {

			System.out.println("Trying to save model");
			this.source.saveModel(this.model.getTargetQNetwork());
		}

		System.out.println("Training Progress: " + this.cumulativeReward);
		return ListenerResponse.CONTINUE;
	}

	@Override
	public ListenerResponse onTrainingProgress(ILearning learning) {
		System.out.println("Training Progress: " + this.cumulativeReward);
		return ListenerResponse.CONTINUE;
	}

}