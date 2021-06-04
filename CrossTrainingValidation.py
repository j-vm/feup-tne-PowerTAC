import pandas as pd
from sklearn.model_selection import train_test_split
import os
from WholesalePred.Model import Model
from sklearn.preprocessing import StandardScaler
from WholesalePred.Algorithms.RandomForestRegression import RandomForestRegressionClass
from WholesalePred.Algorithms.RandomForestClassification import RandomForestClassificationClass
from WholesalePred.Algorithms.LinearRegression import LinearRegressionClass
from WholesalePred.Algorithms.NeuralNetwork import NeuralNetworkClass
from WholesalePred.Algorithms.NeuralNetworkClassification import NeuralNetworkClassificationClass
from sklearn import preprocessing
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import classification_report, confusion_matrix
from sklearn import svm
from sklearn.model_selection import KFold # import KFold
from sklearn.model_selection import LeaveOneOut 
import numpy as np
from sklearn.model_selection import cross_val_score, cross_val_predict
from sklearn import metrics

def getDataset():
    # get relative path
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'WholesalePred/data.csv'))
    dataset.head()
    return dataset

def KFoldsCrossValidation(X, y):
    kf = KFold(n_splits=2) # Define the split - into 2 folds 
    kf.get_n_splits(X) # returns the number of splitting iterations in the cross-validator
    KFold(n_splits=2, random_state=None, shuffle=False)

    for train_index, test_index in kf.split(X):
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]

        return X_train, X_test, y_train, y_test


def LinearRegression():
    model = Model("LinearRegression", LinearRegressionClass())
    dataset = getDataset()
    
    # Preparing the Data
    X = dataset[['number_competitors','number_customers','temperature','cloud_cover','wind_direction','wind_speed',
                'temperature1','cloudCover1','windDirection1','windSpeed1','temperature2','cloudCover2','windDirection2',
                'windSpeed2','temperature3','cloudCover3','windDirection3','windSpeed3','temperature4','cloudCover4','windDirection4',
                'windSpeed4','temperature5','cloudCover5','windDirection5','windSpeed5','temperature6','cloudCover6','windDirection6',
                'windSpeed6','temperature7','cloudCover7','windDirection7','windSpeed7','temperature8','cloudCover8','windDirection8',
                'windSpeed8','temperature9','cloudCover9','windDirection9','windSpeed9','temperature10','cloudCover10','windDirection10',
                'windSpeed10','temperature11','cloudCover11','windDirection11','windSpeed11','temperature12','cloudCover12','windDirection12',
                'windSpeed12','temperature13','cloudCover13','windDirection13','windSpeed13','temperature14','cloudCover14','windDirection14',
                'windSpeed14','temperature15','cloudCover15','windDirection15','windSpeed15','temperature16','cloudCover16','windDirection16',
                'windSpeed16','temperature17','cloudCover17','windDirection17','windSpeed17','temperature18','cloudCover18','windDirection18',
                'windSpeed18','temperature19','cloudCover19','windDirection19','windSpeed19','temperature20','cloudCover20','windDirection20',
                'windSpeed20','temperature21','cloudCover21','windDirection21','windSpeed21','temperature22','cloudCover22','windDirection22',
                'windSpeed22','temperature23','cloudCover23','windDirection23','windSpeed23','temperature0','cloudCover0','windDirection0',
                'windSpeed0']]
    y = dataset['clearingPrice']

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling - better results without
    # sc = StandardScaler()
    # X_train = sc.fit_transform(X_train)
    # X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)

    y_pred = model.sample_predict(X_test)
    model.get_error(y_test, y_pred)
    
def LinearRegressionKFolds():
    model = Model("LinearRegression", LinearRegressionClass())
    dataset = getDataset()
    
    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nK-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)

    # Feature Scaling - better results without
    # sc = StandardScaler()
    # X_train = sc.fit_transform(X_train)
    # X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)

    y_pred = model.sample_predict(X_test)
    model.get_error(y_test, y_pred)


def RandomForestRegression():
    model = Model("RandomForestRegression", RandomForestRegressionClass())
    dataset = getDataset()

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling - better results with
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)

    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)

def RandomForestRegressionKFolds():
    model = Model("RandomForestRegression", RandomForestRegressionClass())
    dataset = getDataset()

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nK-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)
 
    # Feature Scaling - better results with
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)



def NeuralNetwork():
    model = Model("NeuralNetwork", NeuralNetworkClass())
    dataset = getDataset()

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)   

def NeuralNetworkKFolds():
    model = Model("NeuralNetwork", NeuralNetworkClass())
    dataset = getDataset()

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nK-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)  

def RandomForestClassification():
    model = Model("RandomForestClassification", RandomForestClassificationClass())
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'WholesalePred/dataClassification.csv'))

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)

def RandomForestClassificationKFolds():
    model = Model("RandomForestClassification", RandomForestClassificationClass())
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'WholesalePred/dataClassification.csv'))

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nK-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)

def NeuralNetworkClassification():
    model = Model("NeuralNetworkClassification", NeuralNetworkClassificationClass())
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'WholesalePred/dataClassification.csv'))

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)    

def NeuralNetworkClassificationKFolds():
    model = Model("NeuralNetworkClassification", NeuralNetworkClassificationClass())
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'WholesalePred/dataClassification.csv'))

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    print('\nK-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)    


print('\nRegression:')

LinearRegression()
LinearRegressionKFolds()

RandomForestRegression()
RandomForestRegressionKFolds()

NeuralNetwork()
NeuralNetworkKFolds()

print('\nClassification:')

RandomForestClassification() 
RandomForestClassificationKFolds() 

NeuralNetworkClassification()
NeuralNetworkClassificationKFolds()
