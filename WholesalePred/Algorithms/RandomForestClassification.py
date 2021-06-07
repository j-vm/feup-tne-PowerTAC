import numpy as npy
from sklearn.ensemble import RandomForestRegressor
from sklearn import metrics
import pandas as pd
from sklearn.preprocessing import StandardScaler
from matplotlib import pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.lines as mlines


class RandomForestClassificationClass:
    def __init__(self):
        self.model = RandomForestRegressor(n_estimators=400, random_state=0)

    def train(self, data_X, data_Y):
        self.model.fit(data_X, data_Y)
 
    def predict(self, data):
        predicts = self.model.predict(data)
        result = []
        for predict in predicts:
            result.append(round(predict))
        return result

    def train_csv(self, file_path):
        dataset = pd.read_csv(file_path)

        X = dataset.iloc[:, 0:102].values
        y = dataset.iloc[:, 102].values

        # sc = StandardScaler()
        # X = sc.fit_transform(X)

        self.train(X, y)

    def get_total_error(self, real_value, prediction_value, f1_score, accuracy, notImportant):
        print('Random Forest Classification:')
        # Evaluating the Algorithm

        print(metrics.confusion_matrix(real_value,prediction_value))
        # print(classification_report(real_value,prediction_value))
        print('f1_score:', metrics.f1_score(real_value, prediction_value, labels=npy.unique(real_value)))
        print('accuracy:', metrics.accuracy_score(real_value, prediction_value))

        timeSlot = len(f1_score)
        t = list(range(timeSlot))
        
        if timeSlot == 99 or timeSlot == 149 or timeSlot == 199 or timeSlot == 249:
            plt.plot(t, f1_score, 'r--', t, accuracy, 'b^')

            red_patch = mpatches.Patch(color='red', label='F1 score')
            blue_line = mlines.Line2D([], [], color='blue', marker='^', markersize=15, label='Accuracy')
            plt.legend(handles=[red_patch,blue_line])

            plt.xlabel("Time slot")
            plt.ylabel("Score")
            plt.title("Random Forest Classification Error")
            plt.savefig("WholesalePred/plots/Classification/RandomForestClassification_Error_" + str(timeSlot) +"Timeslots_with_legend.png")  



    def get_error(self, real_value, prediction_value):
        print('\nRandom Forest Classification:')
        print('Predicted value: ', prediction_value[0], '   Real value: ', real_value[0])

        # Evaluating the Algorithm
        #print("\nConfusion matrix")
        #print(metrics.confusion_matrix(real_value,prediction_value))
        # print(metrics.classification_report(real_value,prediction_value))

        f1_score = metrics.f1_score(real_value, prediction_value, labels=npy.unique(real_value))
        print('f1_score:', f1_score)

        accuracy_score = metrics.accuracy_score(real_value, prediction_value)
        print('accuracy:', accuracy_score)

        return f1_score, accuracy_score, 0
        