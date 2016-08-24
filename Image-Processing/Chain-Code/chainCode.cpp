#include <iostream>
#include <string>
#include <fstream>
#include <stdio.h>
#include <stdlib.h>
using namespace std;

class ImageProcessing{
	friend class chainCode;

	int** imgAry;
	int numRows, numCols, minVal, maxVal;
	int** ZeroframedAry;

public:
	ImageProcessing(int r, int c, int mi, int ma){
		numRows = r;
		numCols = c;
		minVal = mi;
		maxVal = ma;

		imgAry = new int *[numRows];
		ZeroframedAry = new int *[numRows + 2];

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols]();
		}

		for (int i = 0; i < numRows + 2; i++){
			ZeroframedAry[i] = new int[numCols + 2]();
		}

	}

	~ImageProcessing(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}
		for (int i = 0; i < numRows + 2; i++){
			delete[] ZeroframedAry[i];
		}

		delete[] imgAry;
		delete[] ZeroframedAry;
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
		prettyPrint(arg);
	}

	void prettyPrint(char *arg[]){
		ofstream ofs;
		ofs.open(arg[3]);

		for (int i = 1; i < numRows + 1; i++){
			for (int j = 1; j < numCols + 1; j++){
				if (ZeroframedAry[i][j] == 0)
					ofs << " ";
				else
					ofs << ZeroframedAry[i][j];
			}
			ofs << endl;
		}
		ofs.close();
	}

	void zeroFramed(){
		for (int i = 1; i < numRows + 1; i++)
			for (int j = 1; j < numCols + 1; j++)
				ZeroframedAry[i][j] = imgAry[i - 1][j - 1];
	}

};

class chainCode{
	struct pixel{
		int rowPosition;
		int colPosition;
	};

	pixel firstNoneZero;
	int label;
	int lastZeroTable[8];
	int lastZeroDir;
	int nextDir;
	pixel currPixel;
	pixel nextPixel;
	int chainDir;
	ImageProcessing* img;
	int neighborTable[8][2];

public:
	chainCode(int r, int c, int min, int max, ifstream& ifs, char *arg[]){
		img = new ImageProcessing(r, c, min, max);
		img->loadImage(ifs, arg);
		lastZeroDir = 4;
		label = 1;
		int lastZero[8] = { 6, 0, 0, 2, 2, 4, 4, 6 };
		int neighobor[8][2] = { {0,1}, {-1,1}, {-1,0}, {-1,-1}, {0,-1}, {1,-1}, {1,0}, {1,1} };

		for (int i = 0; i < 8; i++){
			lastZeroTable[i] = lastZero[i];
		}

		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 2; j++){
				neighborTable[i][j] = neighobor[i][j];
			}
		}
	}

	void computeChainCode(char *arg[]){
		int count = 0;
		int temp = 0;

		for (int i = 1; i < img->numRows + 1; i++){
			for (int j = 1; j < img->numCols + 1; j++){
				if (img->ZeroframedAry[i][j] == label){
					string output = "";
					firstNoneZero = findNextComponent(i ,j);
					while (!samePixel()){
						if (count == 0){
							currPixel = firstNoneZero;
						}
						nextDir = lastZeroDir + 1;
						chainDir = findNextNoneZeroNeighbor(nextDir);
						temp = findNextDir(chainDir);
						currPixel = nextPixel;
						lastZeroDir = lastZeroTable[temp];
						char c = chainDir + 48;
						string s(1, c);
						output = output + s + " ";
						count++;
					}
					count = 0;
					prettyPrint(arg, output);
				}
			}
		}
	}

	pixel findNextComponent(int i, int j){
		pixel temp;
		temp.rowPosition = i;
		temp.colPosition = j;
		label++;
		return temp;
	}

	int findNextNoneZeroNeighbor(int lastZeroPlusOne){
		int temp = 0;
		bool found = false;

		for (int i = 0; i < 8; i++){
			int dir = (i + lastZeroPlusOne) % 8;
			if (img->ZeroframedAry[currPixel.rowPosition + neighborTable[dir][0]][currPixel.colPosition + neighborTable[dir][1]] > 0){
				temp = dir;
				break;
			}
		}
	
		nextPixel.rowPosition = currPixel.rowPosition + neighborTable[temp][0];
		nextPixel.colPosition = currPixel.colPosition + neighborTable[temp][1];

		return temp;
	}

	int findNextDir(int x){
		if (x == 0)
			return 7;
		else
			return x - 1;
	}

	bool samePixel(){
		return (currPixel.rowPosition == firstNoneZero.rowPosition) && (currPixel.colPosition == firstNoneZero.colPosition);
	}

	void prettyPrint(char *arg[], string output){
		ofstream ofs;
		ofs.open(arg[2], ofs.app);

		ofs << "rowPosition: " << firstNoneZero.rowPosition << " " << "colPosition: " << firstNoneZero.colPosition << " "
            << "Label: " << img->ZeroframedAry[firstNoneZero.rowPosition][firstNoneZero.colPosition] << " " << "Chain Code: " << output << endl << endl;

		ofs.close();
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

	chainCode cC(row, col, min, max, ifs, argv);
	cC.computeChainCode(argv);

	ifs.close();

	return 0;
}
