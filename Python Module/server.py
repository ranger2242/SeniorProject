import socket
import time
from _thread import *
import select

'''
    IDs:
    -Java Module: 0
    -Python Module: 1
'''


def threaded_client(connection):
    print("Connection:", end=" ")

    while True:
        data = connection.recv(4096)
        clientID = int(data.decode("utf-8"))

        if clientID == 0:
            print("Java Client")
        if clientID == 1:
            print("Python Client")
        else:
            print("Bad ID -- Rejecting Client")
            break

        connection.send(str("Connection Successful").encode())

    connection.close()


class Server:

    def __init__(self, host, port):

        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        try:
            self.s.bind((host, port))
        except socket.error as e:
            print(str(e))

        self.s.listen(5)


    def run(self):
        while True:
            conn, address = self.s.accept()
            start_new_thread(threaded_client, (conn,))





# --Program Start--
host = "localhost"
port = 2242
server = Server(host, 2242)
print("Server is running")
server.run()
print("Server: SHUT DOWN")
