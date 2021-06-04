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
    def __init__(self, path="WholesalePred/data.csv"):
        self.X = read_csv(path).iloc[:, 0:102].values
        self.y = read_csv(path).iloc[:, 102].values
    
    def get_line_list(self, train_size, test_size):
        if train_size + test_size != 100:
            raise("Error in get_line_list;\nSum of the two arguments should be 100.")

        var = int(len(self.y) * train_size / 100)
        return [var, len(self.y) - var]

    def __len__(self):
        return len(self.X)
 
    def __getitem__(self, idx):
        return [self.X[idx], self.y[idx]]

class Net(nn.Module):
    def __init__(self):
        super().__init__()
        self.fc1 = nn.Linear(102, 64)
        self.fc2 = nn.Linear(64, 64)
        self.fc3 = nn.Linear(64, 1)

    def forward(self, x):
        # For each layer that is not the output layer, we pass self.current_layer to an activation function
        # In this case, F.relu(self.current_layer(x))
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = self.fc3(x)
        # The last layer also contains an activation function, but is usually different.
        return F.relu(x) 



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
            output = output.round()
            return output

    def train_csv(self, file_path):
        dataset = CSVDataset(file_path)
        # train, test = torch.utils.data.random_split(dataset, dataset.get_line_list(80, 10))
        trainset = torch.utils.data.DataLoader(dataset, batch_size=64, shuffle=True)
        # testset = torch.utils.data.DataLoader(test, batch_size=1024, shuffle=False)

        EPOCHS = 10
        for _ in range(EPOCHS):
            for data in trainset:
                X, y = data
                X = X.float()
                y = y.float()

                self.optimizer.zero_grad() 

                output = self.net(X) 
                loss = self.criterion(output, y.view(-1,1))
                loss.backward()

                self.optimizer.step()

    @staticmethod
    def get_error(real_value, prediction_value):
        print('\nNeural Network error:')
        if(len(real_value) == 1):
            print('Predicted value: ', prediction_value, '   Real value: ', real_value)
        mse = mean_squared_error(real_value, prediction_value)
        print("Mean Squared Error:", mse)