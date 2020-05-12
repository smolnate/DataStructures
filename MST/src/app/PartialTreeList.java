package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.PartialTree;
import structures.Vertex;
import structures.MinHeap;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
	public static PartialTreeList initialize(Graph graph) {
		PartialTreeList L = new PartialTreeList();
		for(int i = 0; i < graph.vertices.length; i++) {
			PartialTree vertTree = new PartialTree(graph.vertices[i]);
			Vertex.Neighbor ptr = graph.vertices[i].neighbors;
			while(ptr != null) {
				Arc yert = new Arc(graph.vertices[i].getRoot(), ptr.vertex, ptr.weight);
				vertTree.getArcs().insert(yert);
				ptr = ptr.next;
			}
			L.append(vertTree);
			
		}		
		return L;
	}
	
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
	public static ArrayList<Arc> execute(PartialTreeList ptlist) {
		ArrayList<Arc> MST = new ArrayList<Arc>();
		while(ptlist.size()>1) {
			PartialTree PTX = ptlist.remove(); //step 3
			//System.out.println("PTX beginning: "+PTX);
			MinHeap<Arc> PQX = PTX.getArcs();
			//System.out.println("PQX: "+PQX);
			Arc alpha = PQX.deleteMin(); //step 4
			while(alpha.getv1().getRoot().equals(alpha.getv2().getRoot())) {//step 5	
				alpha = PQX.deleteMin();
			}
			//System.out.println(alpha+" is a component of MST");
			MST.add(alpha); //step 6
			PartialTree PTY = ptlist.removeTreeContaining(alpha.getv2()); //step 7
			//System.out.println("PTY: "+PTY);
			PTX.merge(PTY);
			//System.out.println("PTX merged with PTY: "+PTX);
			ptlist.append(PTX);
		}
		
		return MST;
		
	}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    throws NoSuchElementException {
	    	PartialTree todelete = null;
	    	if(rear == null){
	    		throw new NoSuchElementException();
	    	}
	    	if(rear == rear.next) {
	    		if(containsVertex(rear.tree, vertex)) {
	    			todelete = rear.tree;
	    			rear = null;
	    			size--;
	    			return todelete;
	    		}
	    	}
	    	Node ptr = rear.next;
	    	Node prev = rear;
	    	do {
	    		if(containsVertex(ptr.tree, vertex)) {
	    			todelete = ptr.tree;
	    			size--;
	    			prev.next = ptr.next;
	    			if(ptr == rear) {
	    				rear = prev;
	    			}
	    			break;
	    		}
	    		prev = ptr;
	    		ptr = ptr.next;
	    	} while (prev != rear);
	    	if(todelete == null)
	    		throw new NoSuchElementException();
	    	return todelete;
     }
    
    private boolean containsVertex(PartialTree p, Vertex v) {
    	MinHeap<Arc> copy = new MinHeap<Arc>(p.getArcs());
    	while(!copy.isEmpty()) {
    		Arc temp = copy.deleteMin();
    		if(temp.getv1().name.equals(v.name)) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


