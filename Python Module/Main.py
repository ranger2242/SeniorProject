import json
import sys


# Prints JSON once read
def printJSON(json):
    keys = list(json)
    for key in keys:
        for i in range(len(json[key])):
            print(json[key][i])
        print()


# Get project Directory
def getProjectRootDirectory():

    # Move Up one directory
    def upAFolder(path):
        if path != "":
            while path[-1] != '\\' :
                path = path[:-1]
        return path

    path = sys.path.__getitem__(0)
    path = upAFolder(path)
    return path


# Create JSON output file
def createOutputJSONFile(content):
    root = getProjectRootDirectory()
    jsonDirectory = root + "json/"
    file = open(jsonDirectory + "python_output.json", "w")
    file.write("{")
    file.write(content)
    file.write("}")
    file.close()


# Read from json file into a matrix
path = getProjectRootDirectory()
path = path + "\json\matrices.json"
data = json.load(open(path))


createOutputJSONFile("\"hello\" : \"world\"")

