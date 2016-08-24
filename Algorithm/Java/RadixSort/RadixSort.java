import java.io.*;


public class RadixSort {
	private hashtable[] Table = new hashtable[2];
	private int totalDigit, currentDigit, currenTable, previousTable, index;
	
	public RadixSort(int t){
		totalDigit = t;
		currentDigit = 0;
		currenTable = 0;
		previousTable = 1;
		for(int i = 0; i < 2; i++){
			Table[i] = new hashtable();
		}
	}
	

	public void radixSort(Stack s){
		while (currentDigit != totalDigit){
			if (currentDigit == 0){
				while (!s.isEmpty()){
					Node temp = s.pop();
					Hash(temp.getData());
				}//while
				System.out.println("\n\nCurrent Table*************************************");
				Table[currenTable].printTable();
				swapTable();
			}//if
			else{
				System.out.println("\n\nPrevious Table*************************************");
				Table[previousTable].printTable();

				for (int i = 0; i < 10; i++){
				     while (!Table[previousTable].accessQueue(i).isEmpty()){
				        Node temp = Table[previousTable].accessQueue(i).deleteHead();
				        Hash(temp.getData());
					}//while
				}//for
				swapTable();
			}//else
			currentDigit++;
		}//while

		System.out.println("\n\nCurrent Table*************************************");
		if (totalDigit % 2 == 0){
			Table[1].printTable();
		}
		else{
			Table[0].printTable();
		}
	}

	public void swapTable(){
		int temp = currenTable;
		currenTable = previousTable;
		previousTable = temp;
	}

	public void outPutToFile(String[] arg) throws IOException{
		BufferedWriter outputFile = new BufferedWriter(new FileWriter(arg[1]));

		if (totalDigit % 2 == 0){
			for (int i = 0; i < 10; i++){
				while (!Table[1].accessQueue(i).isEmpty()){
					Node temp = Table[previousTable].accessQueue(i).deleteHead();
					outputFile.write(temp.getData() + " ");
				}
			}
		}
		else{
			for (int i = 0; i < 10; i++){
				while (!Table[0].accessQueue(i).isEmpty()){
					Node temp = Table[previousTable].accessQueue(i).deleteHead();
					outputFile.write(temp.getData() + " ");
				}
		    }
		}
		
		outputFile.newLine();
		outputFile.close();
	}

	public void Hash(String input){

		if (input.length() < (currentDigit + 1))
			index = 0;
		else
			index = input.charAt(input.length() - (currentDigit + 1)) - 48;

		if (currenTable % 2 == 0)
			Table[0].accessQueue(index).addTail(input);
		else
			Table[1].accessQueue(index).addTail(input);
		}
}
