package drones;

import drones.BaseDrone.DroneType;

public class DroneBuilder {
	public static BaseDrone buildDrone(String[] data) {
		switch (DroneType.valueOf(data[1].toUpperCase())) {
			case OPERATOR: 
				return new OperatorDrone(data[0], data[1], data[2], data[3]);
			case RELAY: 
				return new RelayDrone(data[0], data[1], data[2], data[3]);
			case  SEARCH: 
				return new SearchDrone(data[0], data[1], data[2], data[3]);
		}
	    return new BaseDrone(data[0], data[1], data[2], data[3]);
	}
}
