
import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn import metrics

class LinearRegression:
    model = LinearRegression()

    @staticmethod
    def train(data_X, data_Y):
        LinearRegression.model.fit(data_X, data_Y)

    @staticmethod
    def predict(data):
        prediction = LinearRegression.model.predict(data)
        return prediction

    @staticmethod
    def get_error(real_value, prediction_value):
        # Evaluating the Algorithm
        print('Mean Absolute Error:', metrics.mean_absolute_error(real_value, prediction_value))
        print('Mean Squared Error:', metrics.mean_squared_error(real_value, prediction_value))
        print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(real_value, prediction_value)))
        