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


dataset = CSVDataset()

train, test = torch.utils.data.random_split(dataset, dataset.get_line_list(80, 20))

trainset = torch.utils.data.DataLoader(train, batch_size=32, shuffle=True)
testset = torch.utils.data.DataLoader(test, batch_size=1, shuffle=False)

class Net(nn.Module):
    def __init__(self):
        super().__init__()
        self.fc1 = nn.Linear(102, 8)
        self.fc2 = nn.Linear(8, 16)
        self.fc3 = nn.Linear(16, 1)

    def forward(self, x):
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        return x


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

        print(f"Pred {output}, Actual {actual}")

        predictions.append(output)
        actuals.append(actual)
        

    predictions, actuals = vstack(predictions), vstack(actuals)
    # calculate accuracy
    mse = mean_squared_error(actuals, predictions)
    mae = mean_absolute_error(actuals, predictions)


print("Mean Squared Error:", mse)
print("Mean Absolute Error:", mae)