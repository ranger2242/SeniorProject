import json
import sys
import os
from socketIO_client_nexus import SocketIO, LoggingNamespace

class Main:

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
    def createOutputJSONFile(self, fileName,content):
        root = self.getProjectRootDirectory()
        jsonDirectory = root + "json/"
        file = open(jsonDirectory + fileName, "w")
        file.write(content)
        file.close()

    #Connect to JS Server
    def connectToServer(self, ip, port):

        def on_bbb_response(*args):
            print('on_bbb_response', args)

        def receive_data(*args):
            print("receive", args)

        socket = SocketIO(ip, port, LoggingNamespace)
        socket.emit("id", 1, on_bbb_response)
        socket.on("SEND-PYTHON", receive_data)
        socket.wait_for_callbacks(seconds=100)

#--Code start--
ip = "localhost" #"10.133.225.28"
port = "2242"
main = Main()
main.connectToServer(ip, port)


