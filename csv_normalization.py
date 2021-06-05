from sklearn.preprocessing import StandardScaler
import numpy as np

arr_to_normalize = []
arr_not = []

with open("WholesalePred/data.csv", "r") as f:
    i = 0
    for line in f.readlines():
        if i == 0:
            i += 1
            first_line = line
            continue
        
        new_arr = [el for el in line.split(",")]
        
        arr_to_normalize.append(new_arr[:len(new_arr) - 1])
        arr_not.append(new_arr[len(new_arr) - 1:])

    scaler = StandardScaler()

    arr_to_normalize = scaler.fit_transform(arr_to_normalize)
    
    for i in range(len(arr_to_normalize)):
        for j in range(len(arr_to_normalize[i])):
            arr_to_normalize[i][j] = str(float(arr_to_normalize[i][j]) * 1000)


    arr = []

    for i in range(len(arr_to_normalize)):
        arr.append(np.concatenate((arr_to_normalize[i], np.array(arr_not[i]))))

    with open("WholesalePred/data2.csv", "w") as f2:
        f2.write(first_line)
        for el in arr:
            for i, l in enumerate(el):
                f2.write(str(l))
                if i != len(el) - 1:
                    f2.write(",")