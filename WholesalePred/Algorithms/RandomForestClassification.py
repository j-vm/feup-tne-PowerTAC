import numpy as npy
from sklearn.ensemble import RandomForestRegressor
from sklearn import metrics
import pandas as pd
from sklearn.preprocessing import StandardScaler
from matplotlib import pyplot as plt

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

        t = list(range(len(f1_score)))
        if len(t) == 100 or len(t) == 150 or len(t) == 200:
            plt.plot(t, f1_score, 'r--', t, accuracy, 'bs')
            plt.xlabel("Time slot")
            plt.ylabel("Score")
            plt.title("Random Forest Classification Error")
            if len(t) == 100:
                plt.savefig("WholesalePred/plots/Classification/RandomForestClassification_Error_100Timeslots.png")  
            if len(t) == 150:
                plt.savefig("WholesalePred/plots/Classification/RandomForestClassification_Error_150Timeslots.png") 
            if len(t) == 200:
                plt.savefig("WholesalePred/plots/Classification/RandomForestClassification_Error_200Timeslots.png") 


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
        