
package ds2application;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import ds2project.Graph;
import ds2project.BTree;
import ds2project.Graph.Edge;
import ds2project.Graph.Node;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Ds2Application extends JPanel{
    /**
     * This is Ethan Uliano's project 3 
     * This project firstly uses a loader class that loads JSON data from a file and populates hashmaps with the
     * data, then a BTree is constructed to contain pointers to files containing each businesses information 
     * respectively. After this, a graph is populated by using the Haversine Formula, in which it allows each business
     *(Node) to have 4 neighbors, each being the 4 geographically closest to that node out of every other business.
     * Then, using a breadth first search, each business maintains a set of nodes that it is connected to, allowing
     * it to show its connectivity with respect to the rest of the graph.
     * Then both the Btree and graph are persisted through a serialized file. Then the Application is created.
     * The application reads in the BTree and graph from the persisted files and also takes in the data from the
     * BTree nodes that point to each businesses information. After reading in every businesses information,
     * the graph's edges weights are then reconstructed on the basis of how similiarity(Tf-Idf)
     * instead of geographical distance. Then we allow the user, through the use of GUI, to select any random node
     * (business) and we determine which node in the selected business's connected nodes is the closest to any
     * of the computed centroids. After this, the shortest path from the node the user selected to the closest
     * node to a centroid is calculated, and the user is presented with the list of businesses that make up
     * the shortest path to the node closest to the centroid
     * 
     * Thank you for a great semester Professor Lea!
     * Have a great summer :) 
     * 
     * 
     * 
     */

   
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, ParseException {
    Scanner scan;
    JSONObject obj;
    int counter =0;
    Map<String, String> idToCategories = new HashMap<>(); 
    Map<String, String> idToLong = new HashMap<>();
    Map<String, String> idToLat= new HashMap<>(); 
    Map<String, Double> idToStars = new HashMap<>();
    
    
    // The name of the file to open.
    String fileName = "/Users/ethan/Desktop/hw1.json";
    // This will reference one line at a time
    String line = null;

       //This is the application side of the program, we will de-serialize the BTree, 
       //calculate the tf-idf value of each respective business in respect to each centroid,
       //calculate the haversine distance for each business using their long and latitude, 
       //and then compare each business' star rating and overall compare the euclidean distance 
       //of each business and see which businesses are most similiar. Remember we first will choose 5-10 random
       //centroids and then compare all other businesses to those centroids, and then, using euclidean
       //distance, determine which businesses belong in which cluster
         
         //we first must read the tree in from the serialized file
         BTree tree;
         FileInputStream fileIn = new FileInputStream("/Users/ethan/Desktop/btreeP3.ser");
         BufferedInputStream bis = new BufferedInputStream(fileIn);
         ObjectInputStream in = new ObjectInputStream(bis);
         tree = (BTree) in.readObject();
         in.close();
         fileIn.close();
         //we then will read the graoh in from the serialized file
         Graph graph;
         FileInputStream graphIn = new FileInputStream("/Users/ethan/Desktop/graphP3.ser");
         BufferedInputStream gis = new BufferedInputStream(graphIn);
         ObjectInputStream input = new ObjectInputStream(gis);
         graph = (Graph) input.readObject();
         input.close();
         graphIn.close();
         
         
         String category="", result="";
         int pFrom=0, pTo=0;
         double ans=0;
         //now that we have the tree, we must retrieve the names of all the files that are in the tree
         try {
        FileReader fileReader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        //add limit to 10000 businesses
        //load all the data into soem hashmaps
        while((line = bufferedReader.readLine()) != null && counter <1000) {
            obj = (JSONObject) new JSONParser().parse(line);
            
            if(tree.Contains((String)obj.get("business_id"))){
                //read the data from the tree into memory
              // if(pFrom != -1 && pTo != -1){
               category = tree.readData((String)obj.get("business_id"));

              //we now parse the data from the tree into respective hashmaps
               pFrom = category.indexOf("Categories:")+"Categories:".length();
               pTo = category.lastIndexOf("\nLongitude:");
               
               if(pFrom != -1 && pTo != -1){
               result  = category.substring(pFrom, pTo);
               idToCategories.put((String)obj.get("business_id"), result);
               }
              //we also need to take in the long and latitude data from the tree
               pFrom = category.indexOf("Longitude:")+"Longitude:".length();
               pTo = category.lastIndexOf("\nLatitude");
               
               if(pFrom != -1 && pTo != -1){
               result  = category.substring(pFrom, pTo);
               idToLong.put((String)obj.get("business_id"), result);
               }
               
               pFrom = category.indexOf("Latitude:")+"Latitude:".length();
               pTo = category.lastIndexOf("\nStars");
               
                if(pFrom != -1 && pTo != -1){
               result  = category.substring(pFrom, pTo);
               idToLat.put((String)obj.get("business_id"), result);
               }
                
            counter++;
           // }
            }
            }
        
        //we get all the data and put it into the hashmap
        
        
        Set<String> set = new HashSet<>();
        set.addAll(idToCategories.keySet());
        
//       for(String l : set){
//            System.out.println("Categories: "+ idToCategories.get(l));
//            System.out.println("Longitude: " + idToLong.get(l));
//            System.out.println("Latitude: " + idToLat.get(l));
//
//       }
       
       
       //now that all the data is being read properly
       //we now will perform the proper tf-idf calculations for each respective business
       
       //for the top section of the assignment we will use the haversine distance as the weights 
       //for the edges, and then for the bottom half we will use dijkstras to calculate the shortest
       //distance to similiar businesses in a set(the weights for that graph will be tf-idf)
       
       
                
        
        
        
        final int finalCounter = counter;
        bufferedReader.close();
      
        //create the frequency table for tf
        FrequencyTable[] tf = new FrequencyTable[counter];
        //initialize each tf
        for(int i=0; i<tf.length;++i){
            //initialize the frequencytables
            tf[i] = new FrequencyTable();
        }
        
        
        int m=0;
        //make a collection of all of the values from nameToID
        Set<String> values = new HashSet<>();
        values.addAll(idToCategories.keySet());
        //List<String> values = nameToID.values();
        HashMap<String, Integer> idToLoc = new HashMap<>();
        //for how every many businesses were parsed
        for(String k : values){
        //we need to get their categories and parse them 
            String businessesID = k;
            String getCategories = idToCategories.get(businessesID);
            if(getCategories != null){
            String[] split = getCategories.split(", ");
            for(String string : split){
            //we must add each word to our frequency table and then allow it to calculate the term frequency
            idToLoc.put(businessesID, m);
            tf[m].add(string);
           
        }
        }
        ++m;
        }
       
        //we create the FrequencyTable for idf
        FrequencyTable idf = new FrequencyTable();
        //calculate the idf for all of the words in the document and how often they occur
        String allStrings;
        //for every string that is in idToCategories, we will add each String from the hashmap to get how often
        //a word occurs overall in the document
        for(String keys: idToCategories.keySet()){
        allStrings = idToCategories.get(keys);
        //System.out.println("\nString of categories: " + allStrings);
        if(allStrings != null){
        String[] parser = allStrings.split(", ");
        for(String aString : parser){
                idf.add(aString);
            }
        }
        }
        
        String[] randomBusinesses;
        selectRandomBusiness random = new selectRandomBusiness(5);
        //randomBusinesses = set.toArray(new String[0]);
        
        //String [] centroids = random.randomBusiness(counter, randomBusinesses);
        double[] numLowTfIdf = new double[5 * 10];
        String[] businessIDS = new String[5 * 10];
        int a=0;
        double[] data = new double[10000];
        int q=0;
        //for a random amount of times(10 for now), we will compute the randomly selected businesses, and
        //see which ones are the best for the clusters
        //for(int k=0;k<10;++k){
            
        //selectRandomBusiness random = new selectRandomBusiness(5);
        randomBusinesses = set.toArray(new String[0]);
        
        String [] centroids = random.randomBusiness(counter, randomBusinesses);    
                
        System.out.println("centrouds.length: " + centroids.length);        
        //for each random business, we will compute the distances of every other business to that respective 
        //business(tf-idf value)
        for(int l=0;l<centroids.length;++l){
                    //System.out.println("random Businesses: " + centroids[l]);

        int numLines=0;
        String busID = centroids[l];
        
        int numZeroes=0;
        int j=0;
       
            String categories = idToCategories.get(busID);
        
            //lets calculate the tfidf value of the business just entered
            TfIdf tfIdf = new TfIdf();
            //we must split the categories of the business entered to evaluate the 
            //tfidf value of each individual word
            String[] splits = categories.split(", ");
            //we now must check every business to compare the category of the business entered
            //we have a collection if all the businesses already defined so we'll use that
            for(String s : values){
            //we define the tfidf value outside of the for each word loop because we want it to reset only
            //when every word in the respective businesses category has been looped over
            double tfIdfValue = 0;
            //we now have every business)thei id's, and will use that to get their categories,
            //and compare their values to the category of the business entered
            //get the businesses ID and set it to a relevant variable
            String businessID = s;
           
            //we now should get each businesses categories descriptions and then
            //comparare each split category descriptor from the entered business
            String busCategories = idToCategories.get(businessID);
            //for every word in the split category variable, we will get the tf, idf, and tfidf value
            //from every business
            if(splits != null && busCategories != null){
            for(String first : splits){
                //get tf value from the current business, its respective categories, and the string in split
                double Value = tfIdf.tf(tf[idToLoc.get(businessID)], busCategories, first);
                double newValue = tfIdf.idf(finalCounter, idf.frequency(first));
                
                tfIdfValue = tfIdfValue + tfIdf.tfIdf(Value, newValue);
                 

            }
                if(tfIdfValue != 0){
                data[q] = tfIdfValue;
                q++;
                }
                //if(tfIdfValue <.55)
                numZeroes++;
                //coordinates[j] = tfIdfValue;
                //j++;
                if(tfIdfValue >= .55 ){
                    //System.out.println("Recommended  business: " + businessID + ", Likeness: "+ String.format("%.3f", tfIdfValue));  
                    numLines++;
                }
                //if more than 15 businesses are shown, we will not display anymore(we dont want to 
                //overwhelm the user with information
                if(numLines >= 15){
                    break;
                }
            }
        }
            //after all the businesses are comapred to one business, we will add the busID and the number of 0's
            //to parallel arrays to keep track of the best ones
            
            numLowTfIdf[a] = numZeroes;
            businessIDS[a] = centroids[l];
            a++;
        }
        
        
    //we shall select 5 random points out of the dataPoints set
        Mediod med = new Mediod(5, q);
        //create a centroid of the amount of random points we will select
        double [][] centroid = new double[2][5];
        for(int j=0;j<centroid[1].length;++j){
            centroid[0][j] = 0;
            centroid[1][j] = med.randomMediods(data);
        }
        
       
        
        for(double j : centroid[1]){
        System.out.println("random centroid: " + j);
        }
        //double[][] finalCentroids;
        //finalCentroids =
        
       //we will now compute the tf-idf value for every business' neighbor to
       //make that the weight between each business, and then we will use dijkstras alg
       //to determine the shortest path from a selected node to the nearest cluster center
       
       //we first have to change the weight of each edge to the tf-idf value
       //for every business
       for(String id : set){
           //reset the tfIdf value each iteration
           double tfIdfValue=0;
           //we will get its node, and then get its edges, and compute its tf-idf value compared to its neighbors
           //we get this businesses categories
           String thisBusinessesCategories = idToCategories.get(id);
           //make a tfidf object
           TfIdf tfIdf = new TfIdf();
           //split thisbusinesses categories
           String[] splits = thisBusinessesCategories.split(", ");
           //we must now compare each neighbor with that of the current business
           for(Edge e : graph.getNode(id).getEdgesUF()){
               //we now compute the tf-idf value of each edge compared to the business
               String edgesCategories = idToCategories.get(e.getDestination());
               //for each word in the original businesses categories
               if(splits != null && edgesCategories != null){
               for(String first : splits){
                //get tf value from the current business, its respective categories, and the string in split
                double Value = tfIdf.tf(tf[idToLoc.get(e.getDestination())], edgesCategories, first);
                double newValue = tfIdf.idf(finalCounter, idf.frequency(first));
                
                tfIdfValue = tfIdfValue + tfIdf.tfIdf(Value, newValue);
                 
            }
               }
               //for each edge we will change its weight to that if the tfIdf value
               //if(tfIdfValue != 0)
               e.overWriteWeight(graph.getNode(id), graph.getNode(e.getDestination()), tfIdfValue);
           }
       }
        
       //now that we have the tf-idf values of the cluster centers, we just need to compare the selected
       //businesses(from the user) with each centroid, 
       double[][] clusters = med.getCentroid(data, centroid);
        
       System.out.println("*********************************************************************");
        for(int i=0;i<5;++i){
            System.out.println("Cluster "+ (i+1)+ ": " + clusters[1][i]);
        }
        
        String[] names = new String[counter];
        int o=0;
        for(String name : set){
            names[o] = name;
            o++;
        }
        
        
        final JTextArea textArea = new JTextArea(20, 50);
        //SelectedBusiness sb = new SelectedBusiness();
    
    
        //make frame to get input and present recommended businesses(output)
        JFrame frame = new JFrame("Shortest Distance From Centroid");
        frame.setSize(700, 500);
        frame.setLocation(300, 200);
         JPanel panel1 = new JPanel();
         JLabel label1 = new JLabel("Select a business: ");
         //label1.setBounds(0, 0, 200, 50);
         JPanel panel2 =new JPanel();
         JScrollPane output = new JScrollPane(textArea);
         
        final JList list1 = new JList(names);
        list1.setSelectedIndex(1);
        list1.setVisibleRowCount(3);
       
        list1.setFixedCellWidth(400);
        list1.setFixedCellHeight(25);

        JScrollPane scrollPanel1 = new JScrollPane(list1);

        //SelectedBusiness selectedBusiness = new SelectedBusiness();
        JButton button = new JButton("Click me!");
        
        button.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e){ 
            //get the string selected by the user
            String selectedBusiness = String.valueOf(list1.getSelectedValue());
            //we will run the BFS for the given business to determine its set
            graph.BFS(graph.getNode(selectedBusiness));//run BFS to get the 
            //get the node that is the closest to one of the centroids
            Node closestNodeToCentroid = graph.getClosestNode(clusters, graph.getNode(selectedBusiness).getNodeSet(), graph.getNode(selectedBusiness));
            System.out.println("node closest to centroid: " + closestNodeToCentroid.getNodeID());
            //System.out.println("Node set: " + graph.getNode(selectedBusiness).getNodeSet());
            Node[] path;
            path = new Node[100];
            path = graph.ShortestPath(graph.getNode(selectedBusiness), closestNodeToCentroid, graph.getNode(selectedBusiness).getNodeSet());
            
            Set<Node> toTextArea = new HashSet<>();
            //we then will toss the centroid we received from the closestNodeToCentroid and the node 
            //the user selected and run dijkstras to find the shortest path from that node to the node near the centroid
            for(Node n : path){
                toTextArea.add(n);
            }
            //textArea.append(selectedBusiness + "\n");
            textArea.append("Path To Centroid\n");
            for(Node n : toTextArea){
            if(n != null)
            textArea.append("-> " + n.getNodeID() + "\n");
            }
            }
        });
        
        panel1.add(scrollPanel1);
        panel1.add(button);
        //panel1.add(button2);
        panel2.add(output);
     
    //add panels to the frame
        frame.add(panel1);
        frame.add(panel2);
     //split the frame to distinguish between entered stuff and output
        JSplitPane split = new JSplitPane(SwingConstants.HORIZONTAL, panel1, panel2);
        
        //add split to frame
        frame.add(split);
        //just some stuff to make you be able to see the the frame, exit it etc.
        frame.setVisible(true);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        
        }
        catch(IOException e){
                e.printStackTrace();
                } catch (ParseException ex) {
            Logger.getLogger(Ds2Application.class.getName()).log(Level.SEVERE, null, ex);
        }
            
         
      
    }
    
}


























        
        
        //now that we have the distances computed, we need to determine which one is the lowest 
        //compared to the other clusters(lowest value per cluster
        
        //-that is also handled inside of the distFromMediods method, move onto the next stage...
        
        
        //eachCategory is all of out points
