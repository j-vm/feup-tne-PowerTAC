import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
# %matplotlib inline
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression
from sklearn import metrics
import os

# get relative path
dir = os.path.dirname(__file__)
filename = os.path.join(dir,'mock.csv')


dataset = pd.read_csv(os.path.join(dir,'mock.csv'))
dataset1 = pd.read_csv(os.path.join(dir,'mock.csv'))

dataset.head()
dataset1.head()

# see statistical details of the dataset
dataset.describe()
dataset1.describe()

# Preparing the Data
X = dataset[['clearedTrade', 'orderbook', 'weatherForecast', 'weather']]
y = dataset['clearing_price']

X1 = dataset1[['clearedTrade', 'orderbook', 'weatherForecast', 'weather']]
y1 = dataset1['clearing_price']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)
X1_train, X1_test, y1_train, y1_test = train_test_split(X1, y1, test_size=0.4, random_state=0)

# Training the Algorithm
regressor = LinearRegression()
regressor.fit(X_train, y_train)
regressor.fit(X1_train, y1_train)

coeff_df = pd.DataFrame(regressor.coef_, X.columns, columns=['Coefficient'])
coeff_df

# Making Predictions
y_pred = regressor.predict(X_test)

df = pd.DataFrame({'Actual': y_test, 'Predicted': y_pred})
df

# Evaluating the Algorithm
print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))
print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))
print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(y_test, y_pred)))