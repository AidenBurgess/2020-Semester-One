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
	private static HashMap<String, Integer> oldCosts = new HashMap<>();
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
		    BaseDrone newDrone = new BaseDrone(data[0], data[1], data[2], data[3]);
		    droneList.add(newDrone);
			// Add initial row to costs
			HashMap<String, Integer>initCosts = new HashMap<String, Integer>();
			initCosts.put(newDrone._name, 0);
			forwardingTable.put(newDrone._name, initCosts);
		}
		csvReader.close();
		System.out.println("Reading client list: finished - " + droneList.size() + " clients read");
	}
	
	private static void pingClients() {
		System.out.println("Pinging all clients: starting");
		forwardingTable.put("Relay1", new HashMap<String, Integer>());
		forwardingTable.get("Relay1").put("Relay1", 0);
		
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
		// Preserve old costs
		copyOldCosts();
		
		// Get costs in a good format
		for (String costInfo: data.split(",")) {
			String dest = costInfo.split("=")[0];
			Integer cost = Integer.parseInt(costInfo.split("=")[1]);
			if (cost < 0) {
				forwardingTable.get(origin).remove(dest);
				cost = null;
			} else {
				forwardingTable.get(origin).put(dest, cost);				
			}
			// Update drones if need be
			if (origin.equals("Relay1")) {
				for (BaseDrone drone: droneList) {
					if (drone._name.equals(dest)) {
						drone._lastResponseTime = cost;
					}
				}
			}
		}
		
		ArrayList<String> updates = updateForwardingTable();
		sendUpdatedDV(updates);
		
		dataIn.close();
		dataOut.close();
		socket.close();
		System.out.println("DV update calculation finished");
		
		writeForwardingTable();
	}
	
	private static ArrayList<String> updateForwardingTable() {
		ArrayList<String> updated = new ArrayList<>();
		for (BaseDrone drone: droneList) {
			String update = calculateCost(drone._name);
			if (update != null) {
				updated.add(update);
			}
		}
		return updated;
	}
	
	
	private static void sendUpdatedDV(ArrayList<String> updates) {
		if (updates.size() == 0) {
			System.out.println("Skipping DV update send");
			return;
		}
		
		System.out.println("Sending updated DVs");
		// build update string
		String updateMsg = "UPDATE:";
		updateMsg += "Relay1:";
		ArrayList<String> paths = new ArrayList<>();
		for (String update: updates) {

			String[] info = update.split(" ");
			String dest = info[0];
			String thru = info[1];
			String cost = info[2];
			paths.add(dest + "=" + cost);
		}
		updateMsg += String.join(",", paths);
		updateMsg +=":"+ updates.size() + "\n";
		
		for (BaseDrone drone: droneList) {
			if (drone._type != DroneType.RELAY) { continue; }
			System.out.print("- Sending to " + drone._name + "...");
			try {
				sendUpdate(drone, updateMsg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("done");
		}
	}
	
	private static void sendUpdate(BaseDrone client, String msg) throws IOException {
		Socket socket = new Socket(client._IPAddress, client._port);

		DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
		DataInputStream dataIn = new DataInputStream(socket.getInputStream());

		dataOut.writeUTF(msg);
		dataOut.flush();
		
		dataIn.readUTF();
		
		dataOut.close();
		socket.close();
	}
	
	private static void writeForwardingTable() throws IOException {
		BufferedWriter fooWriter = new BufferedWriter(new FileWriter("forwarding-Relay1.csv"));
		droneList.forEach( drone -> {
			String dest = drone._name;
			String[] info = getPath(dest).split(" ");
			String thru = info[1];
			
			if (dest.equals("Relay1")) { return; }
			
			try {
				fooWriter.write(thru + "," + dest);
				fooWriter.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		fooWriter.close();
	}
	
	private static String calculateCost(String dest) {
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
		Integer oldCost = oldCosts.get(dest);
		if (oldCost == null) {
			oldCost = Integer.MAX_VALUE;
		}
		
		if (!newCost.equals(oldCost)) {
			if (newCost.equals(Integer.MAX_VALUE)) {
				forwardingTable.get("Relay1").remove(dest);
				System.out.println("cost updated to -1, " + dest + " is unreachable");
				return dest + " " +  "Relay1" + " -1";
			} else {
				forwardingTable.get("Relay1").put(dest, newCost);
				System.out.println("cost updated to " + newCost + " via " + altDrone);
				return dest + " " + altDrone + " " + newCost;
			}
		}
		else {
			System.out.println("no change");
		}
		
		return null;
	}
	
	private static String getPath(String dest) {
		String src = "Relay1";
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
		Integer oldCost = oldCosts.get(dest);
		if (oldCost == null) {
			oldCost = Integer.MAX_VALUE;
		}
		return dest + " " + altDrone + " " + newCost; 
	}
	
	private static void copyOldCosts() {
		oldCosts.clear();
	    for (Map.Entry<String, Integer> entry : forwardingTable.get("Relay1").entrySet()) {
	    	oldCosts.put(entry.getKey(),entry.getValue());
	    }
	}
	
	private static void resetBaseRow() {
	    HashMap<String, Integer> baseRow = forwardingTable.get("Relay1");
	    baseRow.clear();
	    for (BaseDrone drone: droneList) {
	    	if (drone._lastResponseTime == null) {
	    		continue;
	    	}
	    	baseRow.put(drone._name, drone._lastResponseTime);
	    }
	}

}