//        //Select 5 random slots in the
//        mediods mediods = new mediods(5, eachCategory.length);
//        double[] candidates = new double[5];
//        for(int j=0;j<candidates.length;++j){
//            candidates[j] = mediods.randomMediods();
//        }
//        
//        for(double k : candidates){
//        System.out.println("candidates: " + k);
//        }
        
        
//       System.out.println("Number of tf-Idf values: " + l);
//       int e=0;
//       for(int j=0;j<bestBus.length; ++j){
//           System.out.println("THIS IS A NEW SLOT IN THE ARRAY");
//           for(double d : tfBusValues[j]){
//               //System.out.println("Data: " + d + ", slot in iteration: " + e);
//               e++;
//           }
//           
//       }
//       System.out.println("number of iterations: " + e);
       
       
       
       
       //now we have each respective groups tf-idf values and how similiar each respective business is
       //we now will compare each level of the 2d array, and for every row, whichever number is the highest
       //(if its zero just let it be), we will assign that to each respective group
//       double[] group1, group2, group3, group4, group5;
//       int g1Counter=0, g2Counter=0, g3Counter=0, g4Counter=0, g5Counter=0;
//       group1=new double[counter];
//       group2=new double[counter];
//       group3=new double[counter];
//       group4=new double[counter];
//       group5=new double[counter];
//       
//       
//       //for(int k=0;k<bestBus.length;++k){
//           for(int j=0;j<counter;++j){
//               if(tfBusValues[0][j] > tfBusValues[1][j] && tfBusValues[0][j] > tfBusValues[2][j] && 
//                       tfBusValues[0][j] > tfBusValues[3][j] && tfBusValues[0][j] > tfBusValues[4][j]){
//                   group1[g1Counter] = tfBusValues[0][j];
//                   g1Counter++;
//               }
//               if(tfBusValues[1][j] > tfBusValues[0][j] && tfBusValues[1][j] > tfBusValues[2][j] && 
//                       tfBusValues[1][j] > tfBusValues[3][j] && tfBusValues[1][j] > tfBusValues[4][j]){
//                   group2[g2Counter] = tfBusValues[1][j];
//                   g2Counter++;
//               }
//               if(tfBusValues[2][j] > tfBusValues[0][j] && tfBusValues[2][j] > tfBusValues[1][j] && 
//                       tfBusValues[2][j] > tfBusValues[3][j] && tfBusValues[2][j] > tfBusValues[4][j]){
//                   group3[g3Counter] = tfBusValues[2][j];
//                   g3Counter++;
//               }
//               if(tfBusValues[3][j] > tfBusValues[0][j] && tfBusValues[3][j] > tfBusValues[1][j] && 
//                       tfBusValues[3][j] > tfBusValues[2][j] && tfBusValues[3][j] > tfBusValues[4][j]){
//                   group4[g4Counter] = tfBusValues[3][j];
//                   g4Counter++;
//               }
//               if(tfBusValues[4][j] > tfBusValues[0][j] && tfBusValues[4][j] > tfBusValues[1][j] && 
//                       tfBusValues[4][j] > tfBusValues[2][j] && tfBusValues[4][j] > tfBusValues[3][j]){
//                   group5[g5Counter] = tfBusValues[4][j];
//                   g5Counter++;
//               }
//               else
//                   continue;
//           }
//           
//           for(double d : group1){
//               System.out.println("group1: "+ d);
//           }
//           System.out.println("************************************BREAK POINT************************************");
//           for(double d : group2){
//               System.out.println("group2: "+ d);
//           }
//           System.out.println("************************************BREAK POINT************************************");
//
//           for(double d : group3){
//               System.out.println("group3: "+ d);
//           }
//           System.out.println("************************************BREAK POINT************************************");
//           
//           for(double d : group4){
//               System.out.println("group4: "+ d);
//           }
//           System.out.println("************************************BREAK POINT************************************");
//           
//           for(double d : group5){
//               System.out.println("group5: "+ d);
//           }
           
