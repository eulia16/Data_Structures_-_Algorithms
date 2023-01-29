
package ds2application;

import ds2project.BTree;

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



public class Ds2Application extends JPanel{

   
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
    Scanner scan;
    JSONObject obj;
    int counter =0;
    Map<String, String> idToCategories = new HashMap<>(); 
    Map<String, Double> idToLong = new HashMap<>();
    Map<String, Double> idToLat= new HashMap<>(); 
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
         FileInputStream fileIn = new FileInputStream("/Users/ethan/Desktop/btree.ser");
         BufferedInputStream bis = new BufferedInputStream(fileIn);
         ObjectInputStream in = new ObjectInputStream(bis);
         tree = (BTree) in.readObject();
         in.close();
         fileIn.close();
         String category="", result="";
         int pFrom=0, pTo=0;
         double ans=0;
         //now that we have the tree, we must retrieve the names of all the files that are in the tree
         try {
        FileReader fileReader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        //add limit to 10000 businesses
        while((line = bufferedReader.readLine()) != null && counter <10000) {
            obj = (JSONObject) new JSONParser().parse(line);
            
            if(tree.Contains((String)obj.get("business_id"))){
                //read the data from the tree into memory
               if(pFrom != -1 && pTo != -1){
               category = tree.readData((String)obj.get("business_id"));

              //we now parse the data from the tree into respective hashmaps
               pFrom = category.indexOf("Categories:")+"Categories:".length();
               pTo = category.lastIndexOf("\nLongitude:");
               if(pFrom != -1 && pTo != -1){
               result  = category.substring(pFrom, pTo);
               idToCategories.put((String)obj.get("business_id"), result);
              
            counter++;
            }}}
        }
        //We now have all of the data read from the BTree from secondary storage, we now have all the data back
        //into memory, we will now compute the distances of each respective business from randomly selected 
        //clusters and then display which businesses are in which clusters
        Set<String> set = new HashSet<>();
        set.addAll(idToCategories.keySet());
       
        
        final int finalCounter = counter;
        bufferedReader.close();
      
        //create the frequency table for tf
        FrequencyTable[] tf = new FrequencyTable[counter];
        //initialize each tf
        for(int i=0; i<tf.length;++i){
            //initialize the frequencytables
            tf[i] = new FrequencyTable();
        }
        
        
        int i=0;
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
            idToLoc.put(businessesID, i);
            tf[i].add(string);
           
        }
        }
        ++i;
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
        med.getCentroid(data, centroid);
        
//        double[] temp; //= new double[centroid.length];
//        //temp = centroid;
//       
//        //we now have 5 random starting points to compare all the other values to
//        //for(int j=0; j<10; j++){
//        //we need to pass in the data(tf-idf values, and a 2d array)
//        temp = mediod.distFromMediods(centroid, data);
//       
//        //}
//        for(double d : temp){
//            System.out.println("centroid: " +d);
//        }
//        //System.out.println("numData passed in: " + );
//        
//        
//        System.out.println("number of iterations: " + q);






        
//        for(int j=0;j<counter;++j){
//            System.out.println("data: " + data[j]);
//        }
        
        
        
        //now that we have all of the stats on each respectvive random business we checked(we only did 10 rn 
        //for times sake) we will see which ones were the 'best'(had least amt of zeroes)
