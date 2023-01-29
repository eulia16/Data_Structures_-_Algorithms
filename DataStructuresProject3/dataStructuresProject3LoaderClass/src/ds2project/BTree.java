
package ds2project;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import static java.lang.System.exit;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class BTree implements Serializable{
    
    
//the maximum number of keys in a node
//Node class that will make up the nodes of the BTree
private static int MAXK;

//this is the root node
private Node root;

public static final class Node implements Serializable{
int numKeys;//or "order"
//MAXK is the maxiimum degree in which each node can hold keys
//The objects the keys hold
String[] key = new String[2 * MAXK -1];
//the children the node has(always can have one more child than the key)
Node[] child = new Node[2 * MAXK];
//if the node is a leaf or internal node
boolean leaf = true;

}

//constructor for BTree
public BTree(int degree){
MAXK = degree;
root = new Node();
root.numKeys = 0;
root.leaf = true;
}
public void writeData(String fileName, Map<String, String> idToCategories, Map<String, Double> idToLong, Map<String, Double>idToLat, Map<String, Double> idToStars) throws FileNotFoundException, IOException{
    if(idToCategories.get(fileName) != null){
        RandomAccessFile reader = new RandomAccessFile("/Users/ethan/Desktop/project3DataStructuresFiles/" + fileName, "rw");
        FileChannel channel = reader.getChannel();
        ByteBuffer buff = ByteBuffer.wrap("Categories:".getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap(idToCategories.get(fileName).getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap("\nLongitude:".getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap(idToLong.get(fileName).toString().getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap("\nLatitude:".getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap(idToLat.get(fileName).toString().getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap("\nStars:".getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap(idToStars.get(fileName).toString().getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        buff = ByteBuffer.wrap("\nIsOpen:".getBytes(StandardCharsets.UTF_8));
        channel.write(buff);
        reader.close();
}
}

public String readData(String fileName) throws FileNotFoundException, IOException{
        String categories;
        ByteBuffer test;
        RandomAccessFile reader = new RandomAccessFile("/Users/ethan/Desktop/project3DataStructuresFiles/" + fileName, "rw");
        FileChannel channel = reader.getChannel();
        long fileSize = channel.size();
        ByteBuffer buff = ByteBuffer.allocate((int)fileSize);
        channel.read(buff);
        categories = new String(buff.array(), StandardCharsets.UTF_8);
        //System.out.println(categories);
        channel.close();
        reader.close();
        return categories;
}

public boolean Contains(String key) throws IOException{
    //readData(key);
    return Contains(key, root);
}
private boolean Contains(String key, Node x){
    boolean ans=false;
    if(x != null){
        for(int i=0;i < x.numKeys; ++i){
            if(x.key[i].equalsIgnoreCase(key)){
           
            return true;
        }
           
        }
            if(!x.leaf){
            for(int i=0;i<x.numKeys+1; ++i){
                ans = Contains(key, x.child[i]);
                if(ans == true)
                     return ans;
            }
            }
        
    }
    return false;
}
public void print(){
print(root);
}

// Display the tree
private void print(Node x) {
if(x != null){
    for (int i = 0; i < x.numKeys; i++) {
        System.out.println(x.key[i] + ", i: " + i );
        System.out.println("isLeaf: " + x.leaf);
        //return;
}
    if (!x.leaf) {
      for (int i = 0; i < x.numKeys + 1; i++) {
        print(x.child[i]);
      }
    }
  }
}

private void split(Node x, int pos, Node y) {
    Node z = new Node();
    z.leaf = y.leaf;
    z.numKeys = MAXK - 1;
    for (int j = 0; j < MAXK - 1; j++) {
      z.key[j] = y.key[j + MAXK];
    }
    if (!y.leaf) {
      for (int j = 0; j < MAXK; j++) {
        z.child[j] = y.child[j + MAXK];
      }
    }
    y.numKeys = MAXK - 1;
    for (int j = x.numKeys; j >= pos + 1; j--) {
      x.child[j + 1] = x.child[j];
    }
    x.child[pos + 1] = z;

    for (int j = x.numKeys - 1; j >= pos; j--) {
      x.key[j + 1] = x.key[j];
    }
    x.key[pos] = y.key[MAXK - 1];
    x.numKeys = x.numKeys + 1;
  }


//This allows the insertion of a key, but calls an overloaded method to insert a node if necessary
public void insert(String key) {
    Node temp = root;
    if (temp.numKeys == 2 * MAXK - 1) {
      Node k = new Node();
      root = k;
      k.leaf = false;
      k.numKeys = 0;
      k.child[0] = temp;
      split(k, 0, temp);
      insert(k, key);
    } else {
      insert(temp, key);
    }
  }

    
    
private void insert(Node x, String k) {

    if (x.leaf) {
      int i;
      //We changed this one to compare to rather than k <x.key[i]

        for (i = x.numKeys - 1; i >= 0 && k.compareTo(x.key[i]) <=0; i--) {
        x.key[i + 1] = x.key[i];
      }
      x.key[i + 1] = k;
      x.numKeys = x.numKeys + 1;
    }
    else {
      int i;
      for (i = x.numKeys - 1; i >= 0 && k.compareTo(x.key[i]) <=0; i--) {
      };
      i++;
      Node tmp = x.child[i];
      if (tmp.numKeys == 2 * MAXK - 1) {
        split(x, i, tmp);
        if (k.compareTo(x.key[i]) <= 0) {
          i++;
        }
      }
      insert(x.child[i], k);
    }

  }


}    

    

