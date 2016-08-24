import java.io.*;
import java.util.*;

public class HeapSort {

	private int[] heapArray;
	private int size;
	
	public HeapSort(int n){
		size = n + 1;
		heapArray = new int[size];
		heapArray[0] = 0;
	}
	
	public void buildHeap(String[] arg) throws IOException{
		Scanner inputFile = new Scanner(new File(arg[0]));
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(arg[1]));
		
		outputFile.write("Build Heap: ");
		outputFile.newLine();
		outputFile.close();
		int data;
		

		while (inputFile.hasNext()){
			data = inputFile.nextInt();
			insertOneDataItem(data);
			
			outputFile = new BufferedWriter(new FileWriter(arg[1], true));
			
			if(data < 10)
		        outputFile.write("insert " + data + "          ");
		    else
		        outputFile.write("insert " + data + "         ");
		
		    outputFile.close();
			
			printHeap(arg);
		}

		inputFile.close();
	}
	
	public void insertOneDataItem(int number){
		if (!isFull()){
			heapArray[0]++;
			heapArray[heapArray[0]] = number;
			bubbleUp();
		}
		else{
			System.out.println("The Heap Array is full.");
		}
	}
	
	private void bubbleUp(){
		for (int i = heapArray[0]; i != 1; i /= 2){
			if (heapArray[i] < heapArray[i / 2]){
				swap(heapArray, i, i / 2);
			}
		}
	}
	
	public void deleteRoot(String[] arg) throws IOException{
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(arg[1], true));
		outputFile.write("Delete Heap: ");
		outputFile.newLine();
		outputFile.close();
		
		if (!isEmpty()){
			while (heapArray[0] > 0){
			    outputFile = new BufferedWriter(new FileWriter(arg[1], true));
			    
				int temp = heapArray[1];
				heapArray[1] = heapArray[heapArray[0]];
				heapArray[0]--;
				
				if(temp < 10)
				        outputFile.write("delete " + temp + "          ");
				else
				        outputFile.write("delete " + temp + "         ");
				
				outputFile.close();
				
				bubbleDown();
				printHeap(arg);
			}
		}
		else{
			System.out.println("The Heap Array is empty.");
		}

	}
	
	private void bubbleDown(){
		int i = 1;

		while (i * 2 <= heapArray[0]){
			int min = Math.min(heapArray[i * 2], heapArray[i * 2 + 1]);
			if (heapArray[i] > min){
				if (heapArray[i * 2] == min){
					swap(heapArray, i, i * 2);
					i *= 2;
				}//if
				else{
					swap(heapArray, i, i * 2 + 1);
					i = i * 2 + 1;
				}//else if
			}
			else
				break;
		}
	}
	
	private void swap(int[] array, int x, int y){
		int temp = array[x];
		array[x] = array[y];
		array[y] = temp;
	}
	
	private boolean isFull(){
		return heapArray[0] == size;
	}

	private boolean isEmpty(){
		return heapArray[0] == 0;
	}
	
	public void printHeap(String[] arg) throws IOException{
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(arg[1], true));
		
		int totalElements = heapArray[0];
		int count = 0;
        
		while (count < 10 && count != totalElements){
			outputFile.write(heapArray[count + 1] + " ");
			count++;
		}
		
		outputFile.newLine();

		outputFile.close();
	}
	
	public String toString(){
		String result = "";
		
		if(heapArray[0] == 0){
			return result;
		}else{
		    for(int i = 0; i < heapArray[0]; i++)
			result = result + heapArray[i + 1] + " ";
		}
		
		return result;
	}

}
