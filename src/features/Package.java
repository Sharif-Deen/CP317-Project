package features;

public class Package {
    private final String packageID; 
    private final Node destinationNode; 
    private final Van containedVan; 

    public Package(String packageID, Node destinationNode, Van containedVan) {
        this.packageID = packageID; 
        this.destinationNode = destinationNode; 
        this.containedVan = containedVan;
    }

    public String getPackageID() {
        return packageID; 
    }
    public Node getDestinationNode() {
        return destinationNode;
    }
    public Van getContainedVan() {
        return containedVan;
    }
}
