
package org.powertac.samplebroker.mlmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class DQNSource {

	private String fileName = "targetQ";

	private MultiLayerConfiguration multiLayerConf = new NeuralNetConfiguration.Builder().seed(123)
			.updater(new Nesterovs(0.1, 0.9)) // High Level Configuration
			.list() // For configuring MultiLayerNetwork we call the list method
			.layer(0,
					new DenseLayer.Builder().nIn(9).nOut(8).weightInit(WeightInit.XAVIER).activation(Activation.RELU)
							.build()) // Configuring Layers
			.layer(1,
					new DenseLayer.Builder().nIn(8).nOut(7).weightInit(WeightInit.XAVIER).activation(Activation.RELU)
							.build())
			.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.XENT).nIn(7).nOut(7)
					.weightInit(WeightInit.XAVIER).activation(Activation.SIGMOID).build())
			.build(); // Building Configuration

	private MultiLayerNetwork predictMLN = new MultiLayerNetwork(this.multiLayerConf);
	private DQN predictor;

	public DQN getPredictor() {
		return predictor;
	}

	public void setPredictor(DQN predictor) {
		this.predictor = predictor;
	}

	public void createModel() {
		this.predictor = new DQN<>(this.predictMLN);
		this.predictor.reset();
	}

	public void loadModel() {
		this.loadModel(this.fileName);
	}

	public void loadModel(String filename) {
		try { // constructor of File class having file as argument
			File file = new File(filename); // creates a buffer reader input stream
			BufferedReader br = new BufferedReader(new FileReader(file));
			String model = "";
			int r = 0, i = 0;
			while ((r = br.read()) != -1) {
				model = addChar(model, (char) r, i);
				i++;
			}
			this.predictor = DQN.load(model);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void saveModel(IDQN idqn) {
		this.saveModel(this.fileName, idqn);
	}

	public void saveModel(String filename, IDQN idqn) {
		FileOutputStream fos = null;
		File file;
		try {
			file = new File(filename);
			fos = new FileOutputStream(file);

			if (!file.exists()) {
				file.createNewFile();
			}

			this.predictor.save(fos);
			fos.flush();
			System.out.println("DQN Model Saved Successfully to:  " + filename);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioe) {
				System.out.println("Error in closing DQN File Output Stream");
			}
		}
	}

	private String addChar(String str, char ch, int position) {
		StringBuilder sb = new StringBuilder(str);
		sb.insert(position, ch);
		return sb.toString();
	}

	public String getFileName() {
		return this.fileName;
	}

}
