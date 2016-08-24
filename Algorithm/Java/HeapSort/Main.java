import java.io.*;
import java.util.*;

public class Main {
	
	public static void main(String[] args) throws IOException{
		
		if(args.length < 2 || args.length > 2){
			System.out.println("Wrong command argument.");
			System.exit(-1);
		}
		
		int count = 0;
		
		try{
		    Scanner sc = new Scanner(new File(args[0]));
			
			while(sc.hasNext()){
				sc.next();
				count++;
			}
				
			sc.close();
			
		}catch(Exception e){
			System.out.println("Can't open the file");
			System.exit(-1);
		}
		
		HeapSort myArray = new HeapSort(count);
		
		myArray.buildHeap(args);
		
        printFinalHeap(args, myArray);
		
        myArray.deleteRoot(args);
        
        printFinalHeap(args, myArray);

	}
	
	public static void printFinalHeap(String[] arg, HeapSort array) throws IOException{
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(arg[1], true));
		
		outputFile.newLine();
		outputFile.write("Final Heap:");
		outputFile.newLine();
		outputFile.write(array.toString());
		outputFile.newLine();
		outputFile.newLine();
		
		outputFile.close();
	}
	
}