//           
//              JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        GUI gui = new GUI();
//        
////        gui.setg1(group1);
////        gui.setg2(group2);
////        gui.setg3(group3);
////        gui.setg4(group4);
////        gui.setg5(group5);
//        gui.setXCoordinates(eachCategory);
//        frame.add(gui);
//        frame.setSize(1000,1000);
//        frame.setLocation(200,200);
//        frame.setVisible(true);
           
           
           
           
           
           
       //}
       
       
       //now that we have all of the tf-idf values for each respective 'best' business,
       //we will calculate which one is in which group(group 1-5, each will have different color)
       //we must iterate over the eachCategory array and in each slot(0,10000,20000,30000,40000,50000)
       //which one is the closest and assign that to whatever group slot it needs
       
       
     //  for(int j=0;j<10000;++j){
       
//       if(eachCategory[j] != 0){
//               //the higher the tf-idf value, the more similiar, so for every business, whichever
//               //it is most similiar to we will put it in that group
//              if(eachCategory[j] > eachCategory[j+l] && eachCategory[j] > eachCategory[j+2l]
//                      && eachCategory[j] > eachCategory[j+3l] && eachCategory[j] > eachCategory[j+4l]){
//                  group1[g1Counter] = eachCategory[j];
//                  g1Counter++;
//              }
//              if(eachCategory[j+l] > eachCategory[j] && eachCategory[j+l] > eachCategory[j+2l]
//                      && eachCategory[j+l] > eachCategory[j+3l] && eachCategory[j+l] > eachCategory[j+4l]){
//                  group2[g2Counter] = eachCategory[j];
//                  g2Counter++;
//              }
//              if(eachCategory[j+2l] > eachCategory[j] && eachCategory[j+2l] > eachCategory[j+l]
//                      && eachCategory[j+2l] > eachCategory[j+3l] && eachCategory[j+2l] > eachCategory[j+4l]){
//                  group3[g3Counter] = eachCategory[j];
//                  g3Counter++;
//              }
//              if(eachCategory[j+3l] > eachCategory[j] && eachCategory[j+3l] > eachCategory[j+2l]
//                      && eachCategory[j+3l] > eachCategory[j+l] && eachCategory[j+3l] > eachCategory[j+4l]){
//                  group4[g4Counter] = eachCategory[j];
//                  g4Counter++;
//              }
//              if(eachCategory[j+4l] > eachCategory[j] && eachCategory[j+4l] > eachCategory[j+2l]
//                      && eachCategory[j+4l] > eachCategory[j+3l] && eachCategory[j+4l] > eachCategory[j+l]){
//                  group5[g5Counter] = eachCategory[j];
//                  g5Counter++;
//              }
//           }
//       }
//       int co=0;
//       for(double d : eachCategory){
//           System.out.println("data inside eachCategory: "+ d);
//           if(d!= 0)
//               co++;
//       }
//        System.out.println("Co: " + co);
        
        
        
        
        
