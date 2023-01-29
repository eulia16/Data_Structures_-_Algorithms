
package ds2project;


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
        b.writeData(file, idToCategories);
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
         new FileOutputStream("/Users/ethan/Desktop/btree.ser");
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(b);
         out.close();
         fileOut.close();
         
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
    

