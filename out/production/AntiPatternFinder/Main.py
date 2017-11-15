import json

#Read from json file into a matrix
data = json.load(open('matrices.json'))
keys = list(data)

for key in keys:
    for i in range(len(data[key])):
        print(data[key][i])
    print()
