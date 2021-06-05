import matplotlib.pyplot as plt
import numpy as np



full_arr = []
with open('WholesalePred/data.csv', 'r') as f:
    i = 0
    lines = f.readlines()[1:]
    for line in lines:
        full_arr += line.split(',')

full_arr = sorted(map(lambda x: int(float(x)), full_arr))

dic = {}
for el in full_arr:
    if el in dic:
        dic[el] += 1
    else:
        dic[el] = 1
         
x , y = [], []

print(dic)

for k, v in dic.items():
    x.append(k)
    y.append(v)

print(len(x))
print(len(y))

plt.plot(x, y)


# plt.xlabel('Smarts')
# plt.ylabel('Probability')
# plt.title('Histogram of IQ')
plt.show()

