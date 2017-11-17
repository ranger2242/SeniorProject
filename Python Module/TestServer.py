import socket
import time

host = ''
port = 2242
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)


try:
    s.bind((host,port))
except socket.error as e:
    print(str(e))


s.listen(10)

print("Server is running")

conn, addr = s.accept()

print('connected: ' + addr[0] + ':' + str(addr[1]))

for i in range(5):
    time.sleep(1)
    print("seconds connected: " + str(i))

print("program over: ending connection")
