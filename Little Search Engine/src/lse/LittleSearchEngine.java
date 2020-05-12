package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		//2
		Scanner sc = new Scanner(new File(docFile));
		HashMap<String,Occurrence> yerr = new HashMap<String,Occurrence>(1000, 2.0f);
		while(sc.hasNext()) {
			String nextWord = sc.next();
			String temp = getKeyword(nextWord);
			if(temp != null) { //Temp is not null, so it is a keyword.
				if(yerr.containsKey(temp)) { //temp is already in hash table, so add frequency
					yerr.get(temp).frequency++;
				}
				else {
					yerr.put(temp, new Occurrence(docFile, 1));
				}
			}
		}
		return yerr;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		/** COMPLETE THIS METHOD **/
		//4
		int ct = 0;
		for(String key : kws.keySet()) {
			if(keywordsIndex.containsKey(key)) {
				Occurrence toAdd = kws.get(key);
				keywordsIndex.get(key).add(toAdd);
				insertLastOccurrence(keywordsIndex.get(key));
			}
			else {
				ArrayList<Occurrence> tempAdd = new ArrayList<Occurrence>();
				Occurrence toAdd = kws.get(key);
				tempAdd.add(toAdd);
				insertLastOccurrence(tempAdd);
				keywordsIndex.put(key, tempAdd);
			}
			ct++;
		}
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be stripped
	 * So "word!!" will become "word", and "word?!?!" will also become "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE) 
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		String newWord = word.toLowerCase();
		newWord = newWord.replaceAll("[.,?:;!]+$", "");
		String temp = "";
		for(int n = 0; n < newWord.length(); n++) {
			temp += newWord.charAt(n);
			if(!temp.matches("[a-zA-Z]")) {
				return null;
			}
			temp = "";
		}
		if(this.noiseWords.contains(newWord)) {
			return null;
		}
		if(newWord.equals("")) {
			return null;
		}
		return newWord;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		//3
		if(occs.size()<=1)
			return null;
		Occurrence last = occs.remove(occs.size()-1);
		ArrayList<Integer> midpoints = new ArrayList<Integer>();
		int lo = occs.size()-1;
		int hi = 0;
		int mid = 0;
		if(occs.size()%2 == 0)
			mid = (occs.size()/2)-1;
		else
			mid = occs.size()/2;
		while(lo > hi) {
			if(last.frequency == occs.get(mid).frequency) {
				midpoints.add(mid);
				break;
			}
			else if(last.frequency > occs.get(mid).frequency) {
				lo = mid;
			}
			else if(last.frequency < occs.get(mid).frequency) {
				hi = mid+1;
			}
			midpoints.add(mid);
			mid = (lo + hi) / 2;			
		}
		if(last.frequency < occs.get(lo).frequency) {
			midpoints.add(lo);
			occs.add(lo+1, last);
		}
		else {
			occs.add(mid, last);
		}
		return midpoints;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. 
	 * 
	 * Note that a matching document will only appear once in the result. 
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. 
	 * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
	 * frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, 
	 *         returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		/** COMPLETE THIS METHOD **/
		//5
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Occurrence> combine = new ArrayList<Occurrence>();
		if(keywordsIndex.get(kw1)!=null) {
		for(Occurrence k : keywordsIndex.get(kw1)) {
			combine.add(k);
		}
		}
		if(keywordsIndex.get(kw2)!=null) {
		for(Occurrence k : keywordsIndex.get(kw2)) { //for every occurrence kw2, insert by frequency
			for(int i = 0; i < combine.size(); i++) {
				if(combine.get(i).document.equals(k.document) && combine.get(i).frequency == k.frequency) {
					break;
				}
				if(k.frequency > combine.get(i).frequency) {
					combine.add(i, k);
					break;
				}
				if(i == combine.size()-1) {
					combine.add(i+1, k);
					break;
				}
			}
		}
		}
		int i = 0;
		while(result.size()<5 && i < combine.size()) {
			if(!result.contains(combine.get(i).document))
				result.add(combine.get(i).document);
			i++;
		}
		return result;
	
	}
}


