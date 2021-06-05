import pandas as pd
import numpy as npy
from sklearn.preprocessing import StandardScaler
from sklearn.neural_network import MLPClassifier
from sklearn import metrics
from matplotlib import pyplot as plt

class NeuralNetworkClassificationClass:
    def __init__(self):
        self.model = MLPClassifier(hidden_layer_sizes=(10, 10, 10), max_iter=1000)

    def train(self, data_X, data_Y):
        self.model.fit(data_X, data_Y)
 
    def predict(self, data):
        # TODO test with and without the following line
        # data = StandardScaler().fit(data)
        return self.model.predict(data)

    def train_csv(self, file_path):
        dataset = pd.read_csv(file_path)

        X = dataset.iloc[:, 0:102].values
        y = dataset.iloc[:, 102].values

        scaler = StandardScaler()
        scaler.fit(X)

        X_train = scaler.transform(X)

        self.train(X_train, y)

    def get_total_error(self, real_value, prediction_value, f1_score, accuracy, notImportant):
        print('Neural Network Classification:')

        # Evaluating the Algorithm
        print(metrics.confusion_matrix(real_value,prediction_value))
        # print(classification_report(real_value,prediction_value))
        print('f1_score:', metrics.f1_score(real_value, prediction_value, labels=npy.unique(real_value)))
        print('accuracy:', metrics.accuracy_score(real_value, prediction_value))

        t = list(range(len(f1_score)))
        if len(t) == 100 or len(t) == 150 or len(t) == 200:
            plt.plot(t, f1_score, 'r--', t, accuracy, 'bs')
            plt.xlabel("Time slot")
            plt.ylabel("Score")
            plt.title("Neural Network Classification Error")
            if len(t) == 100:
                plt.savefig("WholesalePred/plots/Classification/NeuralNetworkClassification_Error_100Timeslots.png")  
            if len(t) == 150:
                plt.savefig("WholesalePred/plots/Classification/NeuralNetworkClassification_Error_150Timeslots.png")  
            if len(t) == 200:
                plt.savefig("WholesalePred/plots/Classification/NeuralNetworkClassification_Error_200Timeslots.png")  


    def get_error(self, real_value, prediction_value):
        print('\nNeural Network Classification:')
        print('Predicted value: ', prediction_value[0], '   Real value: ', real_value[0])
        
        # Evaluating the Algorithm
        # print("\nConfusion matrix")
        # print(metrics.confusion_matrix(real_value,prediction_value))
        # print(metrics.classification_report(real_value,prediction_value))

        f1_score = metrics.f1_score(real_value, prediction_value, labels=npy.unique(real_value))
        print('f1_score:', f1_score)

        accuracy_score = metrics.accuracy_score(real_value, prediction_value)
        print('accuracy:', accuracy_score)

        return f1_score, accuracy_score, 0