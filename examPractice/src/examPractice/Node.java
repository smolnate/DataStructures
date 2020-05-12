package examPractice;

public class Node {
	public int data;
	public Node next;
	public Node(int data, Node next) {
		this.data = data;
		this.next = next;
	}

	public static Node commonElements(Node frontL1, Node frontL2) {
		Node L1ptr = frontL1;
		Node L2ptr = frontL2;
		Node match = new Node(0, null);
		Node matchptr = match;
		while(L1ptr != null && L2ptr != null) {
			if(L1ptr.data == L2ptr.data) {
				Node newNode = new Node(L1ptr.data, null);
				matchptr.next = newNode;
				matchptr = matchptr.next;
				L1ptr = L1ptr.next;
				L2ptr = L2ptr.next;
			}
			else if(L1ptr.data < L2ptr.data) {
				L1ptr = L1ptr.next;
			}
			else {
				L2ptr = L2ptr.next;
			}
		}
		return match.next;
	}
}
