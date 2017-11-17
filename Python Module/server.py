import socket
import time
from _thread import *

'''
    IDs:
    -Java Module: 0
    -Python Module: 1
'''


# Start Server
def startServer():
    host = "localhost"
    port = 2242
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    try:
        s.bind((host, port))
    except socket.error as e:
        print(str(e))

    s.listen(10)
    return s



def threaded_client(connection):

    while True:
        data = connection.recv(4096)
        clientID = int(data.decode("utf-8"))

        if clientID == 0:
            print("Connected: Java Client")
        if clientID == 1:
            print("Connected: Python Client")
        else:
            print("Bad ID -- Rejecting Client")
            break

        connection.send(str("Connection Successful").encode())

    connection.close()


# --Program Start--
server = startServer()
print("Server is running")

while True:
    conn, address = server.accept()
    start_new_thread(threaded_client, (conn,))

print("Server: SHUT DOWN")
