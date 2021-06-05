from sklearn.preprocessing import StandardScaler

with open("WholesalePred/data.csv", "r") as f:
    arr = []
    i = 0
    for line in f.readlines():
        if i == 0:
            i += 1
            first_line = line
            continue
        
        new_arr = [el for el in line.split(",")]
        
        arr.append(new_arr)

    scaler = StandardScaler()

    scaler.fit_transform(arr)

    with open("WholesalePred/data2.csv", "w") as f2:
        f2.write(first_line)
        for el in arr:
            for i, l in enumerate(el):
                f2.write(str(l))
                if i != len(el) - 1:
                    f2.write(",")
            f2.write('\n')