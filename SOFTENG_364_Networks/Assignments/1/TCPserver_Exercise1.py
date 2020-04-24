import socket

HOST, PORT = "localhost", 12000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)
print(f"Server started listening {HOST}: {PORT}")
conn, addr = s.accept()

with open("audioTRM.wav", "rb") as data:
    for line in data:
        conn.sendall(line)
conn.close()
