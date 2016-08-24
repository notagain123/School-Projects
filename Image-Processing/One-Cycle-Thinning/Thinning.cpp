#include <iostream>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
using namespace std;

class IMAGE{
	int** imgAry;
	int numRows, numCols, minVal, maxVal, threaholdValue;
	int** ZeroframedAry;
	bool changeflag;
	int** FirstAry;
	int** SecondAry;
	int counter;

public:
	IMAGE(int r, int c, int mi, int ma, int n){
		numRows = r;
		numCols = c;
		minVal = mi;
		maxVal = ma;
		changeflag = true;
		counter = 0;

		imgAry = new int *[numRows];
		ZeroframedAry = new int *[numRows + 2];
		FirstAry = new int *[numRows + 2];
		SecondAry = new int *[numRows + 2];

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols]();
		}

		for (int i = 0; i < numRows + 2; i++){
			ZeroframedAry[i] = new int[numCols + 2]();
			FirstAry[i] = new int[numCols + 2]();
			SecondAry[i] = new int[numCols + 2]();
		}

	}

	~IMAGE(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}
		for (int i = 0; i < numRows + 2; i++){
			delete[] ZeroframedAry[i];
			delete[] FirstAry[i];
			delete[] SecondAry[i];
		}

		delete[] imgAry;
		delete[] ZeroframedAry;
		delete[] FirstAry;
		delete[] SecondAry;
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

		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				FirstAry[i][j] = ZeroframedAry[i][j];
				SecondAry[i][j] = ZeroframedAry[i][j];
			}
		}

		OneCycleThinning(arg);
		prettyPrint(arg, false, true, 0);

	}

	void prettyPrint(char *arg[], bool firstCycle, bool result, int n){
		ofstream ofs;

		if (firstCycle){
			if (n == 1){
				ofs.open(arg[3], ofs.app);
			    ofs << "First Cycle North Thinning:" << endl;
		     }
		    else if (firstCycle && n == 2){
				ofs.open(arg[3], ofs.app);
				ofs << "First Cycle South Thinning:" << endl;
			}
			else if (firstCycle && n == 3){
				ofs.open(arg[3], ofs.app);
				ofs << "First Cycle West Thinning:" << endl;
			}
			else if (firstCycle && n == 4){
				ofs.open(arg[3], ofs.app);
				ofs << "First Cycle East Thinning:" << endl;
			}
		}

		if (!firstCycle && !result){
			ofs.open(arg[3], ofs.app);
			ofs <<  counter << " Cycle Thinning Second Array: " << endl;
		}

		if (result){
			ofs.open(arg[2], ofs.app);
			ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;
		}
		
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				if (!result){
					if (SecondAry[i][j] == 0)
						ofs << " ";
					else
						ofs << SecondAry[i][j];
				}
				else
					ofs << SecondAry[i][j];
			}
			ofs << endl;
		}

		ofs << endl;

		if (!result)
			ofs << "--------------------------------" << endl;

		ofs.close();
	}

	void zeroFramed(){
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				ZeroframedAry[i][j] = imgAry[i - 1][j - 1];
	}

	void OneCycleThinning(char *arg[]){
		bool tof = true;
		while(changeflag){
			changeflag = false;
			NorthThinning();
			if (tof)
				prettyPrint(arg, true, false, 1);

			SouthThinning();
			if (tof)
				prettyPrint(arg, true, false, 2);

			WestThinning();
			if (tof)
				prettyPrint(arg, true, false, 3);

			EastThinning();
			if (tof)
				prettyPrint(arg, true, false, 4);

			tof = false;
			counter++;
			if (counter % 3 == 0)
				prettyPrint(arg, false, false, 0);
			copyAry();
		}
	}

	void NorthThinning(){
		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				if (FirstAry[i][j] > 0 && FirstAry[i - 1][j] == 0){
					if (countZero(i, j) <= 3){
						SecondAry[i][j] = 0;
						changeflag = true;
					}
					else
						SecondAry[i][j] = FirstAry[i][j];
				}
			}
		}
	}

	void SouthThinning(){
		for (int i = numRows + 1; i > 0; i--){
			for (int j = numCols + 1; j > 0; j--){
				if (FirstAry[i][j] > 0 && FirstAry[i + 1][j] == 0){
					if (countZero(i, j) <= 3){
						SecondAry[i][j] = 0;
						changeflag = true;
					}
					else
						SecondAry[i][j] = FirstAry[i][j];
				}
			}
		}
	}

	void WestThinning(){
		for (int j = 1; j < numCols + 1; j++){
			for (int i = 1; i < numRows + 1; i++){
				if (FirstAry[i][j] > 0 && FirstAry[i][j - 1] == 0){
					if (countZero(i, j) <= 3){
						SecondAry[i][j] = 0;
						changeflag = true;
					}
					else
						SecondAry[i][j] = FirstAry[i][j];
				}
			}
		}
	}

	void EastThinning(){
		for (int j = numCols + 1; j > 0; j--){
			for (int i = 1; i < numRows + 1; i++){
				if (FirstAry[i][j] > 0 && FirstAry[i][j + 1] == 0){
					if (countZero(i, j) <= 3){
						SecondAry[i][j] = 0;
						changeflag = true;
					}
					else
						SecondAry[i][j] = FirstAry[i][j];
				}
			}
		}
	}

	int countZero(int i, int j){
		int count = 0;
		for (int n = i - 1; n < i + 2; n++){
			for (int m = j - 1; m < j + 2; m++){
				if (FirstAry[n][m] == 0){
					count++;
				}
			}
		}

		return count;
	}

	void copyAry(){
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				FirstAry[i][j] = SecondAry[i][j];
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
	
	IMAGE zf(row, col, min, max, 2);
	zf.loadImage(ifs, argv);

	ifs.close();

	return 0;
}
