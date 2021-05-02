package org.powertac.samplebroker.mlmodel;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.rl4j.learning.sync.qlearning.TargetQNetworkSource;
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.TDTargetAlgorithm.DoubleDQN;
import org.deeplearning4j.rl4j.network.configuration.DQNDenseNetworkConfiguration;
import org.deeplearning4j.rl4j.network.dqn.DQN;
import org.deeplearning4j.rl4j.network.dqn.DQNFactory;
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense;
import org.deeplearning4j.rl4j.network.dqn.IDQN;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nesterovs;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;


public class DQNSource implements TargetQNetworkSource{
	
	
	private int[] shape = {2, 3, 4};
	private	MultiLayerConfiguration multiLayerConf = new NeuralNetConfiguration.Builder()
			  .seed(123)
			  .updater(new Nesterovs(0.1, 0.9)) //High Level Configuration
			  .list() //For configuring MultiLayerNetwork we call the list method
			  .layer(0, new DenseLayer.Builder().nIn(6).nOut(6).weightInit(WeightInit.XAVIER).activation(Activation.RELU).build()) //Configuring Layers
			  .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.XENT).nIn(6).nOut(3).weightInit(WeightInit.XAVIER).activation(Activation.SIGMOID).build())
			  .build(); //Building Configuration
	
	private MultiLayerNetwork predictMLN = new MultiLayerNetwork(this.multiLayerConf);
	private DQN predictor = new DQN<>(this.predictMLN);
	private MultiLayerNetwork targetMLN = new MultiLayerNetwork(this.multiLayerConf);
	private DQN target = new DQN<>(this.targetMLN);

	
	public void createModel() {
		
	}
	
	public void predict() {
		
	}
	
	public void fit() {
		
	}
	
	public void sync() {
		
	}
	
	public void loadModel() {
		
	}

	public void saveModel() {
		
	}

	@Override
	public IDQN getQNetwork() {
		return (IDQN)this.predictor;
	}

	@Override
	public IDQN getTargetQNetwork() {
		return (IDQN)this.target;
	}
}
