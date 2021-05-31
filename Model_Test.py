import pandas as pd
from sklearn.model_selection import train_test_split
import os
from WholesalePred.Model import Model
from sklearn.preprocessing import StandardScaler
from WholesalePred.Algorithms.RandomForest import RandomForestClass
from WholesalePred.Algorithms.LinearRegression import LinearRegressionClass

def getDataset():
    # get relative path
    dir = os.path.dirname(__file__)
    dataset = pd.read_csv(os.path.join(dir,'WholesalePred/data.csv'))
    dataset.head()
    return dataset

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



    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

    model.single_sample_train(X_train, y_train)
    # model.single_sample_train([[9.00,3571,1976,0.5250]], [[541]])

    y_pred = model.single_sample_predict(X_test)
    model.get_error(y_test, y_pred)

def RandomForest():
    model = Model("RandomForest", RandomForestClass())
    dataset = getDataset()

    # Preparing Data For Training - geting the right columns
    X = dataset.iloc[:, 0:102].values # not inclusivé [0,102[
    y = dataset.iloc[:, 102].values

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=0)

    # Feature Scaling
    sc = StandardScaler()
    X_train = sc.fit_transform(X_train)
    X_test = sc.transform(X_test)

    model.single_sample_train(X_train, y_train)
    # model.single_sample_train([[9.00,3571,1976,0.5250]], [541])

    y_pred = model.single_sample_predict(X_test)
    
    model.get_error(y_test,y_pred)

LinearRegression()
RandomForest()