import java.net.*;
import java.io.*;

public class EchoServer {
	private static final int localPort = 3333;

	public static void main(String args[]) {
		System.out.format("Starting echo server%n");
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(localPort);

			System.out.format("Listing in port %d%n", localPort);
			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(new Runnable() {
					public void run() {
						try {
							handleClientConnection(socket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

	private static void handleClientConnection(Socket socket) throws IOException {
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
		System.out.format("Connected to %s%n", socket.getRemoteSocketAddress());

		String msg = "";
		while (!msg.equals("stop")) {
			msg = dataIn.readUTF();
			System.out.format("Received message '%s' from client%n", msg);
			dataOut.writeUTF("You said " + msg);
			dataOut.flush();
		}

		System.out.format("Stopping echo server%n");
		dataIn.close();
		dataOut.close();
		socket.close();
	}
}
