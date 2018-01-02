import json
import sys
import os
from socketIO_client_nexus import SocketIO, LoggingNamespace
import json
import _datetime as datetime


class Pipeline:
    id = str



    def __init__(self):
        self.slog("Pipeline Initialized")


    # Logs server communication with time
    def slog(self, message):
        fmt = '%Y-%m-%d::%H:%M:%S: '
        date_string = datetime.datetime.now().strftime(fmt)
        print(date_string + message)


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
    def connect_to_server(self, ip, port):

        def receive_data(*args):
            id = args[0]['id']
            data = args[0]['data']
            print("Received:", data, '\tFrom Client ID:', id)
            send_to_client(id, "received", "true")

            # Send data to NN here

        def send_to_client(client_id, key, data):
            j = {key: data}
            socket.emit("SEND-CLIENT", (client_id, json.dumps(j)))

        def connected(*args):
            self.slog("Connected To Server")

        def id(*args):
            socket.emit("ID", 1)
            self.id = args[0]



        socket = SocketIO(ip, port, LoggingNamespace)
        socket.on("SEND-PYTHON", receive_data)
        socket.on("CONNECTED", connected)
        socket.on("ID", id)
        socket.wait_for_callbacks(seconds=5000)

        socket.wait()
