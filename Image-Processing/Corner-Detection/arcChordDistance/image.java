import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class image {

	private int numRows, numCols, minVal, maxVal;
	private int imgAry[][];
	
	public image(int r, int c, int min, int max){
		numRows = r;
		numCols = c;
		minVal = min;
		maxVal = max;
		imgAry = new int[numRows][numCols];
	}

	public void prettyPrint(String[] arg) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(arg[3]));
		
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				if (imgAry[i][j] != 0){
					if (imgAry[i][j] == 1)
						bw.write("1");
					else
						bw.write("9");
				}
				else
					bw.write(" ");
			}
			bw.newLine();
		}
		bw.close();
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public int getMinVal() {
		return minVal;
	}

	public int getMaxVal() {
		return maxVal;
	}

	public int[][] getImgAry() {
		return imgAry;
	}

	public void setImgAry(int[][] imgAry) {
		this.imgAry = imgAry;
	}
	
	
	
}
