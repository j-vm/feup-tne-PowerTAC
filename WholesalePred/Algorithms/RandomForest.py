import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.ensemble import RandomForestRegressor
from sklearn import metrics
import os

# get relative path
dir = os.path.dirname(__file__)
filename = os.path.join(dir,'mock.csv')

dataset = pd.read_csv(os.path.join(dir,'mock.csv'))
dataset1 = pd.read_csv(os.path.join(dir,'mock.csv'))

dataset.head()
dataset1.head()

# Preparing Data For Training - geting the right columns
X = dataset.iloc[:, 0:4].values # not inclusivé [0,4[
y = dataset.iloc[:, 4].values

X1 = dataset1.iloc[:, 0:4].values # not inclusivé [0,4[
y1 = dataset1.iloc[:, 4].values

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)
X1_train, X1_test, y1_train, y1_test = train_test_split(X1, y1, test_size=0.4, random_state=0)

# Feature Scaling
sc = StandardScaler()
X_train = sc.fit_transform(X_train)
X_test = sc.transform(X_test)

X1_train = sc.fit_transform(X1_train)
X1_test = sc.transform(X1_test)

# Training the Algorithm
regressor = RandomForestRegressor(n_estimators=400, random_state=0)
regressor.fit(X_train, y_train)
regressor.fit(X1_train, y1_train)

y_pred = regressor.predict(X_test)

# Evaluating the Algorithm
print('Mean Absolute Error:', metrics.mean_absolute_error(y_test, y_pred))
print('Mean Squared Error:', metrics.mean_squared_error(y_test, y_pred))
print('Root Mean Squared Error:', np.sqrt(metrics.mean_squared_error(y_test, y_pred)))