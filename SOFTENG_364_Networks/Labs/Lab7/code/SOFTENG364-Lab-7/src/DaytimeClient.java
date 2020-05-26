import java.io.*;
import java.net.*;

public class DaytimeClient {
	public static void main(String[] args) {
		String hostname = "time.nist.gov";
		int port = 13;
		try {
			Socket socket = new Socket(hostname, port);

			System.out.format("Requesting time%n");
			DataInputStream dataIn = new DataInputStream(socket.getInputStream());
			int character;
			StringBuilder msg = new StringBuilder();
			while ((character = dataIn.read()) != -1) {
				msg.append((char) character);
			}
			System.out.format("Received message%s", msg.toString());
			System.out.format("Done%n");
			socket.close();
		} catch (IOException e) {
			System.err.format("Something went wrong: '%s'%n", e.getMessage());
			e.printStackTrace();
		}
	}
}