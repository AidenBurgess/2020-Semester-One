package drones;

public class BaseDrone {
	enum DroneType {
		OPERATOR, 
		RELAY,
		SEARCH
	}
	
	public String _name;
	public DroneType _type;
	public String _IPAddress;
	public int _lastResponseTime;
	
	
	public BaseDrone(String name, String type, String IPAddress, String lastResponseTime) {
		_name = name;
		_type = DroneType.valueOf(type.toUpperCase());
		_IPAddress = IPAddress;
		_lastResponseTime = Integer.parseInt(lastResponseTime) ;
	}


	public void ping() {
		System.out.println(_name + " pinged");
	}


	@Override
	public String toString() {
		return "BaseDrone [_name=" + _name + ", _type=" + _type + ", _IPAddress=" + _IPAddress + ", _lastResponseTime="
				+ _lastResponseTime + "]";
	}
	
	
}
