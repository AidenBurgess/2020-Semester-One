import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Network {
	private Network() {		
	}
	
	private List<NetworkNode> _nodes = new ArrayList<NetworkNode>(); 
	// TODO: store the links
	
	public static Network parse(JSONObject data) {
		Network network = new Network();	
		
		JSONArray nodeData = (JSONArray)data.get("nodes");
        Iterator itr = nodeData.iterator();

        while (itr.hasNext()) {
            JSONObject nodeItem = (JSONObject)itr.next();
            String id = (String)nodeItem.get("id");
            network._nodes.add(new NetworkNode(id));
		}
		
		// TODO: load the links
        
		return network;
	}
	
	public List<NetworkNode> getNodes() {
		return Collections.unmodifiableList(_nodes);
	}

	// TODO: add a getter for retrieving the links
}
