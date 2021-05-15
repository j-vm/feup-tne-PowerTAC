import pandas as pd
from sklearn.model_selection import train_test_split
import os
from WholesalePred.Model import Model
from sklearn.preprocessing import StandardScaler
from WholesalePred.Algorithms.RandomForest import RandomForest as RandomForestModel
from WholesalePred.Algorithms.LinearRegression import LinearRegression as LinearRegressionModel

def getDataset():
    # get relative path
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'mock.csv'))
    dataset.head()
    return dataset

def LinearRegression():
    model = Model("LinearRegression", LinearRegressionModel)
    dataset = getDataset()
    dataset1 = getDataset()
    
    # Preparing the Data
    X = dataset[['clearedTrade', 'orderbook', 'weatherForecast', 'weather']]
    y = dataset['clearing_price']

    X1 = dataset1[['clearedTrade', 'orderbook', 'weatherForecast', 'weather']]
    y1 = dataset1['clearing_price']

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)
    X1_train, X1_test, y1_train, y1_test = train_test_split(X1, y1, test_size=0.4, random_state=0)

    model.single_sample_train(X_train, y_train)
    model.single_sample_train(X1_train, y1_train)

    y_pred = model.single_sample_predict(X_test)
    model.get_error(y_test, y_pred)

def RandomForest():
    model = Model("RandomForest", RandomForestModel)
    dataset = getDataset()
    dataset1 = getDataset()

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

    model.single_sample_train(X_train, y_train)
    model.single_sample_train(X1_train, y1_train)

    y_pred = model.single_sample_predict(X_test)
    
    model.get_error(y_test,y_pred)

LinearRegression()
RandomForest()