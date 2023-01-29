
package datastructures;



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

public class DataStructures{

    
    public static void main(String[] args) throws IOException, ParseException {
    
    //hashMaps for data
    Map<String, String> nameToID = new HashMap<>();
    Map<String, String> idToName = new HashMap<>();
    Map<String, String> idToCategories = new HashMap<>();
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
            counter++;
        }
        final int finalCounter = counter;
        bufferedReader.close();
        Scanner kbd = new Scanner(System.in);
      
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
        values.addAll(nameToID.values());
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
        //we now will calculate all of the tf-idf values for every word
        //This will be the input of the businesses in the GUI
        //we need to pass am array of strings that contain all of the
        String[] names = new String[counter];
        int k=0;
        for(String input: idToName.values()){
            names[k] = input;
            k++;
        }
        //GUI gui = new GUI();
        //gui.makeGUI(names);
        
        //final JTextField textField = new JTextField();
        final JTextArea textArea = new JTextArea(20, 50);
        SelectedBusiness sb = new SelectedBusiness();
    
    
        //make frame to get input and present recommended businesses(output)
        JFrame frame = new JFrame("Business Recommender");
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

        SelectedBusiness selectedBusiness = new SelectedBusiness();
        JButton button = new JButton("Click me!");
        //we need to make an action listener to allow us to get what business the
        //user selected and do some stuff with it
        button.addActionListener(new ActionListener(){  
        public void actionPerformed(ActionEvent e){ 
            int numLines=0;
            String name = String.valueOf(list1.getSelectedValue());
            selectedBusiness.setSelectedBusiness(name);
            String busID = nameToID.get(name);
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
                
                if(tfIdfValue >= 1 ){
                    textArea.append("Recommended  business: " + idToName.get(businessID) + ", Likeness: "+ String.format("%.3f", tfIdfValue)+"\n");  
                    numLines++;
                }
                //if more than 15 businesses are shown, we will not display anymore(we dont want to 
                //overwhelm the user with information
                if(numLines >= 15){
                    break;
                }
            
            }
            
            
        }}
    });  
        //we will make a reset button
        JButton button2 = new JButton("Reset");
        button2.addActionListener((new ActionListener(){  
        public void actionPerformed(ActionEvent e){  
        textArea.setText("");
        }
        }));
 
    //add all the components to the panels
        panel1.add(scrollPanel1);
        panel1.add(button);
        panel1.add(button2);
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
        
        //catch some potential errors
      
    }
    catch(FileNotFoundException e) {
        e.printStackTrace();
    }
    catch(IOException e) {
       e.printStackTrace();
    }
    catch (ParseException e) {
        e.printStackTrace();
    }
    }
    
 
          

    
}
