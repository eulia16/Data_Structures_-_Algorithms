
package ds2application;

public class FrequencyTable {
    Node[] table = new Node[16384];
    int size = 0;
    static final class Node {
	Object key;
	Node next;
	int count;
       // Object value;
	Node(Object k, int c, Node n) { key = k; count = c; next = n; }
    }
    //Node[] table = new Node[8];
    //int size = 0;
    //we must change this method to a frequency method(how often a word occurs
    int frequency(Object key) {
	int h = key.hashCode();
	int i = h & (table.length - 1);
	for (Node e = table[i]; e != null; e = e.next) {
	    if (key.equals(e.key))
		return e.count;
	}
	return 0;
    }
    //we must also add a to arrayList method, that puts elements into an arraylist, use the same
    //loop from resize
    void add(Object key) {
	if ((float)size/table.length >= 0.7f){
	    System.out.println("The table size has been reset!");
            resize();
        }
        
        int h = key.hashCode();
	int i = h & (table.length - 1);
	for (Node e = table[i]; e != null; e = e.next) {
	    //System.out.println("\nString key: " + e.key + ", key entered: " + key);
            if (key.equals(e.key)){
		//System.out.println("key entered: " + key + ", key matched with: " + e.key);
                e.count++;
                return;
            }
                //frequency(key);oaepsyvc0J17qwi8cfrOWg
                //e.count++;//when we change it make this call frequency to add 1 to the e.key
	}
        //if the word does not mathc any words in the frequency table, make a  new node with count =1 
//        if ((float)size/table.length >= 0.65f){
//	    System.out.println("The table size has been reset!");
//            resize();
//        }
        
        table[i] = new Node(key, 1, table[i]);
	++size;
    }
    //There is a problem w/ resize, it resets everyones count variable to 1, FIX THIS IMMEDIATLY
    void resize() {
	Node[] oldTable = table;
	int oldCapacity = oldTable.length;
	int newCapacity =  oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        table = newTable;
        for (int i = 0; i < oldCapacity; ++i) {
	    for (Node e = oldTable[i]; e != null; e = e.next) {
		int h = e.hashCode();
		int j = h & (newTable.length - 1);
                //System.out.println("e.key: " + e.key + ", e.count: " + e.count);
                //The count cariable isnt going into the new table
		table[j] = new Node(e.key, e.count, table[j]);
                System.out.println("newTable[j].count: " + newTable[j].count);
                //++size;
	    }
	}
        System.out.println("newTable size: " +newTable.length);
	//table = newTable;
    }
    void remove(Object key) {
	int h = key.hashCode();
	int i = h & (table.length - 1);
	Node e = table[i], p = null;
	while (e != null) {
	    if (key.equals(e.key)) {
		if (p == null)
		    table[i] = e.next;
		else
		    p.next = e.next;
		break;
	    }
	    p = e;
	    e = e.next;
	}
    }
//    
//    int print(Object key){
//        //int numOccurances=0;
//	int h = key.hashCode();
//	int i = h & (table.length - 1);
//	for (Node e = table[i]; e != null; e = e.next) {
//            ////System.out.println("stuff: " + e.key);
//            if(key.equals(e.key))
//                return e.count;
//        }
//   return 0;
//    }
}

