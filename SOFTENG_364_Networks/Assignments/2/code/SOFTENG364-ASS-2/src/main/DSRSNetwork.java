package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import drones.BaseDrone;
import drones.BaseDrone.DroneType;

public class DSRSNetwork {
	
	private static  HashMap<String, HashMap<String, Integer>> forwardingTable = new HashMap<>();
	private static ArrayList<BaseDrone> droneList = new ArrayList<>();
	
	public static void main(String[] args) {
		System.out.println("Starting ping process");
		try {
			 initClients();
		} catch (IOException e) {
			System.out.println("File in wrong format, or could not be found");
			e.printStackTrace();
		}
		pingClients();
		try {
			writeClients();
		} catch (IOException e) {
			System.out.println("File could not be written to.");
			e.printStackTrace();
		}
		System.out.println("Ping process finished");
		// Now start Calculating Routes
		try {
			calculateRoutes();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void initClients() throws FileNotFoundException, IOException {
		BufferedReader csvReader = new BufferedReader(new FileReader("clients-Relay1.csv"));
		String row;
		System.out.println("Reading client list: starting");
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    droneList.add(new BaseDrone(data[0], data[1], data[2], data[3]));
		}
		csvReader.close();
		System.out.println("Reading client list: finished - " + droneList.size() + " clients read");
	}
	
	private static void pingClients() {
		System.out.println("Pinging all clients: starting");
		forwardingTable.put("Relay1", new HashMap<String, Integer>());
		
		droneList.forEach(drone -> {
			long startTime = System.currentTimeMillis();
			try {
				ping(drone);
				long endTime = System.currentTimeMillis();
				int duration = (int) (endTime - startTime)/1000;
				drone._lastResponseTime = duration;
				forwardingTable.get("Relay1").put(drone._name, drone._lastResponseTime);
				System.out.print("ping received after " + duration + "s\n");
			} catch (IOException e) {
				drone._lastResponseTime = null;
				System.out.print("could not ping\n");
			}
		});
		System.out.println("Pinging all clients: finished - " + droneList.size() + " clients pinged");
	}
	
	private static void ping(BaseDrone client) throws IOException {
		System.out.println("Pinging " + client._name + "...");
		Socket socket = new Socket(client._IPAddress, client._port);

		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());

		String msg = "PING\n";
		dataOut.writeUTF(msg);
		dataOut.flush();
		
		dataIn.readUTF();
		
		dataOut.close();
		socket.close();
	}
	
	private static void writeClients() throws IOException {
		BufferedWriter fooWriter = new BufferedWriter(new FileWriter("clients-Relay1.csv"));
		droneList.forEach(drone-> {
			try {
				fooWriter.write(String.join(",", drone._name, drone.getTypeString(), drone._fullAddress, String.valueOf(drone._lastResponseTime)));
				fooWriter.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		});
		fooWriter.close();
	}
	
	public static void calculateRoutes() throws UnknownHostException, IOException {
		ServerSocket serverSocket;
			serverSocket = new ServerSocket(10120);
			while (true) {
				Socket socket = serverSocket.accept();
				new Thread(new Runnable() {
					public void run() {
						try {
							handleDVUpdate(socket);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
			
	}
	
	public static void handleDVUpdate(Socket socket) throws IOException {
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

		 String msg = dataIn.readUTF();
		
		if (!msg.contains("UPDATE")) {
			return;
		}
		System.out.println("New DVs received");
		System.out.println("Starting DV update calculation");
		String[] info= msg.split(":");
		String origin = info[1];
		String data = info[2];
		int numUpdates = Integer.parseInt(info[3].replace("\n",""));
		
		dataOut.writeUTF("ACK\n");
		dataOut.flush();
		
		// Get costs in a good format
		HashMap<String, Integer> costs = new HashMap<>();
		for (String costInfo: data.split(",")) {
			String dest = costInfo.split("=")[0];
			Integer cost = Integer.parseInt(costInfo.split("=")[1]);
			forwardingTable.get(origin).put(dest, cost);
		}
		
		updateForwardingTable();
		
//		boolean updatesExist = updateForwardingTable(originDrone, costs);
//		if (updatesExist) {
//			sendUpdatedDV();
//		} else {
//			System.out.println("Skipping DV update send");
//		}
		

		
		dataIn.close();
		dataOut.close();
		socket.close();
		System.out.println("DV update calculation finished");
		
		writeForwardingTable();
	}
	
	private static void updateForwardingTable() {
		for (BaseDrone drone: droneList) {
			calculateCost(drone._name);
		}
		forwardingTable.forEach((key, value) -> {
			System.out.println(key + " "  + value);
		});
	}
	
	
	private static void sendUpdatedDV() {
//		System.out.println("Sending updated DVs");
//		for (BaseDrone drone: clients) {
//			if (drone._type != DroneType.RELAY) { continue; }
//			System.out.print("- Sending to " + drone._name + "...");
//			try {
//				sendUpdate(drone);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			System.out.println("done");
//		}
	}
	
	private static void sendUpdate(BaseDrone client) throws IOException {
//		Socket socket = new Socket(client._IPAddress, client._port);
//
//		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
//		DataInputStream dataIn = new DataInputStream(socket.getInputStream());
//
//		String msg = "UPDATE:";
//		msg += "Relay1:";
//		ArrayList<String> paths = new ArrayList<>();
//		for (Map.Entry<String,String> path : currentPath.entrySet())  {
//			String dest = path.getKey();
//			String through = path.getValue();
//			if (dest.equals("Relay1")) { continue; }
//			int cost = networkCosts.get(through).get(dest);
//			paths.add(dest+"="+cost);
//		}
//		msg += String.join(",", paths);
//		msg +=":"+ clients.size() + "\n";
//
//		dataOut.writeUTF(msg);
//		dataOut.flush();
//		
//		dataIn.readUTF();
//		
//		dataOut.close();
//		socket.close();
	}
	
	private static void writeForwardingTable() throws IOException {
//		BufferedWriter fooWriter = new BufferedWriter(new FileWriter("forwarding-Relay1.csv"));
//		currentPath.forEach( (dest, through) -> {
//			if (dest.equals("Relay1")) { return; }
//			try {
//				fooWriter.write(dest + "," + through);
//				fooWriter.newLine();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		});
//		fooWriter.close();
	}
	
	private static void calculateCost(String dest) {
		// D_relay1(to) = min(c(relay1, through) + c(through, dest))
		String src = "Relay1";

		System.out.print("- Calculating cost for " + dest + "...");
		Integer newCost = Integer.MAX_VALUE;
		String altDrone = "";
		for (BaseDrone throughDrone: droneList) {
			String thru = throughDrone._name;
			Integer thruCost = forwardingTable.get(src).get(thru);
			Integer destCost = forwardingTable.get(thru).get(dest);
			if (thruCost == null || destCost == null) { continue; }
			if (thruCost+destCost < newCost) {
				newCost = thruCost+destCost;
				altDrone = thru;
			}
		}
		
		System.out.println(newCost);
		
//		// Check if alternative route has been found
//		if (newCost == currentCosts.get(near)) {
//			System.out.println("no change");
//		} else {
//			changed = true;
//			if (newCost == Integer.MAX_VALUE) { newCost = -1; }
//			System.out.println("cost updated to " + newCost + " via " + altDrone);	
//			currentCosts.put(near, newCost);
//			currentPath.put(near, altDrone);
//		}
	
	}

}
