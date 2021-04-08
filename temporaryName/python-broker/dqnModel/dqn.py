from collections import deque
from time import gmtime, strftime
from keras.models import Sequential
from keras.layers import Input, Dense 
from keras.callbacks import TensorBoard
from keras.optimizers import Adam
import json

#TODO: one replay per game should be the best option for consistent strategies
REPLAY_BATCH = 100000

class DeepQNetwork:


    #TODO: Read from json model properties and weights by id. ( __ini__(name): Loads from json)
    def __ini__(self, name, n_inputs, hidden_layers, n_actions):
        
        self.n_inputs = n_inputs
        self.hidden_layers = hidden_layers
        self.n_actions = n_actions

        self.main_model = create_dqn()

        self.predict_model()
        self.predict_model.set_weights(self.main_model.get_weights)

        self.tensorboard  = ModifiedTensorBoard(log_dir="logs/"+ name + "-" + strftime("%b%d_%H_%M_%S", gmtime()))

        self.replay_memory = deque()

    def create_dqn(self):
        dqn_model = Sequential()
        
        #Initialize layers
        dqn_model.add(Input(shape=(self.n_inputs,))
        for nodes_in_layer in self.hidden_layers:
            dqn_model.add(Dense(nodes_in_layer, activation="relu"))
        dqn_model.add(Dense(self.n_actions), activation="linear")

        dqn_model.compile(loss="mse", optimizer=Adam(Lr=0.001), metrics=['accuracy'])

        return dqn_model


    def add_replay_memory(self, transition):
        self.replay_memory.append(transition)
    
    def get_qs(): #TODO: Implement get_qs, predict, process_effect
    def predict():
    def process_effect():

    def save_weights():
        weights = []
        for layer in dqn_model.layers:
            weights.append(self.dqn_model.get_weights())
        
        with open('weights.json', 'w') as outfile:
            json.dump(data, outfile)



#TODO: Edit Modified Tensor Board

# Own Tensorboard class
class ModifiedTensorBoard(TensorBoard):

    # Overriding init to set initial step and writer (we want one log file for all .fit() calls)
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.step = 1
        self.writer = tf.summary.FileWriter(self.log_dir)

    # Overriding this method to stop creating default log writer
    def set_model(self, model):
        pass

    # Overrided, saves logs with our step number
    # (otherwise every .fit() will start writing from 0th step)
    def on_epoch_end(self, epoch, logs=None):
        self.update_stats(**logs)

    # Overrided
    # We train for one batch only, no need to save anything at epoch end
    def on_batch_end(self, batch, logs=None):
        pass

    # Overrided, so won't close writer
    def on_train_end(self, _):
        pass

    # Custom method for saving own metrics
    # Creates writer, writes custom metrics and closes writer
    def update_stats(self, **stats):
        self._write_logs(stats, self.step)