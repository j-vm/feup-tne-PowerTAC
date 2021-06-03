import pandas as pd
import numpy as npy
from sklearn.preprocessing import StandardScaler
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import classification_report, confusion_matrix, accuracy_score, f1_score

class NeuralNetworkClassificationClass:
    def __init__(self):
        self.model = MLPClassifier(hidden_layer_sizes=(10, 10, 10), max_iter=1000)

    def train(self, data_X, data_Y):
        self.model.fit(data_X, data_Y)
 
    def predict(self, data):
        return self.model.predict(data)

    def train_csv(self, file_path):
        dataset = pd.read_csv(file_path)

        X = dataset.iloc[:, 0:102].values
        y = dataset.iloc[:, 102].values

        scaler = StandardScaler()
        scaler.fit(X)

        X_train = scaler.transform(X)

        self.train(X_train, y)

    @staticmethod
    def get_error(real_value, prediction_value):
        print('\nNeural Network Classification:')
        print('Predicted value: ', prediction_value, '   Real value: ', real_value)
        # Evaluating the Algorithm
        # print(confusion_matrix(real_value,prediction_value))
        # print(classification_report(real_value,prediction_value))
        print('f1_score:', f1_score(real_value, prediction_value, labels=npy.unique(real_value)))
        print('accuracy:', accuracy_score(real_value, prediction_value))