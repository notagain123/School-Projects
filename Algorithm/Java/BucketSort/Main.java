import java.util.*;
import java.io.*;


public class Main {

	public static void main(String[] args) throws IOException{
		
		if(args.length < 2){
			System.out.println("Wrong command argument.");
			System.exit(-1);
		}
		
		int input, largest = 0;
		
		try{
		   Scanner sc = new Scanner(new File(args[0]));
		
		   while(sc.hasNext()){
			  input = sc.nextInt();
			  largest = BucketSort.findMax(input, largest);
		   }
		   
		   sc.close();
		   
		}catch(Exception e){
			System.out.println("Can't open the file.");
			System.exit(-1);
		}
		
		BucketSort bs = new BucketSort(largest + 1);
		bs.hashForBucketSort(args);
		
	    bs.printOut(args);
	    
	}
}
