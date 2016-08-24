#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
using namespace std;

class IMAGE{
	int** imgAry;
	int numRows, numCols, minVal, maxVal, threaholdValue;
	int framingRow, framingCol;
	int** ZeroframedAry;
	int* EQAry;
	int newLable;
	int size;
	int counter;
public:
	IMAGE(int r, int c, int mi, int ma){
		numRows = r;
		numCols = c;
		framingRow = (r + 2);
		framingCol = (c + 2);
		minVal = mi;
		maxVal = ma;
		newLable = 0;
		size = (r * c) / 4;
		counter = 1;

		imgAry = new int *[numRows];
		ZeroframedAry = new int *[framingRow];
		EQAry = new int[size];

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols];
		}

		for (int i = 0; i < size; i++){
			EQAry[i] = i;
		}

		for (int i = 0; i < framingRow; i++){
			ZeroframedAry[i] = new int[framingCol]();
		}
	}

	~IMAGE(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}
		for (int i = 0; i < framingRow; i++){
			delete[] ZeroframedAry[i];
		}

		delete[] imgAry;
		delete[] ZeroframedAry;
		delete[] EQAry;
	}

	void loadImage(ifstream& ifs, char *arg[]){
		char data;

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				ifs >> data;
				imgAry[i][j] = data - 48;
			}
		}

		zeroFramed();
		firstPass8conn(arg);
		secondPass8conn(arg);
		thirdPass8conn(arg);
		binaryOutput(arg);
	}

	void prettyPrint(char *arg[], bool manage){
		ofstream ofs;

		ofs.open(arg[2], ofs.app);

		for (int i = 1; i < framingRow - 1; i++){
			for (int j = 1; j < framingCol - 1; j++){
				if (ZeroframedAry[i][j] == 0)
					ofs << " ";
				else
					ofs << ZeroframedAry[i][j];
			}
			ofs << endl;
		}
		ofs << "--------------------------------" << endl;
		if (!manage)
		   ofs << "EQ Array:" << endl;
		else
			ofs << "Managed EQ Array:" << endl;

		for (int i = 1; i < newLable + 1; i++)
			ofs << EQAry[i] << " ";

		ofs << endl;
		ofs << "--------------------------------" << endl;
		ofs.close();
	}

	void zeroFramed(){
		for (int i = 1; i < framingRow - 1; i++)
			for (int j = 1; j < framingCol - 1; j++)
				ZeroframedAry[i][j] = imgAry[i - 1][j - 1];
	}

	void firstPass8conn(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2], ofs.app);
		ofs << "First Pass:" << endl;
		ofs.close();

		for (int i = 1; i < framingRow - 1; i++){
			for (int j = 1; j < framingCol - 1; j++){
				if (ZeroframedAry[i][j] == 1){
					int min, max;
					min = findMin(findMin(ZeroframedAry[i - 1][j - 1], ZeroframedAry[i - 1][j]),
						findMin(ZeroframedAry[i - 1][j + 1], ZeroframedAry[i][j - 1]));
					max = findMax(findMax(ZeroframedAry[i - 1][j - 1], ZeroframedAry[i - 1][j]),
						findMax(ZeroframedAry[i - 1][j + 1], ZeroframedAry[i][j - 1]));
					if (min == 0 && max == 0)
						ZeroframedAry[i][j] = ++newLable;
					else{
						ZeroframedAry[i][j] = min;
						updateEQAry(min, i, j, false);
					}
				}
			}
		}

		prettyPrint(arg, false);
	}

	void secondPass8conn(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2], ofs.app);
		ofs << "Second Pass:" << endl;
		ofs.close();

		for (int i = framingRow - 1; i > 0; i--){
			for (int j = framingCol - 1; j > 0; j--){
				if (ZeroframedAry[i][j] > 0){
					int min, max;
					min = findMin(ZeroframedAry[i][j], findMin(findMin(ZeroframedAry[i][j + 1],
						ZeroframedAry[i + 1][j - 1]), findMin(ZeroframedAry[i + 1][j],
						ZeroframedAry[i + 1][j + 1])));
					max = findMax(ZeroframedAry[i][j], findMax(findMax(ZeroframedAry[i][j + 1],
						ZeroframedAry[i + 1][j - 1]), findMax(ZeroframedAry[i + 1][j],
						ZeroframedAry[i + 1][j + 1])));
					if (min != 0){
						ZeroframedAry[i][j] = min;
						updateEQAry(min, i, j, true);
					}
				}
			}
		}

		prettyPrint(arg, false);
	}

	void thirdPass8conn(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2], ofs.app);
		ofs << "Third Pass:" << endl;
		ofs.close();

		manageEQAry();
		for (int i = 1; i < framingRow - 1; i++){
			for (int j = 1; j < framingCol - 1; j++){
				if (ZeroframedAry[i][j] > 0)
					ZeroframedAry[i][j] = EQAry[ZeroframedAry[i][j]];
			}
		}

		prettyPrint(arg, true);
	}

	void updateEQAry(int min, int i, int j, bool self){
		if (!self){
			if (EQAry[ZeroframedAry[i - 1][j - 1]] > min)
				EQAry[ZeroframedAry[i - 1][j - 1]] = min;
			if (EQAry[ZeroframedAry[i - 1][j]] > min)
				EQAry[ZeroframedAry[i - 1][j]] = min;
			if (EQAry[ZeroframedAry[i - 1][j + 1]] > min)
				EQAry[ZeroframedAry[i - 1][j + 1]] = min;
			if (EQAry[ZeroframedAry[i][j - 1]] > min)
				EQAry[ZeroframedAry[i][j - 1]] = min;
		}
		else{
			if (EQAry[ZeroframedAry[i][j + 1]] > min)
				EQAry[ZeroframedAry[i][j + 1]] = min;
			if (EQAry[ZeroframedAry[i + 1][j]] > min)
				EQAry[ZeroframedAry[i + 1][j]] = min;
			if (EQAry[ZeroframedAry[i + 1][j + 1]] > min)
				EQAry[ZeroframedAry[i + 1][j + 1]] = min;
			if (EQAry[ZeroframedAry[i + 1][j - 1]] > min)
				EQAry[ZeroframedAry[i + 1][j - 1]] = min;
			if (EQAry[ZeroframedAry[i][j]] > min)
				EQAry[ZeroframedAry[i][j]] = min;
		}
	}

	void manageEQAry(){
		for (int i = 1; i < newLable + 1; i++){
			int temp = EQAry[i];
			EQAry[i] = EQAry[EQAry[i]];
			if (EQAry[i] != EQAry[1] && i <= temp){
				EQAry[i] = ++counter;
			}
		}
	}

	int findMin(int a, int b){
		if (a == 0)
			return b;
		if (b == 0)
			return a;

		if (a > b)
			return b;
		else
			return a;
	}

	int findMax(int a, int b){
		if (a == 0)
			return b;
		if (b == 0)
			return a;

		if (a > b)
			return a;
		else
			return b;
	}

	void binaryOutput(char *arg[]){
		ofstream ofs;

		ofs.open(arg[3]);

		ofs << numRows << " " << numCols << " " << minVal << " " << counter << endl;
		for (int i = 1; i < framingRow - 1; i++){
			for (int j = 1; j < framingCol - 1; j++){
				ofs << ZeroframedAry[i][j];
			}
			ofs << endl;
		}
	}

};

int main(int argc, char *argv[]){

	if (argc < 4 || argc > 4){
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

	IMAGE zf(row, col, min, max);
	zf.loadImage(ifs, argv);

	ifs.close();

	return 0;
}
