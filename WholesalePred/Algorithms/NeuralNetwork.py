import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from datetime import datetime
from pandas import read_csv
from numpy import vstack
from sklearn.metrics import mean_squared_error, mean_absolute_error
from matplotlib import pyplot as plt
import matplotlib.patches as mpatches
import matplotlib.lines as mlines
import numpy as np
  
    
class CSVDataset(torch.utils.data.Dataset):
    def __init__(self, path="WholesalePred/data2.csv"):
        self.X = read_csv(path).iloc[:, 0:102].values
        self.y = read_csv(path).iloc[:, 102].values
    
    def get_line_list(self, train_size, test_size):
        if train_size + test_size != 25121:
            raise("Error in get_line_list;\nSum of the two arguments should be 100.")

        var = int(len(self.y) * train_size / 25121)
        return [var, len(self.y) - var]

    def __len__(self):
        return len(self.X)
 
    def __getitem__(self, idx):
        return [self.X[idx], self.y[idx]]

class Net(nn.Module):
    def __init__(self):
        super().__init__()
        self.fc1 = nn.Linear(102, 8)
        self.fc2 = nn.Linear(8, 16)
        self.fc3 = nn.Linear(16, 1)

    def forward(self, x):
        # For each layer that is not the output layer, we pass self.current_layer to an activation function
        # In this case, F.relu(self.current_layer(x))
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        return x



class NeuralNetworkClass:
    def __init__(self):
        # creation of the network
        self.net = Net()
        self.optimizer = torch.optim.SGD(self.net.parameters(), lr=0.01, momentum=0.9)
        self.criterion = torch.nn.MSELoss()

    def train(self, data_X, data_Y):
        self.optimizer.zero_grad() 
        output = self.net(torch.tensor(data_X)) 
        loss = self.criterion(output, torch.tensor(data_Y).view(-1, 1))
        loss.backward()
        self.optimizer.step()

    def predict(self, data):
        with torch.no_grad():
            output = self.net(torch.tensor(data).float())
            output = output.detach().numpy()
            # output = output.round()
            return output

    def train_csv(self, file_path):
        dataset = CSVDataset(file_path)
        trainset = torch.utils.data.DataLoader(dataset, batch_size=64, shuffle=True)

        EPOCHS = 10
        for _ in range(EPOCHS):
            for data in trainset:
                X, y = data
                X = X.float()
                # y = y.float()

                self.optimizer.zero_grad() 

                output = self.net(X) 
                loss = self.criterion(output, y.view(-1,1))
                # loss.backward()

                self.optimizer.step()

    def get_total_error(self, real_value, prediction_value, meanAbsoluteError, meanSquaredError, rootMeanSquaredError):
        print('Neural Network error:')
        # Evaluating the Algorithm

        print('Mean Absolute Error:', mean_absolute_error(real_value, prediction_value))
        print('Mean Squared Error:', mean_squared_error(real_value, prediction_value))
        print('Root Mean Squared Error:', np.sqrt(mean_squared_error(real_value, prediction_value)))

        timeSlot = len(meanAbsoluteError)
        t = list(range(timeSlot))
        
        if timeSlot == 103 or timeSlot == 153 or timeSlot == 203 or timeSlot == 253:
            plt.plot(t, meanAbsoluteError, 'r--', t, rootMeanSquaredError, 'b^')
            # plt.plot(t, meanAbsoluteError, 'r--', t, meanSquaredError, 'bs', t, rootMeanSquaredError, 'g^')

            red_patch = mpatches.Patch(color='red', label='Mean Absolute Error')
            blue_line = mlines.Line2D([], [], color='blue', marker='^', markersize=15, label='Root Mean Squared Error')
            plt.legend(handles=[red_patch,blue_line])

            plt.xlabel("Time slot")
            plt.ylabel("Errors")
            plt.title("Neural Network_Error")
            plt.savefig("WholesalePred/plots/Regression/NeuralNetworkRegression_Error"+ str(timeSlot) + "timeslot_with_legend.png")  


    def get_error(self, real_value, prediction_value):
        print('\nNeural Network Regression:')
        print('Predicted value: ', prediction_value[0][0], '   Real value: ', real_value[0])
        
        # Evaluating the Algorithm

        singleMeanAbsoluteError = mean_absolute_error(real_value, prediction_value)
        print('Mean Absolute Error:', singleMeanAbsoluteError)

        singleMeanSquaredError = mean_squared_error(real_value, prediction_value)
        print('Mean Squared Error:', singleMeanSquaredError)

        singleRootMeanSquaredError = np.sqrt(mean_squared_error(real_value, prediction_value))
        print('Root Mean Squared Error:', singleRootMeanSquaredError)

        return singleMeanAbsoluteError, singleMeanSquaredError, singleRootMeanSquaredError 