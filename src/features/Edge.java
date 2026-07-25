package features;

public class Edge {
    //Node the edge connects TO. 
    private final Node targetNode;
    private final double distanceKm;
    private final double speedLimitKmh;

    public Edge(Node targetNode, double distanceKm, double speedLimitKmh) {
        this.targetNode = targetNode;
        this.distanceKm = distanceKm; 
        this.speedLimitKmh = speedLimitKmh; 
    }

    // Method to calculate the travel time in minutes according to distance and speed limit.
    // Formula:  (Distance / Speed) * 60 
    public double travelTimeMinutes() {
        if(speedLimitKmh <= 0) 
            return 0.0;
        
        return (distanceKm / speedLimitKmh) * 60.0;
    }

    // Getters
    public Node getTargetNode() {
        return targetNode;
    }
    public double getDistanceKm() {
        return distanceKm;
    }
    public double getSpeedLimitKmh() {
        return speedLimitKmh;
    }
}
