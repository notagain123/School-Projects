#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
using namespace std;

class IMAGE{
	int** imgAry;
	int numRows, numCols, minVal, maxVal, threaholdValue;
	int** ZeroframedAry;
	int** tempAry;

public:
	IMAGE(int r, int c, int mi, int ma, int n){
		numRows = r;
		numCols = c;
		minVal = mi;
		maxVal = ma;

		imgAry = new int *[numRows];
		ZeroframedAry = new int *[numRows + 2];
		tempAry = new int *[numRows + 2];

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols];
		}

		for (int i = 0; i < numRows + 2; i++){
			ZeroframedAry[i] = new int[numCols + 2]();
			tempAry[i] = new int[numCols + 2]();
		}

	}

	~IMAGE(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}
		for (int i = 0; i < numRows + 2; i++){
			delete[] ZeroframedAry[i];
			delete[] tempAry[i];
		}

		delete[] imgAry;
		delete[] ZeroframedAry;
 		delete[] tempAry;
	}

	void loadImage(ifstream& ifs, char *arg[]){
		int data;

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				ifs >> data;
				imgAry[i][j] = data;
			}
		}


		zeroFramed();
		firstPassDistance();
		prettyPrint(arg, true);
		secondPassDistance();
		prettyPrint(arg, false);
		computeSkeleton();
		prettyPrintDistance(arg);
		outputDistanceImage(arg);
		outputSkeletonImage(arg);
	}

	void prettyPrint(char *arg[], bool tof){
		ofstream ofs;

		ofs.open(arg[4], ofs.app);

		if (tof)
			ofs << "First Pass:" << endl;
		else
			ofs << "Second Pass:" << endl;

		ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				if (ZeroframedAry[i][j] == 0)
					ofs << " ";
				else
				    ofs << ZeroframedAry[i][j];
			}
			ofs << endl;
		}

		ofs << endl;
		ofs << "--------------------------------" << endl;
		ofs.close();
	}

	void zeroFramed(){
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				ZeroframedAry[i][j] = imgAry[i - 1][j - 1];
	}

	void firstPassDistance(){
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				if (ZeroframedAry[i][j] != 0)
					ZeroframedAry[i][j] = dfindMin(dfindMin(ZeroframedAry[i - 1][j - 1], ZeroframedAry[i - 1][j]),
					dfindMin(ZeroframedAry[i - 1][j + 1], ZeroframedAry[i][j - 1])) + 1;
			}
		}
	}

	void secondPassDistance(){
		for (int i = numRows + 1; i > 0; i--){
			for (int j = numCols + 1; j > 0; j--){
				if (ZeroframedAry[i][j] != 0)
					ZeroframedAry[i][j] = dfindMin(ZeroframedAry[i][j], dfindMin(dfindMin(ZeroframedAry[i][j + 1],
					ZeroframedAry[i + 1][j - 1]), dfindMin(ZeroframedAry[i + 1][j],
					ZeroframedAry[i + 1][j + 1])) + 1);
			}
		}
	}

	void computeSkeleton(){
		for (int i = numRows + 1; i > 0; i--){
			for (int j = numCols + 1; j > 0; j--){
				if (ZeroframedAry[i][j] > 0){
					int localMax;
					localMax = dfindMax(dfindMax(dfindMax(ZeroframedAry[i - 1][j - 1], ZeroframedAry[i - 1][j]),
						dfindMax(ZeroframedAry[i - 1][j + 1], ZeroframedAry[i][j - 1])), dfindMax(dfindMax(ZeroframedAry[i][j + 1],
						ZeroframedAry[i + 1][j - 1]), dfindMax(ZeroframedAry[i + 1][j], ZeroframedAry[i + 1][j + 1])));
					if (ZeroframedAry[i][j] >= localMax)
						tempAry[i][j] = ZeroframedAry[i][j];
					else
						tempAry[i][j] = 0;
				}
			}
		}
	}

	void prettyPrintDistance(char *arg[]){
		ofstream ofs;

		ofs.open(arg[4], ofs.app);
		ofs << "Image Skeleton:" << endl;
		ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				if (tempAry[i][j] == 0)
					ofs << " ";
				else
					ofs << mapInt2char(tempAry[i][j]);
			}
			ofs << endl;
		}

		ofs << endl;
		ofs << "--------------------------------" << endl;
		ofs.close();
	}

	char mapInt2char(int x){
		char c = '0' + x;
		return c;
	}

	void outputDistanceImage(char *arg[]){
		ofstream ofs;

		ofs.open(arg[2]);

		ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				ofs << ZeroframedAry[i][j];
			}
			ofs << endl;
		}

		ofs << endl;
		ofs.close();
	}

	void outputSkeletonImage(char *arg[]){
		ofstream ofs;

		ofs.open(arg[3]);

		ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				ofs << tempAry[i][j];
			}
			ofs << endl;
		}

		ofs << endl;
		ofs.close();
	}

	int dfindMin(int a, int b){
		if (a > b)
			return b;
		else
			return a;
	}

	int dfindMax(int a, int b){
		if (a > b)
			return a;
		else
			return b;
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

	int row, col, min, max;

	ifs >> row >> col >> min >> max;
	
	IMAGE zf(row, col, min, max, 2);
	zf.loadImage(ifs, argv);

	ifs.close();

	return 0;
}
