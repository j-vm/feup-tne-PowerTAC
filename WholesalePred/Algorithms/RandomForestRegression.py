import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn import metrics
import pandas as pd
from sklearn.preprocessing import StandardScaler
from matplotlib import pyplot as plt

class RandomForestRegressionClass:
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

        self.train(X, y)

    def get_total_error(self, real_value, prediction_value, meanAbsoluteError, meanSquaredError, rootMeanSquaredError):
        print('Random Forest Regression:')
        
        # Evaluating the Algorithm

        print('Mean Absolute Error:', metrics.mean_absolute_error(real_value, prediction_value))
        print('Mean Squared Error:', metrics.mean_squared_error(real_value, prediction_value))
        print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(real_value, prediction_value)))

    
        t = list(range(len(meanAbsoluteError)))

        if len(t) == 100 or len(t) == 150 or len(t) == 200:
            plt.plot(t, meanAbsoluteError, 'r--', t, rootMeanSquaredError, 'bs')
            # plt.plot(t, meanAbsoluteError, 'r--', t, meanSquaredError, 'bs', t, rootMeanSquaredError, 'g^')

            plt.xlabel("Time slot")
            plt.ylabel("Errors")
            plt.title("Random Forest Regression Error")
            if len(t) == 100:
                plt.savefig("WholesalePred/plots/Regression/RandomForestRegression_Error_100Timeslots.png")  
            if len(t) == 150:
                plt.savefig("WholesalePred/plots/Regression/RandomForestRegression_Error_150Timeslots.png")  
            if len(t) == 200:
                plt.savefig("WholesalePred/plots/Regression/RandomForestRegression_Error_200Timeslots.png")  


    def get_error(self, real_value, prediction_value):
        print('\nRandom Forest Regression:')
        print('Predicted value: ', prediction_value[0], '   Real value: ', real_value[0])
        
        # Evaluating the Algorithm

        meanAbsoluteError = metrics.mean_absolute_error(real_value, prediction_value)
        print('Mean Absolute Error:', meanAbsoluteError)

        meanSquaredError = metrics.mean_squared_error(real_value, prediction_value)
        print('Mean Squared Error:', meanSquaredError)

        rootMeanSquaredError = np.sqrt(metrics.mean_squared_error(real_value, prediction_value))
        print('Root Mean Squared Error:', rootMeanSquaredError)

        return meanAbsoluteError, meanSquaredError, rootMeanSquaredError