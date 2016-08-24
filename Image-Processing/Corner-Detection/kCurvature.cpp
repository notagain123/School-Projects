#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
using namespace std;

class image{
	friend class kCurvature;
	int numRows, numCols, minVal, maxVal;
	int** imgAry;

public:
	image(int r, int c, int min, int max){
		numRows = r;
		numCols = c;
		minVal = min;
		maxVal = max;
		imgAry = new int*[numRows]();

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols]();
		}
	}

	~image(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}

		delete[] imgAry;
	}

	void prettyPrint(char* arg[]){
		ofstream ofs;
		ofs.open(arg[4]);

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				if (imgAry[i][j] != 0)
					ofs << imgAry[i][j];
				else
					ofs << " ";
			}
			ofs << endl;
		}
		ofs.close();
	}

};

class boundaryPt{
	friend class kCurvature;
	int x, y, localMax, corner;
	double curvature;

public:
	boundaryPt(){
	}
};

class kCurvature{
	int K, numPts, Q, P, R, beginIndex;
	boundaryPt* boundPtAry;
	image* img;

public:
	kCurvature(int r, int c, int min, int max, int k, int points){
		img = new image(r, c, min, max);
		K = k;
		numPts = points;
		beginIndex = 0;
		boundPtAry = new boundaryPt[numPts];
	}

	~kCurvature(){
		delete[] boundPtAry;
	}

	void loadData(ifstream& ifs, char* arg[]){
		int x, y;
		int count = 0;
		while (!ifs.eof() && count < numPts){
			ifs >> x >> y;
			boundPtAry[beginIndex].x = x;
			boundPtAry[beginIndex].y = y;
			beginIndex++;
			count++;
		}

		computeCurvature(arg);
		computeLocalMaxima();
		isCorner(arg);

		int idx = 0;
		while (idx < numPts){
			img->imgAry[boundPtAry[idx].x][boundPtAry[idx].y] = boundPtAry[idx].corner;
			idx++;
		}

		img->prettyPrint(arg);
	}

	void computeCurvature(char* arg[]){
		ofstream ofs;
		ofs.open(arg[5]);

		Q = 0;
		P = Q + K;
		R = P + K;
		for (int i = 0; i < numPts; i++){
			boundPtAry[P].curvature = (double)(boundPtAry[Q].y - boundPtAry[P].y) / (double)(boundPtAry[Q].x - boundPtAry[P].x + 0.00001) -
			(double)(boundPtAry[P].y - boundPtAry[R].y) / (double)(boundPtAry[P].x - boundPtAry[R].x + 0.00001);

			ofs << "Q: " << Q << ", P: " << P << ", R: " << R << endl;
			ofs << boundPtAry[P].x << " " << boundPtAry[P].y << " " << boundPtAry[P].curvature << endl;

			Q = (Q + 1) % numPts;
			P = (P + 1) % numPts;
			R = (R + 1) % numPts;
		}

		ofs << "Entire boundPtAry-----------------------------------" << endl;
		for (int i = 0; i < numPts; i++){
			ofs << boundPtAry[i].x << " " << boundPtAry[i].y << " " << boundPtAry[i].curvature << endl;
		}

		ofs.close();
	}

	void computeLocalMaxima(){
		int temp1, temp2, temp3, temp4;
		for (int i = 0; i < numPts; i++){
			if (i < 2){
				temp1 = (i - 1) + numPts;
				temp2 = (i - 2) + numPts;
			}
			else{
				temp1 = i - 1;
				temp2 = i - 2;
			}
			temp3 = (i + 1) % numPts;
			temp4 = (i + 2) % numPts;
			if (abs(boundPtAry[i].curvature) >= abs(boundPtAry[temp1].curvature) && abs(boundPtAry[i].curvature) >= abs(boundPtAry[temp2].curvature)
				&& abs(boundPtAry[i].curvature) >= abs(boundPtAry[temp3].curvature) && abs(boundPtAry[i].curvature) >= abs(boundPtAry[temp4].curvature)
				&& boundPtAry[i].curvature != 0)
				boundPtAry[i].localMax = 1;
			else
				boundPtAry[i].localMax = 0;
		}

	}

	void isCorner(char* arg[]){
		ofstream ofs;
		ofs.open(arg[3]);

		int temp1, temp2;
		for (int i = 0; i < numPts; i++){
			if (i < 2){
				temp1 = (i - 2) + numPts;
			}
			else{
				temp1 = i - 2;
			}
			temp2 = (i + 2) % numPts;
			if (boundPtAry[i].localMax == 1 && boundPtAry[temp1].localMax == 0 && boundPtAry[temp2].localMax == 0)
				boundPtAry[i].corner = 8;
			else
				boundPtAry[i].corner = 1;
		}

		ofs << "Is corner:" << endl;
		for (int i = 0; i < numPts; i++){
			ofs << boundPtAry[i].x << " " << boundPtAry[i].y << " " << boundPtAry[i].corner << endl;
		}
		ofs.close();
	}

};

int main(int argc, char *argv[]){
	if (argc < 6 || argc > 6){
		cout << "Wrong command argument!" << endl;
		return -1;
	}

	ifstream ifs(argv[1]);
	if (!ifs){
		cout << "Can't open the file." << endl;
		return -1;
	}

	int k = atoi(argv[2]);
	int row, col, min, max, label, points;

	ifs >> row >> col >> min >> max >> label >> points;

	kCurvature kc(row, col, min, max, k, points);
	kc.loadData(ifs, argv);

	return 0;
}
