package app;
import structures.*;
import java.io.IOException;

/* Check links for the answers
 * Graph 3 Answer: https://tinyurl.com/MSTGraph3, Works
 * Forgot answer to Graph 4
 * Graph 5: https://tinyurl.com/MSTGraph5, Works
 * Graph 6: https://tinyurl.com/MSTGraph6, Works
 * Graph 7: https://tinyurl.com/MSTGraph7, Works
 */
public class Driver {
	public static void main(String[]args) throws IOException {
		Graph woah = new Graph ("graph3.txt");
		PartialTreeList xd = PartialTreeList.initialize(woah);
		/*System.out.println("These are the PartialTrees for initialize:");
		for(PartialTree a: xd) {
			System.out.println(a);
		}
		Vertex d = new Vertex("A");
		PartialTree test = xd.removeTreeContaining(d);
		for(Arc x: test.getArcs()) {
			System.out.print(x + " ");
		}
		System.out.println();
		for(PartialTree a: xd) {
			System.out.print(a.getRoot());
			System.out.print(" ");
			for(Arc x: a.getArcs()) {
				System.out.print(x + " ");
			}
			System.out.println();
		}*/
		
		System.out.println("These are the minimum spanning tree arcs:");
		for(Arc x: PartialTreeList.execute(xd)) {
			System.out.println(x);
		}
	}
}