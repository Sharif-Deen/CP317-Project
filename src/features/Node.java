
import java.util.List;
import java.util.LinkedList; 
import java.util.Map;
import java.util.HashMap;;

// Setup for Nodes which will be utalized in our graph which is the core of the tracking process.
// Hashmaps and linked lists will be efficient data structures to utalize for the purpose of tracking the shortest path.

public class Node implements Comparable<Node>{
    private final String name;
    private Double distance = Double.MAX_VALUE;
    private List<Node> shortestPath = new LinkedList<>();
    private Map<Node, Edge> adjNodes = new HashMap<>();

    public Node(String name) {
        this.name = name; 
    }

    public void addAdjNode(Node node, double distanceKm, double speedLimitKmh) {
        Edge edge = new Edge(node, distanceKm, speedLimitKmh);
        adjNodes.put(node, edge);
    }

    @Override
    public int compareTo(Node node) {
        return Double.compare(this.distance, node.getDistance());
    }

    
    // Getters and Setters
    public String getName() {
        return name; 
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Map<Node, Edge> getAdjNodes() {
        return adjNodes;
    }

    public void setAdjNodes(Map<Node, Edge> adjNodes) {
        this.adjNodes = adjNodes;
    }
}

