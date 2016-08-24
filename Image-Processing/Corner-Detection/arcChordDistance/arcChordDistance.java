import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class arcChordDistance {

	private int Kchord, numPts, P1, P2, index;
	private boundaryPt[] boundPtAry;
	private image img;
	private double[] chordAry;
	private int[] localMax;
	
	public arcChordDistance (int r, int c, int min, int max, String[] arg, int points){
		img = new image(r, c, min, max);
		numPts = points;
		index = 0;
		boundPtAry = new boundaryPt[numPts];
		Kchord = Integer.parseInt(arg[1]);
		P1 = Kchord;
		P2 = 0;
		chordAry = new double[Kchord];
		localMax = new int[numPts];
	}
	
	public void loadData(Scanner sc, String[] arg) throws IOException{
		int x, y;
		while (sc.hasNext()){
			x = sc.nextInt();
			y = sc.nextInt();
			boundPtAry[index] = new boundaryPt(x ,y);
			index++;
		}
		
		
		int index2, currPt;
		double dist;
		for (int i = 0; i < numPts; i++){
			index2 = 0;
			currPt = (P2 + 1) % numPts;
			while (index2 < Kchord){
				dist = computeDistance(P1, P2, currPt);
				chordAry[index2] = dist;

				if (dist > boundPtAry[currPt].getMaxDistance())
					boundPtAry[currPt].setMaxDistance(dist);

				index2++;
				currPt = (currPt + 1) % numPts;
			}

			int maxIndex, whichIndex;
			maxIndex = findMaxDist(arg);
			whichIndex = (P2 + maxIndex + 1) % numPts;
			boundPtAry[whichIndex].setMaxCount(boundPtAry[whichIndex].getMaxCount() + 1);

			P1 = (P1 + 1) % numPts;
			P2 = (P2 + 1) % numPts;
		}
		
		computeLocalMaxima();
		isCorner();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[2]));
		for (int i = 0; i < numPts; i++){
			bw.write(boundPtAry[i].getX() + " " + boundPtAry[i].getY() + " " + boundPtAry[i].getCorner());
			bw.newLine();
		}
		bw.close();
		
		int idx = 0;
		while (idx < numPts){
			img.getImgAry()[boundPtAry[idx].getX()][boundPtAry[idx].getY()] = boundPtAry[idx].getCorner();
			idx++;
		}
		
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(arg[4], true));
		bw2.write("-------------------------------------------------");
		bw2.newLine();
		bw2.write("Entire boundPtAry:");
		bw2.newLine();
		for (int i = 0; i < numPts; i++){
			bw2.write(boundPtAry[i].getX() + " " + boundPtAry[i].getY() + " " + boundPtAry[i].getMaxCount() + " " + boundPtAry[i].getMaxDistance());
			bw2.newLine();
		}
		bw2.close();
		
		img.prettyPrint(arg);
	}
	
	double computeDistance(int p1, int p2, int curr){
		double result;
		result = Math.abs((boundPtAry[P1].getY()-boundPtAry[P2].getY())*boundPtAry[curr].getX()+
				(boundPtAry[P2].getX()-boundPtAry[P1].getX())*boundPtAry[curr].getY()+
				(boundPtAry[P1].getX()*boundPtAry[P2].getY()-boundPtAry[P2].getX()*boundPtAry[P1].getY()))/
				Math.sqrt((boundPtAry[P1].getY()-boundPtAry[P2].getY())*(boundPtAry[P1].getY()-boundPtAry[P2].getY())+
					(boundPtAry[P2].getX()-boundPtAry[P1].getX())*(boundPtAry[P2].getX()-boundPtAry[P1].getX()));
		return result;
	}

	public int findMaxDist(String[] arg) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[4], true));
		bw.write("chordAry: ");
		bw.newLine();

		double temp = chordAry[0];
		int tempIndex = 0;
		for (int i = 0; i < Kchord; i++){
			bw.write(chordAry[i] + " ");
			if (chordAry[i] > temp){
				temp = chordAry[i];
				tempIndex = i;
			}
		}
		bw.newLine();
		bw.close();
		return tempIndex;
	}
	
	public void computeLocalMaxima(){
		int temp1, temp2, temp3, temp4;
		for (int i = 0; i < numPts; i++){
			if (i < 2){
				temp1 = ((i - 1) + numPts) % numPts;
				temp2 = ((i - 2) + numPts) % numPts;
			}
			else{
				temp1 = i - 1;
				temp2 = i - 2;
			}
			temp3 = (i + 1) % numPts;
			temp4 = (i + 2) % numPts;
			if (boundPtAry[i].getMaxCount()>= boundPtAry[temp1].getMaxCount() && boundPtAry[i].getMaxCount() >= boundPtAry[temp2].getMaxCount()
				&& boundPtAry[i].getMaxCount() >= boundPtAry[temp3].getMaxCount() && boundPtAry[i].getMaxCount() >= boundPtAry[temp4].getMaxCount())
				localMax[i] = 1;
			else
				localMax[i] = 0;
		}
	}
	
	public void isCorner(){
		int temp1, temp2;
		for (int i = 0; i < numPts; i++){
			if (i < 2){
				temp1 = (i - 2) + numPts;
			}
			else{
				temp1 = i - 2;
			}
			temp2 = (i + 2) % numPts;
			if (localMax[i] != 0 && localMax[temp1] == 0 && localMax[temp2] == 0)
				boundPtAry[i].setCorner(9);
			else
				boundPtAry[i].setCorner(1);
		}
	}
}
