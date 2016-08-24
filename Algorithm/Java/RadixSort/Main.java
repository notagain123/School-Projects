import java.io.*;
import java.util.*;


public class Main {
    public static void main(String[] args) throws IOException{
    	if (args.length < 2 || args.length > 2){
    		System.out.println("Wrong command argument.");
    		System.exit(-1);
    	}
        
    	Stack s = new Stack();
    	String input, largest = "";
    	
    	try{
    		Scanner sc = new Scanner(new File(args[0]));
            
            while(sc.hasNext()){
            	input = sc.next();
            	largest = findMax(input, largest);
            	s.push(input);
            }
            
            sc.close();
    		
    	}catch(Exception e){
    		System.out.println("Can't open the file");
    		System.exit(-1);;
    	}

    	System.out.println("The Stack:");
    	s.printStack();
    	
    	RadixSort rs = new RadixSort(largest.length());
    	rs.radixSort(s);
    	rs.outPutToFile(args);
    }
    
    public static String findMax(String x, String y){
    	if (x.length() > y.length())
    		return x;
    	else if (y.length() > x.length())
    		return y;
    	else if (x.compareTo(y) > 0)
    		return x;
    	else
    		return y;
    }
    
}
