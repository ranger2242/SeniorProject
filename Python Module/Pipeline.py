import json
import sys
import os
from socketIO_client_nexus import SocketIO, LoggingNamespace
import json

class Pipeline:

    def __init__(self):
        print("Pipeline Initialized")


    # Prints JSON once read
    def print_json(self, json):
        keys = list(json)
        for key in keys:
            for i in range(len(json[key])):
                print(json[key][i])
            print()

    # Get project Directory
    def get_project_root_directory(self):

        # Move Up one directory
        def upAFolder(path):
            if path != "":
                while path[-1] != '\\':
                    path = path[:-1]
            return path

        path = sys.path.__getitem__(0)
        path = upAFolder(path)
        return path

    # Connect to JS Server
    def connectToServer(self, ip, port):
        def on_bbb_response(*args):
            print('on_bbb_response', args)

        def receive_data(*args):
            print("Received:", args[0])

        def send_to_client(key, data):
            j = {key: data}
            socket.emit("SEND-CLIENT", json.dumps(j))

        socket = SocketIO(ip, port, LoggingNamespace)
        socket.emit("id", 1, on_bbb_response)
        socket.on("SEND-PYTHON", receive_data)
        socket.wait_for_callbacks(seconds=60)


