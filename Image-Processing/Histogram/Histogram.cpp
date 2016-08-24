#include <iostream>
#include <fstream>
using namespace std;

class IMAGE{
	int** imgAry;
	int** tempAry;
	int* histogram;
	int row, col, min, max;
public:
	IMAGE(int r, int c, int mi, int ma){
		row = r;
		col = c;
		min = mi;
		max = ma;

		imgAry = new int *[row];
		tempAry = new int *[row];

		for (int i = 0; i < row; i++){
			imgAry[i] = new int[col];
			tempAry[i] = new int[col];
		}

		histogram = new int[max + 1]();
	}

	~IMAGE(){
		for (int i = 0; i < row; i++){
			delete[] imgAry[i];
			delete[] tempAry[i];
		}

		delete[] imgAry;
		delete[] tempAry;
		delete[] histogram;
	}

	void loadImage(ifstream& ifs, char *arg[]){
		int data;

		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
				ifs >> data;
				imgAry[i][j] = data;
			}
		}

		computeHistogram();
		printHistogram(arg);
		computeThreshold(arg);
	}

	void computeHistogram(){
		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
				histogram[imgAry[i][j]]++;
			}
		}
	}

	void printHistogram(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2]);

		int counter;

		for (int i = 0; i < max + 1; i++){
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

			if (counter > 80)
				ofs << "80 + 's" << endl;
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
		int value[] = {15, 30, 45, 40};

		for (int k = 0; k < 4; k++){
			copyArray();
		    for (int i = 0; i < row; i++){
			    for (int j = 0; j < col; j++){
				    if (tempAry[i][j] < value[k]){
						tempAry[i][j] = 0;
				    }
				    else{
						tempAry[i][j] = 1;
				    }
			   }
		   }
			prettyPrint(arg, value[k]);
	    }

	}

	void prettyPrint(char *arg[], int n){
		ofstream ofs;

        ofs.open(arg[2], ofs.app);

		ofs << "Threshold value: " << n << endl; 
		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
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

	void copyArray(){
		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++)
				tempAry[i][j] = imgAry[i][j];
		}
	}
	
	void zeroFramed(int data){
		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
				if (i == 0 || j == 0 || i == (row - 1) || j == (col - 1))
					imgAry[i][j] = 0;
				else{
					imgAry[i][j] = data;
				}
			}
		}
	}
	
	void mirrorFramed(int data){
		for (int i = 1; i < row - 1; i++){
			for (int j = 1; j < col - 1; j++){
				imgAry[i][j] = data;
			}
		}

		for (int i = 0; i < row; i++){
			for (int j = 0; j < col; j++){
				if (i == 0 && j == 0)
					imgAry[i][j] = imgAry[i + 1][j + 1];
				else if (i == 0 && j == col - 1)
					imgAry[i][j] = imgAry[i + 1][j - 1];
				else if (i == row - 1 && j == 0)
					imgAry[i][j] = imgAry[i - 1][j + 1]; 
				else if (i == 0)
					imgAry[i][j] = imgAry[i + 1][j];
				else if (j == 0)
					imgAry[i][j] = imgAry[i][j + 1];
				else if (i == row - 1)
					imgAry[i][j] = imgAry[i - 1][j];
				else if (j == col - 1)
					imgAry[i][j] = imgAry[i][j - 1];
			}
		}
	}
};

int main(int argc, char *argv[]){

	if (argc < 3 || argc > 3){
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
