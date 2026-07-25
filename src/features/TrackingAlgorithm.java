import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;

import java.util.ArrayList;

public class TrackingAlgorithm {
    /**
     * For the purposes of this implimentation, we have decided to opt into using Dijkstra's algorithm.
     * The main concern for the company is finding the shortest path to minimize wear and tear on company vehicles, and maintain quick and reliable pathing for  
     * vans to take. 
     * Sets and priority queues will be very efficient in the implmentation of the algorithm in java. 
     */

    public static void calculateShortestPath(Node source) {
        source.setDistance(0.0);
        Set<Node> settledNodes = new HashSet<>();
        Queue<Node> unsettledNodes = new PriorityQueue<>();
        
        // Initialize our unsettled nodes with our base node 
        unsettledNodes.add(source);

        while(!unsettledNodes.isEmpty()) {
            Node currNode = unsettledNodes.poll();
            currNode.getAdjNodes().entrySet().stream().filter(entry -> !settledNodes.contains(entry.getKey())).forEach(entry -> {
                Node neighbour = entry.getKey();
                Edge edge = entry.getValue();
                evalDistPath(neighbour, edge.getDistanceKm(), currNode);    //This code is what will allow us to evaluate, using the helper function to determine the right path using distance between nodes.
                unsettledNodes.add(entry.getKey());
            });

            //add the node we are currently working with to settled nodes after looping through adjnodes to it.
            settledNodes.add(currNode); 
        }
    } 

    private static void evalDistPath(Node adjNode, Double edgeDistance, Node sourceNode) {
        Double comparisonDistance = sourceNode.getDistance() + edgeDistance;
        
        if(comparisonDistance < adjNode.getDistance()) {
            adjNode.setDistance(comparisonDistance);
            adjNode.setShortestPath(Stream.concat(sourceNode.getShortestPath().stream(), Stream.of(sourceNode)).toList());
        }
    }

    /** We need to provide to customers a rough eta in minutes. 
    * After we determine the path, we need to proceed through it and simply sum up the travel time.
    * For the purposes of this task, we will return both the path taken and the ETA in minutes.
    */

    public static int getETAMinutes(Node sourceNode, Node destinationNode) {
        calculateShortestPath(sourceNode);

        List<Node> Path = new ArrayList<>(destinationNode.getShortestPath());
        Path.add(destinationNode);

        double totalMins = 0.0; 
        for(int i = 0; i < Path.size() -1; i++) {
            Node curr = Path.get(i);                    //Simple calculation to compute total minutes travelled.
            Node next = Path.get(i + 1);                //Edge.travelTimeMinutes computes the average speed based off speed limits and distance for the purposes of testing. 

            Edge edge = curr.getAdjNodes().get(next);
            if(edge != null) {
                totalMins += edge.travelTimeMinutes();
            }
        }

        return (int) Math.round(totalMins);
    }


    public static String trackingSummary(Node sourceNode, Node destinationNode) {
        int etaMins = getETAMinutes(sourceNode, destinationNode);

        String pathStr = destinationNode.getShortestPath().stream().map(Node::getName).collect(Collectors.joining(" -> "));

        String route = pathStr.isBlank()
                ? destinationNode.getName()
                : "%s -> %s".formatted(pathStr, destinationNode.getName());

        return "%s | ETA: %d mins".formatted(route, etaMins);
    }
}