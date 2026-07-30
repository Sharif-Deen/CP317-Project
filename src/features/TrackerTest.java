package features;

public class TrackerTest {
    public static void main(String[] args) {

        //Test 01: Regular delivery delivery run using local places.
        System.out.println("=== TEST 1: Standard Delivery Run ===");
        Graph kwcGraph = new Graph();

        Node hub = new Node("Laurier Food Service");
        Node uptown = new Node("Uptown Waterloo - Pizzeria");
        Node uw = new Node("UW Student Life Center Food Court");
        Node conestoga = new Node("Conestoga Mall Food court");

        
        hub.addAdjNode(uptown, 3.0, 50.0);       
        uptown.addAdjNode(uw, 2.0, 40.0);  

        kwcGraph.addNode(hub);
        kwcGraph.addNode(uptown);
        kwcGraph.addNode(uw);
        kwcGraph.addNode(conestoga);

        Node vanLocation1 = kwcGraph.getNode("Laurier Food Service");
        Node customerDest1 = kwcGraph.getNode("UW Student Life Center Food Court");

        if (vanLocation1 == null || customerDest1 == null) {
            System.err.println("Error in Test 1: Node not found in graph!");
            return;
        }

        int eta1 = TrackingAlgorithm.getETAMinutes(vanLocation1, customerDest1);
        String summary1 = TrackingAlgorithm.trackingSummary(vanLocation1, customerDest1);

        System.out.println("Summary Output: " + summary1);
        System.out.println("ETA Minutes:    " + eta1 + " mins\n");



        //Test 02: Test where the Van is already at the location, we expect 0 minutes to be the output. 
        System.out.println("=== TEST 2: Van Already At Destination ===");
        
        kwcGraph.resetGraph();

        Node sameLocation = kwcGraph.getNode("Laurier Food Service");

        if (sameLocation == null) {
            System.err.println("Error in Test 2: Node not found in graph!");
            return;
        }

        int eta2 = TrackingAlgorithm.getETAMinutes(sameLocation, sameLocation);
        String summary2 = TrackingAlgorithm.trackingSummary(sameLocation, sameLocation);

        System.out.println("Summary Output: " + summary2);
        System.out.println("ETA Minutes:    " + eta2 + " mins\n");





        //Test 03 - Node without an achievable edge (road) directly connecitng it. 
        System.out.println("=== TEST 3: Unreachable Destination ===");
        
        kwcGraph.resetGraph();

        Node isolatedNode = new Node("Isolated Warehouse");
        kwcGraph.addNode(isolatedNode); //Add a node which has no connections.

        Node vanLocation3 = kwcGraph.getNode("Laurier Food Service");

        if (vanLocation3 == null || isolatedNode == null) {
            System.err.println("Error in Test 3: Node not found in graph!");
            return;
        }

        int eta3 = TrackingAlgorithm.getETAMinutes(vanLocation3, isolatedNode);
        String summary3 = TrackingAlgorithm.trackingSummary(vanLocation3, isolatedNode);

        System.out.println("Summary Output: " + summary3);
        System.out.println("ETA Minutes:    " + eta3 + " mins\n");
    }

}