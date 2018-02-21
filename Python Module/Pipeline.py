import sys
from socketIO_client_nexus import SocketIO, LoggingNamespace
import json
from Analyzer import Analyzer
from Helpers import slog
import ast


class Pipeline:

    id = str
    socket = SocketIO

    def __init__(self):
        self.analyzer = Analyzer()
        slog("Pipeline Initialized")


    # Prints JSON once read
    def print_json(self, json):
        keys = list(json)
        for key in keys:
            for i in range(len(json[key])):
                print(json[key][i])
            print()

    # Connect to JS Server
    def connect_to_server(self, ip, port):

        def receive_data(*args):
            id = args[0]['id']
            data = ast.literal_eval(args[0]['data'])
            print("Received:", data, '\tFrom Client ID:', id)
            self.send_to_client(id, "received", "true")

            # Probably start a new thread here
            results = self.analyzer.analyze(data)
            self.send_to_client(id, "RESULTS", results)

        def connected(*args):
            slog("Connected To Server")

        def id(*args):
            self.socket.emit("ID", 1)
            self.id = args[0]



        self.socket = SocketIO(ip, port, LoggingNamespace)
        self.socket.on("SEND-PYTHON", receive_data)
        self.socket.on("CONNECTED", connected)
        self.socket.on("ID", id)
        self.socket.wait()

    def send_to_client(self, client_id, key, data):
        j = {key: data}
        self.socket.emit("SEND-CLIENT", (client_id, json.dumps(j)))



# Connect To Server
ip = "localhost"
port = "2242"
pipeline = Pipeline()
pipeline.connect_to_server(ip, port)
