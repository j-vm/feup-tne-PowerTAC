package org.powertac.samplebroker.mlmodel;

import org.deeplearning4j.rl4j.space.Encodable;
import org.nd4j.linalg.api.ndarray.INDArray;

public class PTacObservation implements Encodable {

	/**
	 * A singleton representing a skipped observation
	 */
	public static PTacObservation SkippedObservation = new PTacObservation(null);

	/**
	 * @return A INDArray containing the data of the observation
	 */
	private final INDArray data;

	public INDArray getData() {
		System.out.println("GETTING DATA: " + this.data.toString());
		return data;
	}

	@Override
	public double[] toArray() {
		return data.data().asDouble();
	}

	public boolean isSkipped() {
		return data == null;
	}

	public PTacObservation(INDArray data) {
		this.data = data;
	}

	/**
	 * Creates a duplicate instance of the current observation
	 * 
	 * @return
	 */
	public PTacObservation dup() {
		if (data == null) {
			return SkippedObservation;
		}

		return new PTacObservation(data.dup());
	}
}
