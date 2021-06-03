import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from datetime import datetime
from pandas import read_csv
from numpy import vstack
from sklearn.metrics import mean_squared_error, mean_absolute_error

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


dataset = CSVDataset()

train, test = torch.utils.data.random_split(dataset,[6000, 1495])

trainset = torch.utils.data.DataLoader(train, batch_size=64, shuffle=True)
testset = torch.utils.data.DataLoader(test, batch_size=1024, shuffle=False)

class Net(nn.Module):
    def __init__(self):
        super().__init__()
        self.fc1 = nn.Linear(102, 32)
        self.fc2 = nn.Linear(32, 16)
        self.fc3 = nn.Linear(16, 8)
        self.fc4 = nn.Linear(8, 1)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        x = self.fc4(x)
        return F.relu(x) 


net = Net()

optimizer = torch.optim.SGD(net.parameters(), lr=0.01, momentum=0.9)
criterion = torch.nn.L1Loss()

EPOCHS = 5
for epoch in range(EPOCHS):
    for data in trainset:
        X, y = data
        X = X.float()
        y = y.long()

        optimizer.zero_grad() 

        output = net(X) 
        loss = criterion(output, y.view(-1, 1))

        optimizer.step()

predictions, actuals = list(), list()
with torch.no_grad():
    for data in testset:
        X, y = data
        X = X.float()
        y = y.float()

        output = net(X)
        output = output.detach().numpy()
        
        actual = y.numpy()
        actual = actual.reshape((len(actual), 1))

        output = output.round()

        predictions.append(output)
        actuals.append(actual)

    predictions, actuals = vstack(predictions), vstack(actuals)
    # calculate accuracy
    mse = mean_squared_error(actuals, predictions)
    mae = mean_absolute_error(actuals, predictions)
print("Mean Squared Error:", mse)
print("Mean Absolute Error:", mae)