//        
//         JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        GUI gui = new GUI();
////        gui.setg1(group1);
////        gui.setg2(group2);
////        gui.setg3(group3);
////        gui.setg4(group4);
////        gui.setg5(group5);
//        gui.setXCoordinates(eachCategory);
//        frame.add(gui);
//        frame.setSize(1000,1000);
//        frame.setLocation(200,200);
//        frame.setVisible(true);
         
        
//        for(int k=0;k<10;k++){
//            
//            if(numLowTfIdf[k] < currentBest){
//                currentBest = numLowTfIdf[k];
//                //System.out.println("bestBusiness: " + currentBest);
//            }
//            for(int w=0;w<leastNumZeroes.length;++w){
//                if(leastNumZeroes[w] > numLowTfIdf[k]){
//                leastNumZeroes[w] = numLowTfIdf[k];
//                bestBus[w] = businessIDS[k];
//                }
//                System.out.println(numLowTfIdf[k]);
//                System.out.println(businessIDS);
//                
//            }
//            
//            
//            System.out.println("NUMZEROES: "+numLowTfIdf[k]);
//                }
        
//            System.out.println("bestBusiness: " + currentBest);
//            for(int w=0;w<bestBus.length;++w){
//            System.out.println("bestBusiness: " + bestBus[w]);
//            System.out.println("leastNumZeroes: " + leastNumZeroes[w]);
//            }
            //so now we can calculate the tf-idf value of every business we want, we will now take the 5 random businesses
            //from the randombusinesses class, and compare those to every other business, then we will randomly choose
            //another 5 businesses. This is where the comparison takes place. If the number of businesses that contain
            //a 0 or very low tf-idf is less in the newer set of rando businesses, keep that business, if the newer random
            //business has more 0's or low tf-idf scores, we will toss it and maintain the top 5 businesses(businesses w/
            //the lowest resulting 0's/low tf-idf scores
            
            //this generates 5 random businesses