//        String[] bestBus = new String[5];
//        double[] leastNumZeroes = new double[5];
//        double currentBest=10000;
//        for(int k=0;k<leastNumZeroes.length;++k){
//            leastNumZeroes[k] = currentBest;
//        }
//        System.out.println("***********************************************************************************");
//
//        //for every business we first will make sure the data was loaded properly into them
//        for(int k=0;k<businessIDS.length;++k){
//            for(int l=0;l<bestBus.length;++l){
//            if(numLowTfIdf[k] < leastNumZeroes[l]){
//                
//                leastNumZeroes[l] = numLowTfIdf[k];
//                bestBus[l] = businessIDS[k];
//                break;
//                
//            }
////            System.out.println("tf-idf values: " + numLowTfIdf[k]);
////            System.out.println("business IDS: " + businessIDS[k]);
//
//            }
//            
//        }
//        
//        System.out.println("***********************************************************************************");
//        //Now we have the top 5 businesses with the most businesses that are similiar to it
////        for(int k=0;k<leastNumZeroes.length;++k){
////            System.out.println("Lowest Zeroes " + leastNumZeroes[k]);
////            System.out.println("business IDS: " + bestBus[k]);
////        }
//        
//        
//        
//        
//        //we need to fogure out how to get all of the tf-idf values from each of the top 5 businesses(bestBus[k])
//        //and plot those to the graph with each respective color
//        
//        //we will now compute the tf-idf values of each respective business compared to every other business
//        //and then add them to respective groups, each being a different color
//        
//        //we need to get all of the values from each respective business, and then for each slot calculate
//        //which is the best option
//        
//        //we will make a large array to keep track of the tf-idf values for each respective business
//        double[] eachCategory = new double[bestBus.length * counter +1];//bestBus.length * counter +1];
//        double[] dataPoints = new double[bestBus.length * counter];
//        int l=0, e=0;
//        
//        
//        //**********88**********************
//        
//        
//        //UYVWDOUBOWUDBUWB WE LEFT OFF HERE TRYING TO MAKE AN ARRAY OR DOUBLE ARRAYS TO KEEP TRACK
//        //OF EACH RESPECTIVE BUSINESSES TF_IDF VALUE
//        
//        
//        
//        
//        
//        //FrequencyTable[] tf = new FrequencyTable[counter];
//        double [][] tfBusValues = new double[bestBus.length][10000];
//        int c =0;
////        for(int k=0;k<bestBus.length;++k){
////            tfBusValues = new double[][] 
////        }        
//        for(int k=0;k<bestBus.length;++k){
//            c=0;
//             String categories = idToCategories.get(bestBus[k]);
//        
//            //lets calculate the tfidf value of the business just entered
//            TfIdf tfIdf = new TfIdf();
//            //we must split the categories of the business entered to evaluate the 
//            //tfidf value of each individual word
//            String[] categorySplitter = categories.split(", ");
//            for(String s : values){
//            //we define the tfidf value outside of the for each word loop because we want it to reset only
//            //when every word in the respective businesses category has been looped over
//            double tfIdfValue = 0;
//            //we now have every business)thei id's, and will use that to get their categories,
//            //and compare their values to the category of the business entered
//            //get the businesses ID and set it to a relevant variable
//            String businessID = s;
//           
//            //we now should get each businesses categories descriptions and then
//            //comparare each split category descriptor from the entered business
//            String busCategories = idToCategories.get(businessID);
//            //for every word in the split category variable, we will get the tf, idf, and tfidf value
//            //from every business
//            if(categorySplitter != null && busCategories != null){
//            for(String first : categorySplitter){
//                //get tf value from the current business, its respective categories, and the string in split
//                double Value = tfIdf.tf(tf[idToLoc.get(businessID)], busCategories, first);
//                double newValue = tfIdf.idf(finalCounter, idf.frequency(first));
//                
//                tfIdfValue = tfIdfValue + tfIdf.tfIdf(Value, newValue);
//
//            }
//            if(tfIdfValue != 0){
//            dataPoints[e] = tfIdfValue;
//            e++;
//            }
//            //eachCategory[l] = tfIdfValue;
//            //tfBusValues[k][c] = tfIdfValue;
////            c++;
////            l++;   
//                
//                
//            }
//        }
//        }
//        
////        for(double d : dataPoints){
////            //if(d !=0)
////            System.out.println("data points: " + d + ", number of data points: " + e) ;
////        }
//        
//        //we shall select 5 random points out of the dataPoints set
//        mediods mediod = new mediods(5, e);
//        //create a centroid of the amount of random points we will select
//        double [] centroid = new double[5];
//        for(int j=0;j<centroid.length;++j){
//            centroid[j] = mediod.randomMediods(dataPoints);
//        }
//       
//        
//        for(double j : centroid){
//        System.out.println("random centroid: " + j);
//        }
//        
//        
//        double[] temp; //= new double[centroid.length];
//        //temp = centroid;
//       
//        //we now have 5 random starting points to compare all the other values to
//        //for(int j=0; j<10; j++){
//        temp = mediod.distFromMediods(centroid, dataPoints);
//       
//        //}
//        for(double d : temp){
//            System.out.println("centroid: " +d);
//        }
//        System.out.println("numData passed in: " + e);
//        
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
