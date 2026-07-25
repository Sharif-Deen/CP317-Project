public class Van {
    private final String vanID; 
    private Node currNode;   //This is the location of the node that the van is at. 

    public Van(String vanID, Node currNode) {
        this.vanID = vanID; 
        this.currNode = currNode; 
    }

    //Getters 
    public String getVanID() {
        return vanID; 
    }
    public Node getCurrNode() {
        return currNode;
    }

    // Need a setter to update the vans curr node to the next node.
    public void setCurrNode(Node currNode) {
        this.currNode = currNode;
    }
}
