public class NetworkLink {
	private String _src;
	private String _dst;
	private String _cost;

	// TODO: include the current cost and predecessor
	
	public NetworkLink(String src, String dst, String cost) {
		_src = src;
		_dst = dst;
		_cost = cost;
	}
	
	public String getSrc() {
		return _src;
	}
	
	public String getDst() {
		return _dst;
	}
	
	public String getCost() {
		return _cost;
	}
	// TODO: include getters and setters for the current cost and predecessor
}
