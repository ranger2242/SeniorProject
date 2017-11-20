import socket
import time
from _thread import *

'''
    IDs:
    -Java Module: 0
    -Python Module: 1
'''


class Server:
    p = None
    j = None

    def __init__(self, host, port):

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        try:
            self.s.bind((host, port))
        except socket.error as e:
            print(str(e))

        self.s.listen(5)


    def run(self):

        while True:
            self.checkForMultiConnection()
            conn, address = self.s.accept()
            start_new_thread(self.threaded_client, (conn,))


    def threaded_client(self, connection):
        print("Connection:", end=" ")
        data = connection.recv(1)
        ID = self.indentifyConnection(data)

        if ID == 0:
            print("Java Client")
            self.j = connection
        if ID == 1:
            print("Python Client")
            self.p = connection
        if ID is None:
            print("Bad ID -- Rejecting Connection")
            connection.close()
            return

        while not connection._closed:
            data = connection.recv(4096)
            text = data.decode("utf-8")
            print("ID: " + str(ID) + " -> " + text)
            connection.send(str("Connection Successful").encode())


    def indentifyConnection(self, data):
        text = data.decode("utf-8")

        if not str.isdigit(text):
            print(text)
            return None

        return int(text)


    def checkForMultiConnection(self):
        time.sleep(1)
        if self.p is None or self.j is None:
            return
        print("both connected")

        r1Code = "R1".encode()
        self.p.send(r1Code)
        self.j.send(r1Code)



# --Program Start--
host = "localhost"
port = 2242
server = Server(host, port)
print("Server is running")
server.run()
print("Server: SHUT DOWN")
