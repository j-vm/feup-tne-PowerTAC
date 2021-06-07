import pandas as pd
import numpy as npy
from sklearn.preprocessing import StandardScaler
from sklearn.neural_network import MLPClassifier
from sklearn import metrics
from matplotlib import pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.lines as mlines


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

        
        timeSlot = len(f1_score)
        t = list(range(timeSlot))
        
        if timeSlot == 100 or timeSlot == 150 or timeSlot == 200 or timeSlot == 250:
            plt.plot(t, f1_score, 'r--', t, accuracy, 'b^')

            red_patch = mpatches.Patch(color='red', label='F1 score')
            blue_line = mlines.Line2D([], [], color='blue', marker='^', markersize=15, label='Accuracy')
            plt.legend(handles=[red_patch,blue_line])

            plt.xlabel("Time slot")
            plt.ylabel("Score")
            plt.title("Neural Network Classification Error")
            plt.savefig("WholesalePred/plots/Classification/NeuralNetworkClassification_Error_" + str(timeSlot) + "Timeslots_with_legend.png")  



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