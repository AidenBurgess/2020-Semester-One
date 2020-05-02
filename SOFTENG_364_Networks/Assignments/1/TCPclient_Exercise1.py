import socket, time

HOST, PORT = "localhost", 12000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))
print(f"Connection established to server {HOST}: {PORT}")

with open("received.wav", "wb") as data:
    start_time = time.time()
    while True:
        line = s.recv(1024)
        if not line:
            break
        data.write(line)
end_time = time.time()
s.close()

print("File transfer complete – file name: audio.wav, saved as received.wav")
print(f"start: {start_time} end: {end_time} difference: {end_time-start_time}")
