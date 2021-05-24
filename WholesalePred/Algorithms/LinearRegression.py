
import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn import metrics

class LinearRegressionClass:
    def __init__(self):
        self.model = LinearRegression()

    def train(self, data_X, data_Y):
        self.model.fit(data_X, data_Y)

    def predict(self, data):
        return self.model.predict(data)

    @staticmethod
    def get_error(real_value, prediction_value):
        # Evaluating the Algorithm
        print('Mean Absolute Error:', metrics.mean_absolute_error(real_value, prediction_value))
        print('Mean Squared Error:', metrics.mean_squared_error(real_value, prediction_value))
        print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(real_value, prediction_value)))
        