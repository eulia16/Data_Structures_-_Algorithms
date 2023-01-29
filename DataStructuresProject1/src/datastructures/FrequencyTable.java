
package datastructures;

public class FrequencyTable {
    Node[] table = new Node[8192];//size that requires no resizes 8192
    int size = 0;
    static final class Node {
	Object key;
	Node next;
	int count;
       // Object value;
	Node(Object k, int c, Node n) { key = k; count = c; next = n; }
    }

    int frequency(Object key) {
	int h = key.hashCode();
	int i = h & (table.length - 1);
	for (Node e = table[i]; e != null; e = e.next) {
	    if (key.equals(e.key))
		return e.count;
	}
	return 0;
    }
    void add(Object key) {
	if ((float)size/table.length >= 0.7f){
            resize();
        }
        
        int h = key.hashCode();
	int i = h & (table.length - 1);
	for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.count++;
                return;
            }
	}
        
        table[i] = new Node(key, 1, table[i]);
	++size;
    }
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
		table[j] = new Node(e.key, e.count, table[j]);
                
	    }
	}
	table = newTable;
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

}
