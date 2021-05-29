package org.powertac.samplebroker.tariffoptimizer;

import org.deeplearning4j.rl4j.observation.Observation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.StandardizeStrategy;
import org.nd4j.linalg.dataset.api.preprocessor.stats.DistributionStats;
import org.nd4j.linalg.factory.Nd4j;

public class ObservationGenerator {
//	obs[0] = (double) balance;
//	obs[1] = (double) subscriptions;
//	obs[2] = (double) storageConsumption;
//	obs[3] = (double) storageSubscriptions;
//	obs[4] = (double) productionConsumption;
//	obs[5] = (double) productionSubscriptions;
//	obs[6] = (double) consumptionConsumption;
//	obs[7] = (double) consumptionSubscriptions;
//	obs[8] = (double) timeSlot;

	private static INDArray mean = Nd4j.createFromArray(new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 60 });
	private static INDArray std = Nd4j.createFromArray(new double[] { 5000, 1000, 10000, 100, 10000, 100, 10000, 100, 35 });

	public static Observation generate(double[] observations) {
		INDArray obsArray = Nd4j.createFromArray(observations);
		
		System.out.println("Obs before norm: " + obsArray.toString());
		
		StandardizeStrategy strat = new StandardizeStrategy();
		strat.preProcess(obsArray, null, new DistributionStats(mean, std));
		Observation obs = new Observation(obsArray);

		System.out.println("Obs after norm: " + obsArray.toString());
		return obs;
	}
}
