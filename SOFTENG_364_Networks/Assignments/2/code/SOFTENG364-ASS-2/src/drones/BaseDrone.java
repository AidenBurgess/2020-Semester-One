package drones;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BaseDrone {
	public enum DroneType {
		OPERATOR, 
		RELAY,
		SEARCH
	}
	
	public String _name;
	public DroneType _type;
	public String _IPAddress;
	public int _port;
	public Integer _lastResponseTime;
	public String _fullAddress;
	
	
	public BaseDrone(String name, String type, String IPAddress, String lastResponseTime) {
		
		_name = name;
		_type = DroneType.valueOf(type.toUpperCase());
		_IPAddress = IPAddress.split(":")[0];
		_port = Integer.parseInt(IPAddress.split(":")[1]);
		_fullAddress = IPAddress;
		_lastResponseTime = Integer.parseInt(lastResponseTime) ;
	}
	
	public void start() {
		System.out.println("Starting " + _name);
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(_port);

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
	
	private  void handleClientConnection(Socket socket) throws IOException {
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
		System.out.println("Connected to " + socket.getRemoteSocketAddress());

		String msg = dataIn.readUTF();
		dataOut.writeUTF("ACK");
		dataOut.flush();

		System.out.println("Stopping " + _name);
		dataIn.close();
		dataOut.close();
		socket.close();
	}

	public String getTypeString() {
		String lowercase = _type.toString().toLowerCase();
		return lowercase.substring(0, 1).toUpperCase() + lowercase.substring(1);
	}
	
}
