import sys
from socketIO_client_nexus import SocketIO, LoggingNamespace
import json
import _datetime as datetime
from Analyzer import Analyzer


class Pipeline:

    id = str
    socket = SocketIO
    analyzer = Analyzer()

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
            self.send_to_client(id, "received", "true")

            # Proabably start a new thread here
            # Send data to NN here
            Analyzer.use_neural_networks(data)

        def connected(*args):
            self.slog("Connected To Server")

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
