#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
using namespace std;

class SobelEdge{
	friend class ImageProcessing;

	int maskVertical[3][3];
	int maskHorizontal[3][3];
	int maskRightDiag[3][3];
	int maskLeftDiag[3][3];

	int** SobelVertical;
	int** SobelHorizontal;
	int** SobelRightDiag;
	int** SobelLeftDiag;
	int** SobelVHSum;
	int** SobelDiagSum;
	int numRows, numCols;
	int VHMin, VHMax, DiagMin, DiagMax;

public:
	SobelEdge(int r, int c){
		numRows = r;
		numCols = c;
		VHMin = 0;
		VHMax = 0;
		DiagMin = 0;
		DiagMax = 0;

		int Vertical[3][3] = { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } };
		int Horizontal[3][3] = { { 1, 2, 1 }, { 0, 0, 0 }, { -1, -2, -1 } };
		int RightDiag[3][3] = { { 2, 1, 0 }, { 1, 0, -1 }, { 0, -1, -2 } };
		int LeftDiag[3][3] = { { 0, 1, 2 }, { -1, 0, 1 }, { -2, -1, 0 } };

		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				maskVertical[i][j] = Vertical[i][j];
				maskHorizontal[i][j] = Horizontal[i][j];
				maskRightDiag[i][j] = RightDiag[i][j];
				maskLeftDiag[i][j] = LeftDiag[i][j];
			}
		}

		SobelVertical = new int *[numRows + 2]();
		SobelHorizontal = new int *[numRows + 2]();
		SobelRightDiag = new int *[numRows + 2]();
		SobelLeftDiag = new int *[numRows + 2]();
		SobelVHSum = new int *[numRows + 2]();
		SobelDiagSum = new int *[numRows + 2]();

		for (int i = 0; i < numRows + 2; i++){
			SobelVertical[i] = new int[numCols + 2]();
			SobelHorizontal[i] = new int[numCols + 2]();
			SobelRightDiag[i] = new int[numCols + 2]();
			SobelLeftDiag[i] = new int[numCols + 2]();
			SobelVHSum[i] = new int [numCols + 2]();
			SobelDiagSum[i] = new int [numCols + 2]();
		}
	}

	~SobelEdge(){
		for (int i = 0; i < numRows + 2; i++){
			delete[] SobelVertical[i];
			delete[] SobelHorizontal[i];
			delete[] SobelRightDiag[i];
			delete[] SobelLeftDiag[i];
			delete[] SobelVHSum[i];
			delete[] SobelDiagSum[i];
		}

		delete[] SobelVertical;
		delete[] SobelHorizontal;
		delete[] SobelRightDiag;
		delete[] SobelLeftDiag;
		delete[] SobelVHSum;
		delete[] SobelDiagSum;
	}



	void convolute(int i, int j, int neighbor[][3], int count){
		switch (count){
		    case 1:
				SobelVertical[i][j] = computeCovolution(neighbor, maskVertical);	
				break;
			case 2:
				SobelHorizontal[i][j] = computeCovolution(neighbor, maskHorizontal);
				break;
			case 3:
				SobelRightDiag[i][j] = computeCovolution(neighbor, maskRightDiag);
				break;
			case 4:
				SobelLeftDiag[i][j] = computeCovolution(neighbor, maskLeftDiag);
				break;
			default:
				return;
		}
	}

	int computeCovolution(int neighbor[][3], int mask[][3]){
		int sum = 0;

		for (int i = 0; i < 3; i++){
			for (int j = 0; j < 3; j++){
				sum += (neighbor[i][j] * mask[i][j]);
			}
		}
		return sum;
	}

	void computeSumArray(){
		int tempVHMax = 0, tempVHMin = 0, tempDiagMax = 0, tempDiagMin = 0;
		int counter = 0;

		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				SobelVHSum[i][j] = abs(SobelVertical[i][j]) + abs(SobelHorizontal[i][j]);
				SobelDiagSum[i][j] = abs(SobelRightDiag[i][j]) + abs(SobelLeftDiag[i][j]);
				tempVHMin = SobelVHSum[i][j];
				tempVHMax = SobelVHSum[i][j];
				tempDiagMin = SobelDiagSum[i][j];
				tempDiagMax = SobelDiagSum[i][j];

				if (counter == 0){
					VHMin = SobelVHSum[i][j];
					DiagMin = SobelDiagSum[i][j];
					counter++;
				}

				if (tempVHMin < tempVHMax)
					tempVHMax = tempVHMin;
				if (tempVHMax > VHMax)
					VHMax = tempVHMax;
				if (tempDiagMin < DiagMin)
					DiagMin = tempDiagMin;
				if (tempDiagMax > DiagMax)
					DiagMax = tempDiagMax;
			}
		}
	}

	void outPut(char *arg[], int count){
		ofstream ofs;
		
		if (count == 1){
			ofs.open(arg[2], ofs.app);
			ofs << numRows << " " << numCols << " " << VHMin << " " << VHMax << endl;
			for (int i = 1; i < numRows + 1; i++){
				for (int j = 1; j < numCols + 1; j++){
					ofs << SobelVHSum[i][j] << " ";
				}
				ofs << endl;
			}
		}
		else{
			ofs.open(arg[3], ofs.app);
			ofs << numRows << " " << numCols << " " << DiagMin << " " << DiagMax << endl;
			for (int i = 1; i < numRows + 1; i++){
				for (int j = 1; j < numCols + 1; j++){
					ofs << SobelDiagSum[i][j] << " ";
				}
				ofs << endl;
			}
		}

		ofs.close();
	}
};

