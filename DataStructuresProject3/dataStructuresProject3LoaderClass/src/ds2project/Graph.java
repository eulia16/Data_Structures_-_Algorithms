
package ds2project;

import java.io.Serializable;
import java.util.*;
import java.lang.Math;
import static java.lang.Math.abs;
import static java.lang.System.exit;



public class Graph implements Serializable{
     
    
    private int MAXEDGES;
    private HashMap<String, Node> nodes = new HashMap<>();
    //this will store the sets and their connectivity(each nodes connectedness to the other nodes)
   
    
    public Graph(int numEdges){
        MAXEDGES = numEdges;
    }
    
    public void addNode(String id){
        Node node = new Node(id);
        nodes.putIfAbsent(id, node);
    }
    public Node getNode(String id){
        return nodes.get(id);
    }
    public void printNode(String id){
        System.out.println("Node: " + nodes.get(id).getNodeID() + "\n");
        
    }
    public Set<String> getNodes(){
        return nodes.keySet();
    }
    //unvisited every node
    public void resetVisited(){
        for(Node n : nodes.values()){
            n.unVisit();
        }
    }
//    public HashMap<Node, Object[]> getConnectedSet(){
//        return nodeSets;
//    }
     
    //method to perform a BFS from an arbitrary starting node
    //this determines which nodes are 'reachable' from an arbitrary starting node
    public void BFS(Node node){
        if(node == null)
            return;
        
        //create a queue and add the passed node to it
        Queue<Node> queue = new ArrayDeque();
        queue.add(node);
        
        //make a node to store as a temp to allow storage of connectedness
        Node temp = node;
        
        //we create a set to keep track of the already visited nodes
        Set<Node> alreadyVisited = new HashSet<>();
        //add the node to visited set
        alreadyVisited.add(node);
        
        int queueCounter=0;
        //while the queue isnt empty we will perform the visit operations
        while(!queue.isEmpty()){
            //System.out.println("Iteration: " + (++queueCounter));
            
            //get the node at the top of the queue
            Node currentNode = queue.remove();
            alreadyVisited.add(currentNode);
            //System.out.println("Removed Node: " + currentNode.nodeId);
            //for every neighbor the current node has, we will add it to the queue
            
            //we first will add each neighbor to the queue
            for(Edge s : currentNode.getEdgesUF()){
                //get each neighbor from the edges of the current node
                //add node to queue
                //if the node has not already been visited, add ti queue and visit
                if(!alreadyVisited.contains(getNode(s.getDestination()))){
                //System.out.println("Node being added to queue: " + s.getDestination());
                queue.add(getNode(s.getDestination()));
                
                //its visited
                alreadyVisited.add(getNode(s.getDestination()));
                }
            }
           //System.out.println("Queue size: " + queue.size());
           //System.out.println("AlreadyVisited: " + alreadyVisited.toString());
        }
        
        
        //if any node 
//        for(Node n : alreadyVisited)
             node.setNodeSet(temp, alreadyVisited);

    }
    
    //this will be a method to determine the closest node in an arbitrary nodes connected set
    //is to a centroid
    //returns the node that is the closest to the nearest reachable cluster(this will be the node that
    //is the destination, we pass in the potential centroids and the node the user selected
    public Node getClosestNode(double[][] centroids, HashMap<Node, Set<Node>> nodeSet, Node currentNode){
        double realCentroid[] = new double[5];
        for(int i=0;i<5;++i){
            realCentroid[i] = centroids[1][i];
        }
        
        Node closestNode = currentNode;
        double compareValue=100;
        //loop over every centroid available
        
            //loop over every member of the selected nodes nodeset
            int setCounter=0;
            for(Node n : nodeSet.get(currentNode)){
               // compareValue =100;
                //loop over all the edges of each node to get the tf-idf values
                for(Edge e : n.getEdgesUF()){
                    for(double d : realCentroid){
                    //of the weight of an edge is closer to a centroid than the previous value,
                    //set the compare value to that valie and the closest node to the current node
                    if(abs(e.weight-d) < compareValue){
                        //System.out.println("weight less than compare value");
                        compareValue = abs(e.weight-d);
                        closestNode = n;
                    }
                        
                   }
                }
                setCounter++;
            }
            //System.out.println("Num Nodes in set: " + setCounter);
        return closestNode;
    }
    //this will find the shortest path between the node given and the destination node
    public Node[] ShortestPath(Node source, Node destination, HashMap<Node, Set<Node>> nodeSet){
        Node temp = source;
        Node[] pathToDest = new Node[100];
        int pathCounter=0;
        Node bestPath = destination;
        
        //while we are not at the destination Node
        //while(temp.nodeId != destination.nodeId){
        for(Node m : nodeSet.get(source)){
        double weight = 100;
            
            //loop over each edge the node has, the lowest value is the best route
            for(Edge e : m.getEdgesUF()) {
                if(e.weight < weight && e.weight>0){
                    //System.out.println("Shorter path found, BestPathId: " + e.destination.nodeId + ", weight: " +e.weight);
                    bestPath = e.destination;
                    weight = e.weight;
                }
            }
            
            
            temp = bestPath;
            
            pathToDest[pathCounter]  = temp;
            pathCounter++;
            
        }
        
        
        return pathToDest;
    }
    

//an edge only knows about its source, its location, and its weight
    public static final class Edge implements Serializable{
       private Node source, destination;
       private double weight;
        public Edge(Node source, Node destination, double weight){
            this.source = source;
            this.destination=destination;
            this.weight = weight;
        }
        //this will be used when we calculate the tf-idf for each business
        public void overWriteWeight(Node source, Node destination, double weight){
            this.weight = weight;
        }
        public String getEdge(){
            String name= "";
            if(source != null && destination != null)
            name += source.nodeId + ", " + destination.nodeId + ", " + Double.toString(weight);
            return name;
        }
        
        public String getEdgeSourceDest(){
            return source + ", " + destination;
        }
        
        public String getDestination(){
            return destination.nodeId;
        }
    }
    
    //a node contains 4 edges, that being its four closest neigbhbors
    public static final class Node implements Serializable{
        private String nodeId;
        private boolean visited=false;
        
         private HashMap<Node, Set<Node>> nodeSets = new HashMap<>();
        //this array list will contain the four
        
        //need to somehow be able to add array of edges
        private Edge[] temp = new Edge[4];
        int numEntries=0;
        
        public void setNodeSet(Node id, Set<Node> Nodes){
            nodeSets.put(id, Nodes);
        }
        public HashMap<Node, Set<Node>> getNodeSet(){
            return nodeSets;
        }
        public Node(String id){
            nodeId = id;
        }
        public void addEdge(Node source, Node destination, double weight){
            Edge edge = new Edge(source, destination, weight);
            temp[numEntries] = edge;
            numEntries++;
        }
        
        public String getNodeID(){
            return nodeId;
        }
        public String getEdges(){
            String edge="";
            for(int i=0;i<temp.length;++i){
                if(temp[i] != null)
                edge += temp[i].getEdge() + "\n";
            }
            return edge;
        }
        //this method returns all the nodes edges(the array of edges)
        public Edge[] getEdgesUF(){
            return temp;
        }
        //for BFS
        public boolean visited(){
            return visited;
        }
        public void visit(){
            visited = true;
        }
        public void unVisit(){
            visited =false;
        }
    
    }
        
        
    }
