package org.powertac.samplebroker.tariffoptimizer;

import org.deeplearning4j.rl4j.observation.Observation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class ObservationGenerator {

	public static Observation generate(double[] observations) {
		INDArray obsArray = Nd4j.createFromArray(observations);
		Observation obs = new Observation(obsArray);
		return obs;
	}
}
