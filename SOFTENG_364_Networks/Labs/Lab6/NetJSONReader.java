import java.io.FileReader;
import java.nio.file.Path;

import org.json.simple.JSONObject; 
import org.json.simple.parser.JSONParser; 

public class NetJSONReader {
	public static void main(String[] args) throws Exception  
    { 
		// Load and parse the JSON
		String currentDir = System.getProperty("user.dir");
		String jsonPath = Path.of(currentDir, "Lab-6.json").toString();
		System.out.format("Loading Network graph from %s\n", jsonPath);
		JSONObject netData = (JSONObject)new JSONParser()
				.parse(new FileReader(jsonPath)); 
          
        // Retrieve the data type and verify it  
        String dataType = (String)netData.get("type"); 
        if (!dataType.equals("NetworkGraph")) {
        	throw new Exception("Unknown network object: '" + dataType + "'"); 
        }
        
        Network network = Network.parse(netData);
		System.out.format("Network graph has been loaded\n");
		System.out.format("Loaded %d nodes\n", network.getNodes().size());
		// TODO: Display the number of links loaded

		// TODO: Visualise the network

		// TODO: Calculate the weights in the network
	} 
}
