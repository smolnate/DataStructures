package lse;

import java.io.*;
import java.util.*;

public class driver 
{
	static Scanner sc = new Scanner(System.in);
	
	static String getOption() 
	{
		System.out.print("getKeyWord(): ");
		String response = sc.next();
		return response;
	}
	
	public static void main(String args[])
	{
		
		LittleSearchEngine lse = new LittleSearchEngine();
		try {
			lse.makeIndex("docstest2.txt", "noisewords.txt");
			System.out.println(lse.top5search("strange", "case"));
			ArrayList<Occurrence> occs = new ArrayList<Occurrence>();
			occs.add(new Occurrence("d1.txt", 82));
			occs.add(new Occurrence("d2.txt", 76));
			occs.add(new Occurrence("d3.txt", 71));
			occs.add(new Occurrence("d4.txt", 71));
			occs.add(new Occurrence("d5.txt", 70));
			occs.add(new Occurrence("d6.txt", 65));
			occs.add(new Occurrence("d7.txt", 61));
			occs.add(new Occurrence("d8.txt", 56));
			occs.add(new Occurrence("d9.txt", 54));
			occs.add(new Occurrence("d10.txt", 51));
			occs.add(new Occurrence("d11.txt", 48));
			occs.add(new Occurrence("d12.txt", 45));
			occs.add(new Occurrence("d13.txt", 41));
			occs.add(new Occurrence("d14.txt", 36));
			occs.add(new Occurrence("d15.txt", 34));
			occs.add(new Occurrence("d16.txt", 30));
			occs.add(new Occurrence("d17.txt", 25));
			occs.add(new Occurrence("d18.txt", 20));
			occs.add(new Occurrence("d19.txt", 20));
			occs.add(new Occurrence("d20.txt", 18));
			occs.add(new Occurrence("d21.txt", 17));
			occs.add(new Occurrence("d22.txt", 17));
			occs.add(new Occurrence("d23.txt", 14));
			occs.add(new Occurrence("d24.txt", 12));
			occs.add(new Occurrence("d25.txt", 17));
			System.out.println(lse.insertLastOccurrence(occs));
			System.out.println(occs);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Extreme left insertion 
Array: [(d1.txt,82), (d2.txt,76), (d3.txt,71), (d4.txt,71), (d5.txt,70), (d6.txt,65), (d7.txt,61), 
(d8.txt,56), (d9.txt,54), (d10.txt,51), (d11.txt,48), (d12.txt,45), (d13.txt,41), 
(d14.txt,36), (d15.txt,34), (d16.txt,30), (d17.txt,25), (d18.txt,20), (d19.txt,20), 
(d20.txt,18), (d21.txt,17), (d22.txt,17), (d23.txt,14), (d24.txt,12), (d25.txt,85)] 
Ans: [11, 5, 2, 0] - 3 

Extreme right insertion 
Array: [(d1.txt,82), (d2.txt,76), (d3.txt,71), (d4.txt,71), (d5.txt,70), (d6.txt,65), (d7.txt,61), (d8.txt,56), (d9.txt,54), (d10.txt,51), (d11.txt,48), (d12.txt,45), (d13.txt,41), (d14.txt,36), (d15.txt,34), (d16.txt,30), (d17.txt,25), (d18.txt,20), (d19.txt,20), (d20.txt,18), (d21.txt,17), (d22.txt,17), (d23.txt,14), (d24.txt,12), (d25.txt,4)] 
Ans: [11, 17, 20, 22, 23] - 3 

In between 1
Array: [(d1.txt,82), (d2.txt,76), (d3.txt,71), (d4.txt,71), (d5.txt,70), (d6.txt,65), (d7.txt,61), (d8.txt,56), (d9.txt,54), (d10.txt,51), (d11.txt,48), (d12.txt,45), (d13.txt,41), (d14.txt,36), (d15.txt,34), (d16.txt,30), (d17.txt,25), (d18.txt,20), (d19.txt,20), (d20.txt,18), (d21.txt,17), (d22.txt,17), (d23.txt,14), (d24.txt,12), (d25.txt,50)] 
Ans: [11, 5, 8, 9, 10] - 3 

In between 2
Array: [(d1.txt,82), (d2.txt,76), (d3.txt,71), (d4.txt,71), (d5.txt,70), (d6.txt,65), (d7.txt,61), (d8.txt,56), (d9.txt,54), (d10.txt,51), (d11.txt,48), (d12.txt,45), (d13.txt,41), (d14.txt,36), (d15.txt,34), (d16.txt,30), (d17.txt,25), (d18.txt,20), (d19.txt,20), (d20.txt,18), (d21.txt,17), (d22.txt,17), (d23.txt,14), (d24.txt,12), (d25.txt,26)] 
Ans: [11, 17, 14, 15, 16] - 3

Insertion with a frequency match 
Array: [(d1.txt,82), (d2.txt,76), (d3.txt,71), (d4.txt,71), (d5.txt,70), (d6.txt,65), (d7.txt,61), (d8.txt,56), (d9.txt,54), (d10.txt,51), (d11.txt,48), (d12.txt,45), (d13.txt,41), (d14.txt,36), (d15.txt,34), (d16.txt,30), (d17.txt,25), (d18.txt,20), (d19.txt,20), (d20.txt,18), (d21.txt,17), (d22.txt,17), (d23.txt,14), (d24.txt,12), (d25.txt,17)] 
Ans: [11, 17, 20] - 3
	 */
}
