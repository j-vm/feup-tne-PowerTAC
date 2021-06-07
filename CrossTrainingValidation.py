import pandas as pd
from sklearn.model_selection import train_test_split
import os
from WholesalePred.Model import Model
from sklearn.preprocessing import StandardScaler
from WholesalePred.Algorithms.RandomForestRegression import RandomForestRegressionClass
from WholesalePred.Algorithms.RandomForestClassification import RandomForestClassificationClass
from WholesalePred.Algorithms.LinearRegression import LinearRegressionClass
from WholesalePred.Algorithms.NeuralNetwork import CSVDataset, Net
from WholesalePred.Algorithms.NeuralNetworkClassification import NeuralNetworkClassificationClass
from sklearn.model_selection import KFold # import KFold
from matplotlib import pyplot as plt
import torch
from numpy import vstack
from sklearn.metrics import mean_squared_error, mean_absolute_error
import numpy as np


def getDataset():
    # get relative path
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'WholesalePred/data.csv'))
    return dataset

def getXY(filepath):
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,filepath))

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    return X, y

def KFoldsCrossValidation(X, y):
    kf = KFold(n_splits=5) # Define the split - into 2 folds 
    kf.get_n_splits(X) # returns the number of splitting iterations in the cross-validator
    KFold(n_splits=5, random_state=None, shuffle=False)

    for train_index, test_index in kf.split(X):
        X_train, X_test = X[train_index], X[test_index]
        y_train, y_test = y[train_index], y[test_index]

        return X_train, X_test, y_train, y_test

def showPlot(y_test, predictions,name,pltName):
    plt.scatter(y_test, predictions)
    plt.xlabel("True Values")
    plt.ylabel("Predictions")
    plt.title(name)
    # plt.show()
    plt.savefig(pltName)  

def LinearRegression():
    model = Model("LinearRegression", LinearRegressionClass())
    
    X, y = getXY('WholesalePred/data.csv')

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling - better results without
    # sc = StandardScaler()
    # X_train = sc.fit_transform(X_train)
    # X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test, y_pred)

    showPlot(y_test, y_pred,"Linear Regression - Train_test_split", "WholesalePred/plots/Regression/LinearRegression_Train_test_split.png")

def LinearRegressionKFolds():
    model = Model("LinearRegression", LinearRegressionClass())

    X, y = getXY('WholesalePred/data.csv')
    
    print('\n5-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)

    # Feature Scaling - better results without
    # sc = StandardScaler()
    # X_train = sc.fit_transform(X_train)
    # X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)

    y_pred = model.sample_predict(X_test)
    model.get_error(y_test, y_pred)
    showPlot(y_test, y_pred,"Linear Regression - 5-Folds Cross Validation", "WholesalePred/plots/Regression/LinearRegression_KFoldsCrossValidation.png")


def RandomForestRegression():
    model = Model("RandomForestRegression", RandomForestRegressionClass())

    X, y = getXY('WholesalePred/data.csv')

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling - better results with
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)

    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)
    showPlot(y_test, y_pred,"Random Forest Regression - Train_test_split", "WholesalePred/plots/Regression/RandomForestRegression_Train_test_split.png")

def RandomForestRegressionKFolds():
    model = Model("RandomForestRegression", RandomForestRegressionClass())

    X, y = getXY('WholesalePred/data.csv')

    print('\n5-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)
 
    # Feature Scaling - better results with
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)
    showPlot(y_test, y_pred,"Random Forest Regression - 5-Folds Cross Validation", "WholesalePred/plots/Regression/RandomForestRegression_KFoldsCrossValidation.png")

def NeuralNetwork():
    dataset = CSVDataset()

    train, test = torch.utils.data.random_split(dataset, dataset.get_line_list(23097, 2024)) # 80% train, 20% test

    trainset = torch.utils.data.DataLoader(train, batch_size=32, shuffle=True)
    testset = torch.utils.data.DataLoader(test, batch_size=1, shuffle=False)

    net = Net()

    optimizer = torch.optim.SGD(net.parameters(), lr=0.01, momentum=0.9)
    criterion = torch.nn.L1Loss()

    EPOCHS = 10
    for epoch in range(EPOCHS):
        for data in trainset:
            X, y = data
            X = X.float()

            optimizer.zero_grad() 

            output = net(X)
            
            loss = criterion(output, y.view(-1, 1))

            optimizer.step()
            

    predictions, actuals = list(), list()
    with torch.no_grad():
        for data in testset:
            X, y = data
            X = X.float()
            
            output = net(X)
            
            output = output.detach().numpy()

            actual = y.numpy()
            actual = actual.reshape((len(actual), 1))

            # print(f"Pred {output}, Actual {actual}")

            predictions.append(output)
            actuals.append(actual)
            

        predictions, actuals = vstack(predictions), vstack(actuals)
        # calculate accuracy
    
        mae = mean_absolute_error(actuals, predictions)
        mse = mean_squared_error(actuals, predictions)
        rmse = np.sqrt(mean_squared_error(actuals, predictions))


    print('\nTrain_test_split')
    print('\nNeural Network Regression:')
    print("Mean Absolute Error:", mae)
    print("Mean Squared Error:", mse)
    print("Root mean Absolute Error:", rmse)
    print('\n')

