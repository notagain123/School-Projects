import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class DijkstraSSS {
	private int N;
	private int sourceNode;
	private int initialSourceNode;
	private int minNode;
	private int currentNode;
	private int newCost;
	private int[][] costMatrix;
	private int[] fatherArray;
	private int[] markedArray;
	private int[] bestCostArray;
	
	public DijkstraSSS(int n, int source){
		N = n;
		sourceNode = source;
		initialSourceNode = source;
		costMatrix = new int[n][n];
		fatherArray = new int[n];
		markedArray = new int[n];
		bestCostArray = new int[n];
		
		for (int i = 0; i < n; i++){
			fatherArray[i] = i;
			markedArray[i] = 0;
			bestCostArray[i] = 9999;
			for( int j = 0; j < n; j++){
				if (i == j)
					costMatrix[i][j] = 0;
				else
					costMatrix[i][j] = 99999;
			}
		}
	}
	
	public void Dijkstras(Scanner sc, String[] arg) throws IOException{
		bestCostArray[sourceNode - 1] = 0;
		
		int x, y, cost;
		while(sc.hasNext()){
			x = sc.nextInt();
			y = sc.nextInt();
			cost = sc.nextInt();
			fillMatrix(x, y, cost);
		}
		printMartix(arg);
	
        while(sourceNode <= N){
          PrintSource(arg);
		  while(!allMarked()){
		      findMinNode();
		      printMinNode(arg);
		      expanding();
		      debugPrint(arg);
		  }		 
		  findNprintShortestPath(arg);
		  sourceNode++;
		  reSet();
       }
        
       sourceNode = initialSourceNode;
       sourceNode--;
       
       while(sourceNode > 0){
    	  reSet();
          PrintSource(arg);
  		  while(!allMarked()){
  		      findMinNode();
  		      printMinNode(arg);
  		      expanding();
  		      debugPrint(arg);
  		  }		 
  		  findNprintShortestPath(arg);
  		  sourceNode--;
        }

	}
	
	public void expanding(){
		for (int i = 0; i < N; i++){
			if (markedArray[i] == 0){
				currentNode = i;
				computeCost(minNode, currentNode);
				if (newCost < bestCostArray[currentNode]){
					updateFatherArray(minNode, currentNode);
					updateBestCostArray(currentNode, newCost);
				}
			}
		}
	}

	public void fillMatrix(int x, int y, int cost){
		costMatrix[x - 1][y - 1] = cost;
	}
	
	public void findMinNode(){
		for (int i = 0; i < N; i++){
			if (markedArray[i] == 0 && smallest(i)){
				minNode = i;
				updateMarkedArray(minNode);
				break;
			}
		}
	}
	
	public boolean smallest(int x){
		int min = bestCostArray[x];
		int temp = bestCostArray[x];
		for (int i = 0; i < N; i++){
			if (markedArray[i] == 0 && temp > bestCostArray[i])
				temp = bestCostArray[i];
		}
		
		return min == temp;		
	}
	
	public void computeCost(int min, int current){
		newCost = costMatrix[min][current] + bestCostArray[minNode];
	}
	
	public void updateMarkedArray(int min){
		markedArray[min] = 1;
	}
	
	public void updateFatherArray(int min, int current){
		fatherArray[current] = min;
	}
	
	public void updateBestCostArray(int current, int cost){
		bestCostArray[current] = cost;
	}
	
	public void printMartix(String[] arg) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[3]));
		bw.write("Cost Matrix:");
		bw.newLine();
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				if(costMatrix[i][j] < 10)
					bw.write("    " + costMatrix[i][j] + " ");
				else if(costMatrix[i][j] < 100)
					bw.write("   " + costMatrix[i][j] + " ");
				else
					bw.write(costMatrix[i][j] + " ");
			}
			bw.newLine();
		}
		bw.close();
	}
	
	public void PrintSource(String[] arg) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[3], true));
		bw.newLine();
		bw.write("Source Node : " + sourceNode);
		bw.newLine();
		bw.close();
	}
	
	public void debugPrint(String[] arg) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[3], true));

		bw.write("Father Array");
		bw.newLine();
		for (int i = 0; i < N; i++)
			bw.write((fatherArray[i] + 1) + " ");
		bw.newLine();
		
		bw.write("Marked Array");
		bw.newLine();
		for (int i = 0; i < N; i++)
			bw.write(markedArray[i] + " ");
		bw.newLine();
		
		bw.write("Best Cost Array");
		bw.newLine();
		for (int i = 0; i < N; i++)
			bw.write(bestCostArray[i] + " ");
		bw.newLine();
		bw.write("---------------------------------------");
		bw.newLine();
		
		bw.close();
	}
	
	public void printMinNode(String[] arg) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[3], true));
		bw.newLine();
		bw.write("MinNode is " + (minNode + 1));
		bw.newLine();
		bw.close();
	}
	
	public boolean allMarked(){
		for (int i = 0; i < N; i++){
			if (markedArray[i] == 0)
				return false;
		}
		
		return true;
	}
	
	public void reSet(){
		if(sourceNode <= N){
		   for (int i = 0; i < N; i++){
			  fatherArray[i] = i;
			  markedArray[i] = 0;
			  bestCostArray[i] = 9999;
		   }
		   bestCostArray[sourceNode - 1] = 0;
		}
	}
	
	public void findNprintShortestPath(String[] arg) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[2], true));
		bw.write("The graph has " + N + " nodes, the source node is " + sourceNode);
		bw.newLine();
		for (int i = 0; i < N; i++){
			String s = "";
			int temp = i;
			bw.write("The path from " + sourceNode + " to " + (i + 1) + " : " + sourceNode);
            if (bestCostArray[i] == 9999){
            	bw.write("-->" + (i + 1) + " no path");
            	bw.newLine();
            }
            else{
            	if ((i + 1) != sourceNode){
			    while ((fatherArray[temp] + 1) != sourceNode){
				    temp = fatherArray[temp];
			        s = "-->" + (temp + 1) + s;
			    }
			}
			bw.write(s + "-->" + (i + 1) + " : cost = " + bestCostArray[i]);
		    bw.newLine();
            }
		}
		bw.write("---------------------------------------");
		bw.newLine();
		bw.close();
	}

}
