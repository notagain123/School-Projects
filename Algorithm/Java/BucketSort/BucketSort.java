import java.io.*;
import java.util.*;


public class BucketSort {
	
	private int[] array;
	private int size;
	
	BucketSort(int s){
		
		size = s + 1;
		array = new int[size];
		
	}
	
	public void hashForBucketSort(String[] arg) throws FileNotFoundException{
		Scanner sc = new Scanner(new File(arg[0]));
		int data;
		
		while(sc.hasNext()){
			data = sc.nextInt();
			array[data]++;
		}
		
		sc.close();
	}
	
	public void printOut(String[] arg) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[1]));
		
		for(int i = 0; i < size; i++){
			if(array[i] != 0){
				while(array[i] > 0){
					bw.write(i + " ");
					array[i]--;
				}
			}
		}
		
		bw.close();
	}
	
	public static int findMax(int x, int y){
		if(x > y)
			return x;
		else
			return y;
	}

}
