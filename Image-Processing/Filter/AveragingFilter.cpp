#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
using namespace std;

class IMAGE{
	int** imgAry;
	int* histogram;
	int numRows, numCols, minVal, maxVal, threaholdValue;
	int framingRow, framingCol;
	int** MirrorframedAry;
	int** tempAry;
	int median;
public:
	IMAGE(int r, int c, int mi, int ma, int n){
		numRows = r;
		numCols = c;
		framingRow = (r + 2);
		framingCol = (c + 2);
		minVal = mi;
		maxVal = ma;
		median = 0;
		threaholdValue = n;

		imgAry = new int *[numRows];
		MirrorframedAry = new int *[framingRow];
		tempAry = new int *[framingCol];

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols];
		}

		for (int i = 0; i < framingRow; i++){
			MirrorframedAry[i] = new int[framingCol]();
			tempAry[i] = new int[framingCol]();
		}

		histogram = new int[maxVal + 1]();
	}

	~IMAGE(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}
		for (int i = 0; i < framingRow; i++){
			delete[] MirrorframedAry[i];
			delete[] tempAry[i];
		}

		delete[] imgAry;
		delete[] MirrorframedAry;
		delete[] tempAry;
		delete[] histogram;
	}

	void loadImage(ifstream& ifs, char *arg[]){
		int data;

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				ifs >> data;
				imgAry[i][j] = data;
			}
		}

		computeHistogram();
		printHistogram(arg);
		mirrorFramed();
		AVG3X3();
		computeThreshold(arg);
	}

	void computeHistogram(){
		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				histogram[imgAry[i][j]]++;
			}
		}
	}

	void printHistogram(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2]);

		int counter;

		for (int i = 0; i < maxVal + 1; i++){
			counter = 0;
			if (i < 10)
				ofs << "( " << i << "):" << histogram[i] << " ";
			else
				ofs << "(" << i << "):" << histogram[i] << " ";

			while (histogram[i] > 0)
			{
				counter++;
				histogram[i]--;
			}

			if (counter > 80){
				int c = 80;
				while (c > 0){
					ofs << "+";
					c--;
				}
				ofs << endl;
			}
			else{
				while (counter > 0){
					ofs << "+";
					counter--;
				}
				ofs << endl;
			}
		}//for
		ofs << "--------------------------------" << endl;
		ofs.close();
	}

	void computeThreshold(char *arg[]){

		for (int i = 0; i < framingRow - 1; i++){
			for (int j = 0; j < framingCol - 1; j++){
				if (tempAry[i][j] < 35){
					tempAry[i][j] = 0;
				}
				else{
					tempAry[i][j] = 1;
				}
			}
		}
		prettyPrint(arg, threaholdValue);

	}

	void prettyPrint(char *arg[], int n){
		ofstream ofs;

		ofs.open(arg[3]);
		ofs << "Averaging Filter" << endl;

		ofs << "Threshold value: " << n << endl;
		ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;
		for (int i = 1; i < framingRow - 1; i++){
			for (int j = 1; j < framingCol - 1; j++){
				if (tempAry[i][j] == 0)
					ofs << " ";
				else
					ofs << tempAry[i][j];
			}
			ofs << endl;
		}
		ofs << "--------------------------------" << endl;
		ofs.close();
	}

	void zeroFramed(){
		for (int i = 1; i < framingRow - 1; i++)
			for (int j = 1; j < framingCol - 1; j++)
				MirrorframedAry[i][j] = imgAry[i - 1][j - 1];
	}

	void mirrorFramed(){
		for (int i = 1; i < framingRow - 1; i++)
			for (int j = 1; j < framingCol - 1; j++)
				MirrorframedAry[i][j] = imgAry[i - 1][j - 1];

		int j = 0;
		while (j < framingRow){
			MirrorframedAry[0][j] = MirrorframedAry[1][j];
			MirrorframedAry[framingRow - 1][j] = MirrorframedAry[framingRow - 2][j];
			j++;
		}

		int i = 0;
		while (i < framingRow){
			MirrorframedAry[i][0] = MirrorframedAry[i][1];
			MirrorframedAry[i][framingCol - 1] = MirrorframedAry[i][framingCol - 2];
			i++;
		}
	}

	void AVG3X3(){
		for(int i = 1; i < framingRow - 1; i++){
			for (int j = 1; j < framingCol - 1; j++){
				tempAry[i][j] = (MirrorframedAry[i - 1][j - 1] + MirrorframedAry[i - 1][j] +
					MirrorframedAry[i - 1][j + 1] + MirrorframedAry[i][j - 1] + MirrorframedAry[i][j]
					+ MirrorframedAry[i][j + 1] + MirrorframedAry[i + 1][j - 1] + MirrorframedAry[i + 1][j]
					+ MirrorframedAry[i + 1][j + 1]) / 9;
			}
        }
	}

	void Median3X3(){
		for (int i = 1; i < framingRow - 1; i++){
			for (int j = 1; j < framingCol - 1; j++){
				findMedian(i, j);
				tempAry[i][j] = median;
			}
		}
	}


	int findMedian(int i, int j){
		int* bubble = new int[9]();
		bool swapFlag = true;
		int temp;
		bubble[0] = MirrorframedAry[i - 1][j - 1];
		bubble[1] = MirrorframedAry[i - 1][j];
		bubble[2] = MirrorframedAry[i - 1][j + 1];
		bubble[3] = MirrorframedAry[i][j - 1];
		bubble[4] = MirrorframedAry[i][j];
		bubble[5] = MirrorframedAry[i][j + 1];
		bubble[6] = MirrorframedAry[i + 1][j - 1];
		bubble[7] = MirrorframedAry[i + 1][j];
		bubble[8] = MirrorframedAry[i + 1][j + 1];

		for (int h = 0; (h < 9) && swapFlag; h++){
			swapFlag = false;
			for (int k = 0; k < 8; k++){
				if (bubble[k + 1] < bubble[k]){
					temp = bubble[k];
					bubble[k] = bubble[k + 1];
					bubble[k + 1] = temp;
					swapFlag = true;
				}
			}
		}
		
		median = bubble[4];
		delete[] bubble;
		return median;
	}
};

int main(int argc, char *argv[]){

	if (argc < 5 || argc > 5){
		cout << "Wrong command argument!" << endl;
		return -1;
	}

	ifstream ifs(argv[1]);
	if (!ifs){
		cout << "Can't open the file." << endl;
		return -1;
	}
	
	int n = atoi(argv[4]);
    int row, col, min, max;
	
	ifs >> row >> col >> min >> max;

	IMAGE zf(row, col, min, max, n);
	zf.loadImage(ifs, argv);

	ifs.close();

	return 0;
}
