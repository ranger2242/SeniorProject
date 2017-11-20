import json
import sys
import os
import socket

class Main:

    ip = "localhost"
    port = 2242

    socketIO = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    path = (ip, port)
    connection = socket.create_connection(path)
    ID = "1"
    connection.send(ID.encode())
    connection.send("Ready".encode())

    while not connection._closed:

        data = connection.recv(4096)
        text = data.decode("utf-8")
        print(text)
        # handleCode(text)
        if not data:
            break



    # Prints JSON once read
    def printJSON(self, json):
        keys = list(json)
        for key in keys:
            for i in range(len(json[key])):
                print(json[key][i])
            print()


    # Get project Directory
    def getProjectRootDirectory(self):

        # Move Up one directory
        def upAFolder(path):
            if path != "":
                while path[-1] != '\\':
                    path = path[:-1]
            return path

        path = sys.path.__getitem__(0)
        path = upAFolder(path)
        return path


    # Create JSON output file
    def createOutputJSONFile(self, content):
        root = self.getProjectRootDirectory()
        jsonDirectory = root + "json/"
        tmp = open(jsonDirectory + "python_output.tmp", "w")
        tmp.close()
        file = open(jsonDirectory + "python_output.json", "w")
        file.write("{")
        file.write(content)
        file.write("}")
        file.close()
        os.remove(jsonDirectory + "python_output.tmp")

    def readJSON(self):
        # Read from json file into a matrix
        path = self.getProjectRootDirectory()
        path = path + "\json\matrices.json"
        data = json.load(open(path))

    def handleCode(self, code):
        print()
        # if code == "R1":
        #     self.R1()
        # if code == "JSON1":
        #     self.readJSON()

    def R1(self):
        print("R1 received")

