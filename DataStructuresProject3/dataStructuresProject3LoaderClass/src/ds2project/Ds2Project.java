
package ds2project;


import ds2project.Graph.Edge;
import ds2project.Graph.Node;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Ds2Project {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       // TODO code application logic here
    
    Map<String, String> nameToID = new HashMap<>();
    Map<String, String> idToName = new HashMap<>();
    Map<String, String> idToCategories = new HashMap<>();
    Map<String, Double> idToLong = new HashMap<>();
    Map<String, Double> idToLat = new HashMap<>();
    Map<String, Double> idToStars = new HashMap<>();
    Map<String, Long> idToIsOpen = new HashMap<>();
   
    JSONObject obj;
    int counter =0;
    
    // The name of the file to open.
    String fileName = "/Users/ethan/Desktop/hw1.json";
    // This will reference one line at a time
    String line = null;
    //This line of code takes in the business ID's and hashMaps them to the business names
    try {
        FileReader fileReader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        //add limit to 10000 businesses
        while((line = bufferedReader.readLine()) != null && counter <10000) {
            
            obj = (JSONObject) new JSONParser().parse(line);
            //This is where we would add the name and business id to the hashmap(not freq map this time)
            nameToID.put((String)obj.get("name"), (String)obj.get("business_id"));
            idToName.put((String)obj.get("business_id"), (String)obj.get("name"));
            //This will be what the hashmap contains)
            idToCategories.put((String)(obj.get("business_id")), (String)obj.get("categories"));
            idToLong.put((String)obj.get("business_id"), (Double)obj.get("longitude"));
            idToLat.put((String)obj.get("business_id"), (Double)obj.get("latitude"));
            idToStars.put((String)obj.get("business_id"), (Double)obj.get("stars"));
            idToIsOpen.put((String)obj.get("business_id"), (Long)obj.get("is_open"));
            counter++;
        }
    //we now have the data in respective hashmaps for the data we want to store into individual files 
    Set<String> set = new HashSet();
    set.addAll(nameToID.values());
    
    BTree b= new BTree(2); 
    
    for(String file : set){ 
        b.writeData(file, idToCategories, idToLong, idToLat, idToStars);
    }
    
    //we will now determine the four geographically closest businesses to each respective business
    //we will have to include the haversine formula and store the graph data sructure in this program
    //and then read the data from the serialized file that will be stored on the desktop
    

    //we will define an array to keep track of the businesses that are the 4 geographically closest to each
    //business
    
    
    //this is the max number of neighbors a node can have
    int numVertices=4;
    double[] distances = new double[4];
    String[] closestBusinesses = new String[4];
    //we need to make sure that the arrays arent initialized at 0 so we dont get an error
    for(int i=0;i<distances.length;++i){
        distances[i] = 100000;
    }
   
    Distance dist = new Distance();
    //Graph Graph = new Graph(numVertices);
    Graph graph = new Graph(numVertices);
    //create nodes for every business in the set
    for(String id: set){
        graph.addNode(id);
    }
    
    //for every business in the 10000 passed businesses
    for(String id: set){
        //must reset businesses distances and names
        for(int i=0;i<distances.length;++i){
        distances[i] = 100000;
        }
        //we will compare every business with every other business and determine what the 4 closest 
        //geographical neighbors are
        for(String compare : set){
            if(id != compare){
             double weight=0;
             weight = dist.HaversineDist(idToLat.get(id), idToLat.get(compare), idToLong.get(id), idToLong.get(compare));
             //System.out.println("weight: " + weight + ", counter: " + stupidCounter);
            //we will loop over each businesses closest neighbors array and see if the weight is lower,
            //if it is, change the values
            //this is where we will add the graph and then persist it
           for(int i=0; i<numVertices;++i){                
                if(weight < distances[i]){
                   distances[i] = weight;
                   //System.out.println("distances[i]: " + distances[i]);
                   closestBusinesses[i] = compare;
                   //System.out.println("closestBusinesses[i]: " + closestBusinesses[i]);
                    break;
                }
            }
          }
            //break;
    }
        
               // System.out.println("Business maybe? "+graph.getNode(id).getNodeID());
                for(int i=0;i<distances.length; ++i){
                graph.getNode(id).addEdge(graph.getNode(id), graph.getNode(closestBusinesses[i]), distances[i]);
            }
        
    }
    
//    for(String name : set){
//        System.out.println(graph.getNode(name).getEdges());
//    }
    
    //we will now try to implement the disjoint sets portion, first we will extract every nodes edges
    //this will allow us to construct the disjoint sets and determine the number of disjoint sets
   
    
    //now we have the hashmap of edges, we can pass that into the graphs disjoint sets
    //thingymabob to calculate the number of disjoint sets
    System.out.println("Before BFS");
    for(String id : set){
        graph.BFS(graph.getNode(id));
        break;
    }
    
    
    //for every string inside of the set of id's, add that to the BTree and let it sort them
    int c=0;
    for(String ids : set){
    b.insert(ids);
    c++;
    }
    //b.print();
    //write BTree out to a file to maintain persistence
    FileOutputStream fileOut =
         new FileOutputStream("/Users/ethan/Desktop/btreeP3.ser");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(b);
         out.close();
         fileOut.close();
         
     //write graph out to a file to persist it
     FileOutputStream persistGraph = new FileOutputStream("/Users/ethan/Desktop/graphP3.ser");
     ObjectOutputStream graphOut = new ObjectOutputStream(persistGraph);
     graphOut.writeObject(graph);
     out.close();
     persistGraph.close();
     
     
         
         //read BTree from file to access data
//         BTree tree;
//         FileInputStream fileIn = new FileInputStream("/Users/ethan/Desktop/btree.ser");
//         ObjectInputStream in = new ObjectInputStream(fileIn);
//         tree = (BTree) in.readObject();
//         in.close();
//         fileIn.close();

    }
    
        
        catch(IOException e){
                e.printStackTrace();
                }
        catch(ParseException e){
                e.printStackTrace();
                } 
    
    
    }}
    

