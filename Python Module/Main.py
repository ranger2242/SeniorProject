import json
import sys
import os



ip = "localhost"
port = 2242

# This works
import socketIO_client as client
socketIO = client.SocketIO(ip, 2242, client.LoggingNamespace)


#This also works
# import socket
# socketIO = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# path = (ip, port)
# connection = socket.create_connection(path)





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
    tmp = open(jsonDirectory + "python_output.tmp", "w")
    tmp.close()
    file = open(jsonDirectory + "python_output.json", "w")
    file.write("{")
    file.write(content)
    file.write("}")
    file.close()
    os.remove(jsonDirectory + "python_output.tmp")



# Read from json file into a matrix
# path = getProjectRootDirectory()
# path = path + "\json\matrices.json"
# data = json.load(open(path))


def writeMatricesBackToJava(dataToWrite):
    matrices = ""

    for key in dataToWrite.keys():
        matrices += "\"" +key + "\" : ["
        for i in range(len(dataToWrite[key])):
            matrices += "["
            for j in range(len(dataToWrite[key][i])):
                value = dataToWrite[key][j][i]
                matrices += str(value)
            matrices += "],"
        matrices += "]"


    matrices = matrices[:-2] + "]" #Remove last comma
    return matrices


# j = "\"post\" : \"python\", " + writeMatricesBackToJava(data)
# createOutputJSONFile(j)


# createOutputJSONFile("\"hello\" : \"there\"")

