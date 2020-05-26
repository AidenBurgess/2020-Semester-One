import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class EchoClient {
	public static void main(String[] args) {
		String hostname = "localhost";
		int port = 3333;
		try {
			Socket socket = new Socket(hostname, port);

			DataInputStream dataIn = new DataInputStream(socket.getInputStream());
			DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
			
			String returned = "";
			returned  = dataIn.readUTF();

			String msg = "Hello World! stop";
			dataOut.writeUTF(msg);
			dataOut.flush();
			

			
			System.out.format("Received message%s ", returned);
			System.out.println("Done");
			socket.close();
		} catch (IOException e) {
			System.err.format("Something went wrong: '%s'%n", e.getMessage());
			e.printStackTrace();
		}
	}
}
