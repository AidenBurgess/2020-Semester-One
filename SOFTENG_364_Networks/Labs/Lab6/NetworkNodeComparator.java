import java.util.Comparator;

public class NetworkNodeComparator implements Comparator<NetworkNode> {
	public int compare(NetworkNode node1, NetworkNode node2) {
		String id1 = node1.getId();
		String id2 = node2.getId();
		return id1.compareTo(id2);
	}
}