//            String [] centroids = random.randomBusiness(counter, randomBusinesses);
//        
//            for(int l=0;l<centroids.length;++l){
//                    System.out.println("random Businesses: " + centroids[l]);
//
//            }
            
            
         
//         JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        GUI gui = new GUI();
//        gui.setXCoordinates(coordinates);
//        //gui.setYCoordinates(bestY);
//        frame.add(gui);
//        frame.setSize(1000,1000);
//        frame.setLocation(200,200);
//        frame.setVisible(true);
//         
         
       
        
        
        
        
        
        
        
        
        
        
        
        
        
        //we now will calculate all of the tf-idf values for every word
        //This will be the input of the businesses in the GUI
        //we need to pass am array of strings that contain all of the
        
        //GUI gui = new GUI();
        //gui.makeGUI(names);
        
        
        
        //for every long/lat value of each business, we will compute its euclidean distance from each of the centroids
//        int S=0;
//        double[] xAxis = new double[counter * centroids.length];
//        for(String l : centroids){
//        for(int i=0;i<counter; ++i){
//            xAxis[i] = dist.HaversineDist(idToLong.get(l), idToLong.get(randomBusinesses[i]),idToLat.get(l),idToLat.get(randomBusinesses[i]));
//            //System.out.println(xAxis[i]);
//            S++;
//        }
//        }
//        
//        //now that we have the distance in respect to each random business, we must put the number of stars in the Y-axis
//        //to be able to calculate the similiarity
//        double[] yAxis = new double[counter * centroids.length];
//        int c=0;
//        for(String s : centroids){
//        for(int i=0;i<counter;++i){
//            yAxis[i] = idToStars.get(randomBusinesses[i]);
//            //System.out.println("y-axis: " + yAxis[i]);
//            c++;
//        }
//        }
//        double[] bestX = new double[counter];
//        double[] bestY = new double[counter];
//        //load 'bestX' with 100 data points for now
//        for(int i=0;i<counter;++i){
//            bestX[i] = xAxis[i];
//        }
//        for(int i=0; i<counter; ++i){
//            bestY[i] = yAxis[i];
//        }
//        
//          for(int i=0;i<counter;++i){
//         //System.out.println("BestX: " + bestX[i]);
//          }  
//        
//        
//        System.out.println("c " + c);
//        System.out.println("S " + S);
        
        
        
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        GUI gui = new GUI();
//        //gui.setXCoordinates(bestX);
//        //gui.setYCoordinates(bestY);
//        frame.add(gui);
//        frame.setSize(1000,1000);
//        frame.setLocation(200,200);
//        frame.setVisible(true);
////          
//            
//        
//        catch(IOException e){
//                e.printStackTrace();
//                } catch (ParseException ex) {
//            Logger.getLogger(Ds2Application.class.getName()).log(Level.SEVERE, null, ex);
//        }
//            
//         
//      
//    }
//    
//}