class ImageProcessing{
	int** imgAry;
	int numRows, numCols, minVal, maxVal, threaholdValue;
	int** MirrorframedAry;
	int neighbor[3][3];
	SobelEdge* sE;

public:
	ImageProcessing(int r, int c, int mi, int ma, int n){
		numRows = r;
		numCols = c;
		minVal = mi;
		maxVal = ma;
		threaholdValue = n;
		sE = new SobelEdge(r, c);

		imgAry = new int *[numRows];
		MirrorframedAry = new int *[numRows + 2];

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols]();
		}

		for (int i = 0; i < numRows + 2; i++){
			MirrorframedAry[i] = new int[numCols + 2]();
		}
	}

	~ImageProcessing(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}
		for (int i = 0; i < numRows + 2; i++){
			delete[] MirrorframedAry[i];
		}

		delete[] imgAry;
		delete[] MirrorframedAry;
	}

	void loadImage(ifstream& ifs, char *arg[]){
		int data;

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				ifs >> data;
				imgAry[i][j] = data;
			}
		}

		mirrorFramed();
		computeConvolute();
		sE->computeSumArray();
		sE->outPut(arg, 1);
		sE->outPut(arg, 2);
		computeHistogram(arg);
	}

	void computeHistogram(char *arg[]){
		bool t = true, f = false;
		int* VHhistogram = new int[sE->VHMax + 1]();
		int* Diaghistogram = new int[sE->DiagMax + 1]();

		for (int i = 1; i < sE->numRows + 1; i++){
			for (int j = 1; j < sE->numCols + 1; j++){
				VHhistogram[sE->SobelVHSum[i][j]]++;
				Diaghistogram[sE->SobelDiagSum[i][j]]++;
			}
		}

		printHistogram(arg, t, VHhistogram);
		printHistogram(arg, f, Diaghistogram);
	}

	void printHistogram(char *arg[], bool tof, int histo[]){
		ofstream ofs;
		int size = 0;
		
		if (tof){
			ofs.open(arg[2], ofs.app);
			ofs << "------------------------------------------------" << endl;
			ofs << "VHSum Histograam" << endl;
			size = sE->VHMax + 1;;
		}
		else{
			ofs.open(arg[3], ofs.app);
			ofs << "------------------------------------------------" << endl;
			ofs << "DiagSum Histograam" << endl;
			size = sE->DiagMax + 1;
		}


		int counter;

		for (int i = 0; i < size; i++){
			counter = 0;
			if (i < 10)
				ofs << "( " << i << "):" << histo[i] << " ";
			else
				ofs << "(" << i << "):" << histo[i] << " ";

			while (histo[i] > 0)
			{
				counter++;
				histo[i]--;
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
		}

		ofs << "--------------------------------" << endl;
		ofs.close();
		computeThreshold(arg, tof);
	}

	void computeThreshold(char *arg[], bool tof){

		if (tof){
			for (int i = 1; i < sE->numRows + 1; i++){
				for (int j = 1; j < sE->numCols + 1; j++){
					if (sE->SobelVHSum[i][j] < threaholdValue){
						sE->SobelVHSum[i][j] = 0;
					}
					else{
						sE->SobelVHSum[i][j] = 1;
					}
				}
			}
		}
		else{
			for (int i = 1; i < sE->numRows + 1; i++){
				for (int j = 1; j < sE->numCols + 1; j++){
					if (sE->SobelDiagSum[i][j] < threaholdValue){
						sE->SobelDiagSum[i][j] = 0;
					}
					else{
						sE->SobelDiagSum[i][j] = 1;
					}
				}
			}
		}

		prettyPrint(arg, tof);

	}

	void prettyPrint(char *arg[], bool tof){
		ofstream ofs;

		if (tof){
			ofs.open(arg[2], ofs.app);
			ofs << "VHSum Threshold Value: " << threaholdValue << endl;

			for (int i = 1; i < sE->numRows + 1; i++){
				for (int j = 1; j < sE->numCols; j++){
					if (sE->SobelVHSum[i][j] == 0)
						ofs << " ";
					else
						ofs << sE->SobelVHSum[i][j];
				}
				ofs << endl;
			}
			ofs.close();
		}
		else{
			ofs.open(arg[3], ofs.app);
			ofs << "DiagSum Threshold Value: " << threaholdValue << endl;

			for (int i = 1; i < sE->numRows + 1; i++){
				for (int j = 1; j < sE->numCols + 1; j++){
					if (sE->SobelDiagSum[i][j] == 0)
						ofs << " ";
					else
						ofs << sE->SobelDiagSum[i][j];
				}
				ofs << endl;
			}
			ofs.close();
		}
		
	}

	void mirrorFramed(){
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				MirrorframedAry[i][j] = imgAry[i - 1][j - 1];

		int j = 0;
		while (j < numRows + 2){
			MirrorframedAry[0][j] = MirrorframedAry[1][j];
			MirrorframedAry[numRows + 1][j] = MirrorframedAry[numRows][j];
			j++;
		}

		int i = 0;
		while (i < numCols + 2){
			MirrorframedAry[i][0] = MirrorframedAry[i][1];
			MirrorframedAry[i][numCols + 1] = MirrorframedAry[i][numCols];
			i++;
		}
	}

	void computeConvolute(){
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				if (MirrorframedAry[i][j] > 0){
					for (int k = 0; k < 3; k++){
						for (int m = 0; m < 3; m++){
							neighbor[k][m] = MirrorframedAry[i + k - 1][j + m - 1];
						}
					}
					sE->convolute(i, j, neighbor, 1);
					sE->convolute(i, j, neighbor, 2);
					sE->convolute(i, j, neighbor, 3);
					sE->convolute(i, j, neighbor, 4);
				}
			}
		}
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
    int n = atoi(argv[4]);
	ifs >> row >> col >> min >> max;
	
	ImageProcessing zf(row, col, min, max, n);
	zf.loadImage(ifs, argv);

	ifs.close();
	return 0;
}
