
public class NetworkNode {
	private String _id;
	// TODO: include the current cost and predecessor
	private long _cost = Long.MAX_VALUE;
	private String _predecessor = "";

	public NetworkNode(String id) {
		_id = id;
	}
	
	public String getId() {
		return _id;
	}
	
	// TODO: include getters and setters for the current cost and predecessor
	public long getCost() {
		return _cost;
	}
	
	public void setCost(long cost) {
		_cost = cost;
	}
	
	public String getPredecessor() {
		return _predecessor;
	}

	public void setPredecessor(String predecessor) {
		_predecessor = predecessor;
	}
}
