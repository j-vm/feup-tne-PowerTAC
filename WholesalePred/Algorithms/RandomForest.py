import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn import metrics

class RandomForest:
    model = RandomForestRegressor(n_estimators=400, random_state=0)

    @staticmethod
    def train(data_X, data_Y):
        RandomForest.model.fit(data_X, data_Y)
 
    @staticmethod
    def predict(data):
        prediction = RandomForest.model.predict(data)
        return prediction

    @staticmethod
    def get_error(real_value, prediction_value):
        # Evaluating the Algorithm
        print('Mean Absolute Error:', metrics.mean_absolute_error(real_value, prediction_value))
        print('Mean Squared Error:', metrics.mean_squared_error(real_value, prediction_value))
        print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(real_value, prediction_value)))