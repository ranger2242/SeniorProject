import json
import os
import sys

#Move Up one directory
def upAFolder(path):
    while path[-1] != '/':
        path = path[:-1]
    return path


#Prints JSON once read
def printJSON(json):
    keys = list(json)
    for key in keys:
        for i in range(len(json[key])):
            print(json[key][i])
        print()


#Read from json file into a matrix
path = os.path.dirname(sys.modules['__main__'].__file__)
path = upAFolder(path)
path = path + "json/matrices.json"
data = json.load(open(path))

printJSON(data)



