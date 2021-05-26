import torch
import torch.nn as nn
import torch.nn.functional as F
import torch.optim as optim
from datetime import datetime

from torchvision import transforms, datasets

train = datasets.MNIST("", train=True, download=True, transform=transforms.Compose([transforms.ToTensor()]))
test = datasets.MNIST("", train=False, download=True, transform=transforms.Compose([transforms.ToTensor()]))

trainset = torch.utils.data.DataLoader(train, batch_size=10, shuffle=True)
testset = torch.utils.data.DataLoader(test, batch_size=10, shuffle=True)

class Net(nn.Module):
    def __init__(self):
        super().__init__()
        # 28 * 28 features
        self.fc1 = nn.Linear(28*28, 64)
        self.fc2 = nn.Linear(64, 64)
        self.fc3 = nn.Linear(64, 64)
        self.fc4 = nn.Linear(64, 10)

    def forward(self, x):
        # For each layer that is not the output layer, we pass self.current_layer to an activation function
        # In this case, F.relu(self.current_layer(x))
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = F.relu(self.fc3(x))
        x = self.fc4(x)
        # The last layer also contains an activation function, but is usually different.
        return F.log_softmax(x, dim=1) 

# creation of the network
net = Net()

# Implements Adam algorithm: A Method for Stochastic Optimization. 
optimizer = optim.Adam(net.parameters(), lr = 1e-3)

EPOCHS = 3
d1 = datetime.now()
for epoch in range(EPOCHS):
    for data in trainset:
        X, y = data
        net.zero_grad()  #you want to do this every time before passing data to the network
        output = net(X.view(-1, 28**2))  #-1 in a view means that we do not know the size
        loss = F.nll_loss(output, y) #calculating the loss over the network result
        loss.backward()
        optimizer.step()
d2 = datetime.now()
print(d2 - d1)        
correct = 0
total = 0

d1 = datetime.now()
with torch.no_grad():
    for data in trainset:
        X, y = data
        output = net(X.view(-1, 28*28))
        for idx, i in enumerate(output):
            if torch.argmax(i) == y[idx]:
                correct += 1
            total += 1
print('Accuracy:', round(correct/total, 3))
d2 = datetime.now()
print(d2 - d1)  

d1 = datetime.now()
with torch.no_grad():
    for data in testset:
        X, y = data
        output = net(X.view(-1, 28*28))
        for idx, i in enumerate(output):
            if torch.argmax(i) == y[idx]:
                correct += 1
            total += 1
print('Accuracy:', round(correct/total, 3))
d2 = datetime.now()
print(d2 - d1)