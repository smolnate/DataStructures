package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	
	
	private TagNode endTag = null;
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	
	public void build() {		
		/** COMPLETE THIS METHOD **/
		
		Stack<TagNode> tagNodeStack = new Stack<TagNode>();
		tagNodeStack.clear();
		
		String s = null;
		String sNoTags = null;
		
		TagNode ptr = null;
		TagNode prev = null;
		TagNode ptr2 = null;
		
		while (sc.hasNext()) {
			s = sc.nextLine();
			
			if (s.charAt(0) == '<' && s.charAt(s.length() - 1) == '>') {
				if (s.charAt(1) != '/') {
					sNoTags = s.substring(1, s.length() - 1);
					ptr = new TagNode(sNoTags, null, null);
					if (root == null) {
						root = ptr;
						tagNodeStack.push(ptr);
						prev = ptr;
						ptr2 = ptr;
					}
					else if (ptr2.firstChild == null) {
						if (prev != null) {
							if (prev.firstChild == null) {
								prev.firstChild = ptr;
							}
							else {
								ptr2.sibling = ptr;
							}
						}
						else {
							ptr2.firstChild = ptr;
						}
						tagNodeStack.push(ptr);
						prev = tagNodeStack.peek();
						ptr2 = tagNodeStack.peek();
					}
					else if (endTag != null) {
						endTag.sibling = ptr;
						tagNodeStack.push(ptr);
						prev = tagNodeStack.peek();
						ptr2 = tagNodeStack.peek();
					}
				}
				else {
					sNoTags = s.substring(2, s.length() - 1);
					ptr = new TagNode(sNoTags, null, null);
					if (tagNodeStack.peek() != null) {
						TagNode dummy = tagNodeStack.peek();
						if (ptr.tag != dummy.tag) {
							while (!ptr.tag.equals(tagNodeStack.peek().tag)) {
								dummy = tagNodeStack.pop();
							}
							dummy = tagNodeStack.pop();
						}
						endTag = dummy;
						if (!dummy.tag.equals("<html>") && !dummy.tag.equals("html")) {
							ptr2 = tagNodeStack.peek();
						}
					}
				}
			}
			else {
				sNoTags = s;
				ptr = new TagNode(sNoTags, null, null);
				if (ptr2.firstChild == null) {
					if (prev != null) {
						if (prev != ptr2) {
							endTag.sibling = ptr;
						}
						else {
							ptr2.firstChild = ptr;
						}
					}
				}
				else if (endTag != null) {
					endTag.sibling = ptr;
				}
				tagNodeStack.push(ptr);
				ptr2 = tagNodeStack.peek();
			}
		}
		
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	
	private void replace(String oldTag, String newTag, TagNode n) {
		if (n == null) {
			return;
		}
		
		TagNode m = n;
		while (m != null) {
			if (oldTag.equals(m.tag)) {
				m.tag = newTag;
			}
			if (m.firstChild != null) {
				replace(oldTag, newTag, m.firstChild);
			}
			m = m.sibling;
		}
		
	}
	
	public void replaceTag(String oldTag, String newTag) {
		replace(oldTag, newTag, root);
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	private void bold(int row, TagNode n) {
		if (n == null) {
			return;
		}
		
		TagNode ptr = n;
		while (ptr != null) {
			if (ptr.tag.equals("table")) {
				TagNode m = null;
				int counter = 0;
				if (ptr.firstChild.tag.equals("tr")) {
					m = ptr.firstChild;
					counter++;
				}
				while (counter != row) {
					if (m != null) {
						if (m.sibling.tag.equals("tr")) {
							m = m.sibling;
							counter++;
						}
					}
				}
				for (TagNode a = m.firstChild; a.tag.equals("td"); a = a.sibling) {
					if (a.firstChild != null) {
						a.firstChild = new TagNode("b", a.firstChild, a.firstChild.sibling);
					}
				}
			}
			if (ptr.firstChild != null) {
				bold(row, ptr.firstChild);
			}
			ptr = ptr.sibling;
		}
		
		
	}
	
	
	public void boldRow(int row) {
		bold(row, root);
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	
	private void remove(String tag, TagNode n, TagNode prev) {
		
		if (n == null) {
			return;
		}
		if (!tag.equals("p") && !tag.equals("em") && !tag.equals("b") && !tag.equals("ol") && !tag.equals("ul")) {
			return;
		}
		TagNode ptr = n;
		
		while (ptr != null) {
			if (ptr.tag.equals(tag)) {
				if (prev.sibling == ptr) {
					TagNode m = ptr.firstChild.sibling;
					if (ptr.firstChild != null) {
						prev.sibling = ptr.firstChild;
					}
					if (m != null) {
						while (m.sibling != null) {
							m = m.sibling;
						}
						
						m.sibling = ptr.sibling;
						
					}
					ptr = prev.sibling;
				}
				else if (prev.firstChild == ptr) {
					if (ptr.firstChild == null) {
						if (ptr.sibling == null) {
							prev.firstChild = null;
						}
						else {
							prev.firstChild = ptr.sibling;
						}
					}
					else {
						prev.firstChild = ptr.firstChild;
						if (ptr.sibling != null) {
							prev.firstChild.sibling = ptr.sibling;
						}
					}
					ptr = prev.firstChild;
				}
				if (ptr.tag.equals("li")) {
					TagNode dummy = ptr;
					if (dummy.sibling != null) {
						while (dummy.sibling != null) {
							dummy.tag = "p";
							dummy = dummy.sibling;
							if (!dummy.tag.equals("li")) {
								break;
							}
						}
						if (dummy.sibling == null && dummy.tag.equals("li")) {
							dummy.tag = "p";
						}
					}
				}
				
			}
			prev = ptr;
			if (ptr.firstChild != null) {
				remove(tag, ptr.firstChild, ptr);
			}
			ptr = ptr.sibling;
				
		}
	
		
		
	}
	
	public void removeTag(String tag) {
		remove(tag, root, null);
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	
	private void add(String word, String tag, TagNode n, TagNode prev) {
		if (n == null || (!tag.equals("b") && !tag.equals("em"))) {
			return;
		}
		TagNode ptr = n;
		TagNode a = new TagNode(tag, null, null);
		while (ptr != null) {
			if (ptr.tag.equals(word)) {
				if (ptr == prev.sibling) {
					prev.sibling = a;
					a.sibling = ptr;
				}
				else if (ptr == prev.firstChild) {
					prev.firstChild = a;
					a.firstChild = ptr;
				}
				prev = ptr;
			}
			else if (ptr.tag.contains(word)) {
				StringTokenizer st = new StringTokenizer(ptr.tag);
				String[] tokenArray = new String[st.countTokens()];
				int count = 0;
				while (st.hasMoreTokens()) {
					tokenArray[count] = st.nextToken();
					count++;
				}
				ArrayList<String> tokenArrayList = new ArrayList<String>(Arrays.asList(tokenArray));
				for (int num = 0; num < tokenArrayList.size(); num++) {
					if (tokenArrayList.get(num).contains(word)) {
						continue;
					}
					else if (num > 0 && !tokenArrayList.get(num-1).contains(word)) {
						String s1 = tokenArrayList.get(num - 1);
						String s2 = tokenArrayList.get(num);
						tokenArrayList.remove(num);
						tokenArrayList.add(num - 1, s1 + " " + s2);
						num--;
						
					}
				}
				
				int counter = 0;
				TagNode dummy1 = null;
				TagNode dummy2 = null;
				
				while (counter < tokenArrayList.size()) {
					String s3 = tokenArrayList.get(counter);
					if (!s3.contains(word)) {
						if (dummy1 != null) {
							if (dummy2.sibling == null) {
								dummy2.sibling = new TagNode(s3, null, null);
								dummy2 = dummy2.sibling;
							}
						}
						else {
							dummy1 = new TagNode(s3, null, null);
							dummy2 = dummy1;
						}
					}
					else {
						TagNode taggedWord = new TagNode(s3, null, null);
						TagNode bOrEm = new TagNode(tag, null, null);
						bOrEm.firstChild = taggedWord;
						if (dummy2 != null) {
							if (dummy2.sibling == null) {
								dummy2.sibling = bOrEm;
								dummy2 = bOrEm;
							}
						}
						else {
							if (dummy1 == null) {
								dummy1 = bOrEm;
							}
							dummy2 = bOrEm;
						}
					}
					counter++;
					
				}
				
				prev.firstChild = dummy1;
				dummy2.sibling = ptr.sibling;
				
			}
			if (ptr.firstChild != null) {
				add(word, tag, ptr.firstChild, ptr);
			}
			
			ptr = ptr.sibling;
		}
		
	}
	
	public void addTag(String word, String tag) {
		add(word, tag, root, null);
		/** COMPLETE THIS METHOD **/
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