def NeuralNetworkKFolds():
    dataset = CSVDataset()

    train, test = torch.utils.data.random_split(dataset, dataset.get_line_list(23097, 2024)) # 80% train, 20% test

    trainset = torch.utils.data.DataLoader(train, batch_size=32, shuffle=True)
    testset = torch.utils.data.DataLoader(test, batch_size=1, shuffle=False)

    net = Net()

    optimizer = torch.optim.SGD(net.parameters(), lr=0.01, momentum=0.9)
    criterion = torch.nn.L1Loss()

    EPOCHS = 10
    for epoch in range(EPOCHS):
        for data in trainset:
            X, y = data
            X = X.float()

            optimizer.zero_grad() 

            output = net(X)
            
            loss = criterion(output, y.view(-1, 1))

            optimizer.step()
            

    predictions, actuals = list(), list()
    with torch.no_grad():
        for data in testset:
            X, y = data
            X = X.float()
            
            output = net(X)
            
            output = output.detach().numpy()

            actual = y.numpy()
            actual = actual.reshape((len(actual), 1))

            # print(f"Pred {output}, Actual {actual}")

            predictions.append(output)
            actuals.append(actual)
            

        predictions, actuals = vstack(predictions), vstack(actuals)
        # calculate accuracy
    
        mae = mean_absolute_error(actuals, predictions)
        mse = mean_squared_error(actuals, predictions)
        rmse = np.sqrt(mean_squared_error(actuals, predictions))


    print('\n5-Folds Cross Validation')
    print('\nNeural Network Regression:')
    print("Mean Absolute Error:", mae)
    print("Mean Squared Error:", mse)
    print("Root mean Absolute Error:", rmse)
    print('\n')

    # showPlot(actuals, predictions,"Neural Network Regression - Train_test_split", "WholesalePred/plots/Regression/NeuralNetworkRegression_Train_test_split.png")

def RandomForestClassification():
    model = Model("RandomForestClassification", RandomForestClassificationClass())
    
    X,y = getXY('WholesalePred/dataClassification.csv')

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)

    showPlot(y_test, y_pred,"Random Forest Classification - Train_test_split", "WholesalePred/plots/Classification/NeuralNetworkClassification_Train_test_split.png")

def RandomForestClassificationKFolds():
    model = Model("RandomForestClassification", RandomForestClassificationClass())
    
    X,y = getXY('WholesalePred/dataClassification.csv')

    print('\n5-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)
    showPlot(y_test, y_pred,"Neural Network Classification - 5-Folds Cross Validation", "WholesalePred/plots/Classification/NeuralNetworkClassification_KFoldsCrossValidation.png")


def NeuralNetworkClassification():
    model = Model("NeuralNetworkClassification", NeuralNetworkClassificationClass())
    
    X,y = getXY('WholesalePred/dataClassification.csv')

    print('\nTrain_test_split')
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size= 0.8, test_size=0.2, random_state=0)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)    

    showPlot(y_test, y_pred,"Neural Network Classification - Train_test_split", "WholesalePred/plots/Classification/NeuralNetworkClassification_Train_test_split.png")

def NeuralNetworkClassificationKFolds():
    model = Model("NeuralNetworkClassification", NeuralNetworkClassificationClass())
    
    X,y = getXY('WholesalePred/dataClassification.csv')

    print('\n5-Folds Cross Validation')
    X_train, X_test, y_train, y_test = KFoldsCrossValidation(X, y)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.sample_train(X_train, y_train)
    
    y_pred = model.sample_predict(X_test)
    model.get_error(y_test,y_pred)    


print('\nRegression:')

# LinearRegression()
LinearRegressionKFolds()

# RandomForestRegression()
RandomForestRegressionKFolds()

# NeuralNetwork()
NeuralNetworkKFolds()

print('\nClassification:')

# RandomForestClassification() 
RandomForestClassificationKFolds() 

# NeuralNetworkClassification()
NeuralNetworkClassificationKFolds()
