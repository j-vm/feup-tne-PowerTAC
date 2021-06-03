import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from datetime import datetime
from pandas import read_csv
from numpy import vstack
from sklearn.metrics import mean_squared_error

from torchvision import transforms, datasets
  
    
class CSVDataset(torch.utils.data.Dataset):
    # load the dataset
    def __init__(self, path="WholesalePred/data.csv"):
        # store the inputs and outputs
        self.X = read_csv(path).iloc[:, 0:102].values
        self.y = read_csv(path).iloc[:, 102].values
 
    # number of rows in the dataset
    def __len__(self):
        return len(self.X)
 
    # get a row at an index
    def __getitem__(self, idx):
        return [self.X[idx], self.y[idx]]

class Net(nn.Module):
    def __init__(self):
        super().__init__()
        self.fc1 = nn.Linear(102, 64)
        self.fc2 = nn.Linear(64, 64)
        self.fc3 = nn.Linear(64, 64)
        self.fc4 = nn.Linear(64, 1)

    def forward(self, x):
        # For each layer that is not the output layer, we pass self.current_layer to an activation function
        # In this case, F.relu(self.current_layer(x))
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        x = self.fc4(x)
        # The last layer also contains an activation function, but is usually different.
        return F.log_softmax(x, dim=1) 



class NeuralNetworkClass:
    def __init__(self):
        # creation of the network
        self.net = Net()
        self.optimizer = torch.optim.SGD(self.net.parameters(), lr=0.01, momentum=0.9)
        self.criterion = torch.nn.MSELoss()

    def train(self, data_X, data_Y):
        self.optimizer.zero_grad() 
        output = self.net(data_X) 
        loss = self.criterion(output, data_Y)
        loss.backward()
        self.optimizer.step()

    def train_csv(self, file_path):
        dataset = CSVDataset(file_path)
        train, test = torch.utils.data.random_split(dataset,[7000, 286])
        trainset = torch.utils.data.DataLoader(train, batch_size=64, shuffle=True)
        testset = torch.utils.data.DataLoader(test, batch_size=1024, shuffle=False)


        EPOCHS = 10
        for epoch in range(EPOCHS):
            for data in trainset:
                X, y = data
                X = X.float()
                y = y.float()

                self.optimizer.zero_grad() 

                output = self.net(X) 
                loss = self.criterion(output, y)
                loss.backward()

                self.optimizer.step()


        predictions, actuals = list(), list()

        with torch.no_grad():
            for data in trainset:
                X, y = data
                X = X.float()
                y = y.float()

                output = self.net(X)
                output = output.detach().numpy()
                
                actual = y.numpy()
                actual = actual.reshape((len(actual), 1))

                output = output.round()

                predictions.append(output)
                actuals.append(actual)

            predictions, actuals = vstack(predictions), vstack(actuals)
        # calculate accuracy
        NeuralNetworkClass.get_error(actuals, predictions)

        predictions, actuals = list(), list()

        with torch.no_grad():
            for data in testset:
                X, y = data
                X = X.float()
                y = y.float()

                output = self.net(X)
                output = output.detach().numpy()
                
                actual = y.numpy()
                actual = actual.reshape((len(actual), 1))

                output = output.round()

                predictions.append(output)
                actuals.append(actual)

            predictions, actuals = vstack(predictions), vstack(actuals)
        # calculate accuracy
        NeuralNetworkClass.get_error(actuals, predictions)


    @staticmethod
    def get_error(real_value, prediction_value):
        print('\nNeural Network error:')
        if(len(real_value) == 1):
            print('Predicted value: ', prediction_value, '   Real value: ', real_value)
        mse = mean_squared_error(real_value, prediction_value)
        print("Mean Squared Error:", mse)