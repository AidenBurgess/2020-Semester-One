package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import drones.BaseDrone;
import drones.DroneBuilder;

public class DSRSNetwork {

	public static void main(String[] args) {
		ArrayList<BaseDrone> clients = null;
		try {
			 clients = initClients();
		} catch (IOException e) {
			System.out.println("File in wrong format, or could not be found");
			e.printStackTrace();
		}
		System.out.println(clients);
		pingClients(clients);
	}

	private static ArrayList<BaseDrone> initClients() throws FileNotFoundException, IOException {
		BufferedReader csvReader = new BufferedReader(new FileReader("clients-Relay1.csv"));
		String row;
		ArrayList<BaseDrone> drones = new ArrayList<>();
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    drones.add(DroneBuilder.buildDrone(data));
		}
		csvReader.close();
		
		return drones;
	}
	
	private static void pingClients(ArrayList<BaseDrone> clients) {
		clients.forEach(client -> {
			client.ping();
		});
	}
	

}
