import java.util.HashMap;
import java.util.Map;

//Core of the graph, which will be used to track the shortest path between nodes.
//A graph is how we will store the nodes and edges, and will be used to find the shortest path between nodes.

public class Graph {
    private final Map<String, Node> nodes = new HashMap<>();

    public void addNode(Node node) {
        nodes.put(node.getName(), node);
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public void resetGraph() {
        for (Node node : nodes.values()) {
            node.setDistance(Double.MAX_VALUE);
            node.getShortestPath().clear();
        }
    }
}