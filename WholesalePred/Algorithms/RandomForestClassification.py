import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn import metrics
import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import classification_report, confusion_matrix, accuracy_score

class RandomForestClassificationClass:
    def __init__(self):
        self.model = RandomForestRegressor(n_estimators=400, random_state=0)

    def train(self, data_X, data_Y):
        self.model.fit(data_X, data_Y)
 
    def predict(self, data):
        return self.model.predict(data)

    def train_csv(self, file_path):
        dataset = pd.read_csv(file_path)

        X = dataset.iloc[:, 0:102].values
        y = dataset.iloc[:, 102].values

        # sc = StandardScaler()
        # X = sc.fit_transform(X)

        self.train(X, y)

    @staticmethod
    def get_error(real_value, prediction_value):
        print('\nRandom Forest Classification:')
        print('Predicted value: ', prediction_value)
        # Evaluating the Algorithm
        print(confusion_matrix(real_value,prediction_value))
        print(classification_report(real_value,prediction_value))
        print('accuracy:', accuracy_score(real_value, prediction_value))