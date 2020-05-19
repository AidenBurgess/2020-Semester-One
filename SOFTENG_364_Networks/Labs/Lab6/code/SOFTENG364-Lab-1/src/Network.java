import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Network {
	private Network() {		
	}
	
	private List<NetworkNode> _nodes = new ArrayList<>(); 
	private List<NetworkLink> _links = new ArrayList<>(); 

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
        
		JSONArray linkData = (JSONArray)data.get("links");
        itr = linkData.iterator();

        while (itr.hasNext()) {
            JSONObject linkItem = (JSONObject)itr.next();
            String src = (String)linkItem.get("source");
            String dst = (String)linkItem.get("target");
            String cost = "" + linkItem.get("cost");
            network._links.add(new NetworkLink(src, dst, cost));
		}
        
		return network;
	}
	
	public List<NetworkNode> getNodes() {
		return Collections.unmodifiableList(_nodes);
	}

	// TODO: add a getter for retrieving the links
	public List<NetworkLink> getLinks() {
		return Collections.unmodifiableList(_links);
	}
}
