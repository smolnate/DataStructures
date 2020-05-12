package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/()[]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
	
	/**
	 * helper method to check if numeric
	 */
	private static boolean isNumber(String str) {
		String regex = "\\d+";
		if(str.matches(regex))
			return true;
		return false;
		
	}
    public static void 
    makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 **/
    	StringTokenizer str = new StringTokenizer(expr, delims);
    	int lastpos = 0;
    	while(str.hasMoreTokens()) {
    		String temp = str.nextToken();
    		//System.out.println("New temp: "+temp);
    		//check if the character after temp is out of index, then must be variable
    		int pos = expr.indexOf(temp, lastpos)+temp.length();
    		lastpos = pos;
    		if(pos >= expr.length()) {
    			//System.out.println("Option1");
    			Variable varAdd = new Variable(temp);
    			if(!vars.contains(varAdd) && !isNumber(temp))
    				vars.add(varAdd);
    		}
    		//check if char after temp is [, then must be array
    		else if(expr.charAt(pos) == '[') {
    			//System.out.println("Option2");
    			Array arrAdd = new Array(temp);
    			if(!arrays.contains(arrAdd))
    				arrays.add(arrAdd);
    		}
    		else {
    			//System.out.println("Option3");
    			Variable varAdd = new Variable(temp);
    			if(!vars.contains(varAdd) && !isNumber(temp))
    				vars.add(varAdd);
    		}
    	}
    	System.out.println("Variables");
    	for(int i = 0; i < vars.size(); i++) {
    		System.out.println(vars.get(i));
    	}
    	System.out.println("Arrays");
    	for(int i = 0; i < arrays.size(); i++) {
    		System.out.println(arrays.get(i));
    	}
    	
    	
    }
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void 
    loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    /**
     * helper method to determine precedence
     */
    private static boolean hasPrecedence(String temp, Stack<String> operator) {
    	//return true if and only if current operator is */ and top stack is +-
    	boolean precedence = false;
    	if((temp.equals("*")||temp.equals("/")) && (operator.peek().equals("+")||operator.peek().equals("-"))) {
    		precedence = true;
    	}
    	return precedence;
    }
    
    /**
     * helper method to do math
     */
    private static float doOp(float first, float second, String op) {
    	float res=0;
    	switch(op) {
			case "*": res=first * second; break;
			case "/": res=first / second; break;
			case "-": res=first - second; break;
			case "+": res=first + second; break;
    	}
    	return res;
    }
    
    /**
     * helper method to reverse stack
     */
    private static Stack<String> reverse(Stack<String> stk){
    	Stack<String> newstk = new Stack<String>();
    	while(!stk.isEmpty()) {
    		newstk.push(stk.pop());
    	}
    	return newstk;
    }
    
    /**
     * helper method to get value from array and its index
     */
    private static float getArrayValue(Array arr, int index) {
    	return arr.values[index];
    }
    /**
     * helper method to get array from string
     */
    private static Array getArray(ArrayList<Array> arrays, String str) {
    	return arrays.get(arrays.indexOf(str));
    }
    
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	//LOGIC
    	//stack for operands, stack for operators
    	//always push operands when parsing
    	//for operator, only push when stack empty or the precedence of operator is higher than the top of stack
    	//otherwise, pop operator and operate on operand
    	float res = 0;  
    	expr = expr.replaceAll(" ", "");
    	expr = expr.replaceAll("\t", "");
    	int parenct=0;
    	int brackct=0;
    	Stack<Float> operand = new Stack<Float>();
    	Stack<String> operator = new Stack<String>();
    	for(int i = 0; i < expr.length(); i++) {
    		String tok = expr.substring(i, i+1);
    		//if ) found, flush ops and return whatever is in operand
    		if(tok.equals(")")) {
    			parenct--;
    			if(parenct>=1)
    				continue;
    	    	while(!operator.isEmpty()) {
    	    		float second = operand.pop();
    	    		float first = operand.pop();
    	    		operand.push(doOp(first, second, operator.pop()));   		
    	    	}
    			return operand.pop();
    		}
    		//same for ]
    		else if(tok.equals("]")) {
    			brackct--;
    			if(brackct>1)
    				continue;
    			while(!operator.isEmpty()) {
    	    		float second = operand.pop();
    	    		float first = operand.pop();
    	    		operand.push(doOp(first, second, operator.pop()));   		
    	    	}
    			return operand.pop();
    			
    		}
    		//find (, recursive call on evaluate
    		else if(tok.equals("(")) {
    			parenct++;
    			operand.push(evaluate(expr.substring(i+1), vars, arrays));
    			//search for closing ). if finding another (, recursive call. otherwise update char position
    			for(int j = i+1; j < expr.length(); j++) {
    				String toke = expr.substring(j,j+1);
    				if(toke.equals("(")) {
    					parenct++;
    					if(parenct==1)
    						operand.push(evaluate(expr.substring(j+1), vars, arrays));
    				}
    				else if(toke.equals(")")) {
    					parenct--;
    					if(parenct==0) {
    						i = j;
    						break;
    					}
    				}
    			}
    		}  	
    		//check if operator
    		else if(tok.contentEquals("+")||tok.contentEquals("*")||tok.contentEquals("/")||tok.contentEquals("-")){//operator
    			//check if the current operator has priority over top stack, or opstack is empty
    			if(operator.isEmpty() || hasPrecedence(tok, operator))
    				operator.push(tok);
    			else { //does not have priority. Pop currop->domath, then push tok
    				//keep pushing operator until currOp has precedence over top opStack
    				while(!operator.isEmpty()) {
    					if(!hasPrecedence(tok, operator)) {
	        	    		float second = operand.pop();
	        	    		float first = operand.pop();
	        	    		operand.push(doOp(first, second, operator.pop())); 
    					}
    					else
    						break;
    				}
    				operator.push(tok);
    			}
    		}
    		//current character is not a delim */+-[](). Loop till one delim is found,
    		//concatenate character to temp token instead of using tok
    		else {
    			String temptok = tok;
    			for(int pos = i+1; pos < expr.length(); pos++) {
    				if(delims.indexOf(expr.charAt(pos))!=-1)
    					break;
    				else
    					temptok += expr.charAt(pos);
    			}
    			i+=temptok.length()-1;
    			//check if temptok is a value
	    		if(isNumber(temptok)) //push number
	    			operand.push(Float.parseFloat(temptok));
	    		//now check if temptok is either variable, or array.
	    		else {
	    			//array check
	    			boolean brokeloop = false;
	    			for(int x = 0; x < arrays.size(); x++) {
	    				if(arrays.get(x).name.equals(temptok)) { //token is array, treat like (
	    					brackct++;
	    					operand.push((float)arrays.get(x).values[(int)evaluate(expr.substring(i+2),vars,arrays)]);
	    					//search for closing bracket. if finding another [ recursive call. otherwise update
	    					for(int br = i+2; br<expr.length(); br++) {
	    						String toke = expr.substring(br,br+1);
	    						if(toke.equals("[")) {
	    							brackct++; 
	    							if(brackct==1)
	    								operand.push((float)arrays.get(x).values[(int)evaluate(expr.substring(br+1),vars,arrays)]);
	    						}
	    						else if(toke.equals("]")) {
	    	    					brackct--;
	    	    					if(brackct==0) {
	    	    						i = br;
	    	    						brokeloop = true;
	    	    						break;
	    	    					}
	    						}   						
	    					}
	    					if(brokeloop)
	    						break;
	    					continue;
	    				}
	    				if(x == arrays.size()-1) {
	    	    			//not array, must be a variable
	    	    			Variable temp = new Variable(temptok);
	    	    			operand.push((float)vars.get(vars.indexOf(temp)).value);  					
	    				}
	    			}
	    			if(arrays.isEmpty()) {
	    				Variable temp = new Variable(temptok);
		    			operand.push((float)vars.get(vars.indexOf(temp)).value);    				
	    			}
	    		}
	    	}    
    	}
    	//no operator present after traversing whole expression, meaning only a number is present
 //   	if(operator.isEmpty())
 //   		return operand.pop(); 
    	//empty operator stack
    	while(!operator.isEmpty()) {
    		float second = operand.pop();
    		float first = operand.pop();
    		operand.push(doOp(first, second, operator.pop()));   		
    	}
    	
    	//result will be stored in operand
    	return operand.pop();
    }
}