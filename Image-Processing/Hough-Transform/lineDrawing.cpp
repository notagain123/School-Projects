#include <iostream>
#include <string>
#include <fstream>
#include <math.h> 
using namespace std;

class ImageProcessing{
	friend class lineDrawing;

	int numRows, numCols, minVal, maxVal, rowTranslate, colTranslate;
	int** imgAry;

public:
	ImageProcessing(){
		numRows = 128;
		numCols = 128;
		minVal = 0;
		maxVal = 1;
		rowTranslate = 128 / 2;
		colTranslate = 128 / 2;

		imgAry = new int*[128]();

		for (int i = 0; i < 128; i++){
			imgAry[i] = new int[128]();
		}

	}

	~ImageProcessing(){
		for (int i = 0; i < 128; i++){
			delete[] imgAry[i];
		}

		delete[] imgAry;
	}

	void outPutImgAry(char *arg[]){
		ofstream ofs;
		ofs.open(arg[1]);
		ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;

		for (int i = 0; i < 128; i++){
			for (int j = 0; j < 128; j++){
				ofs << imgAry[i][j];
			}
			ofs << endl;
		}

		ofs.close();
	}

	void prettyPrint(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2]);

		for (int i = 0; i < 128; i++){
			for (int j = 0; j < 128; j++){
				if (imgAry[i][j] == 0)
					ofs << " ";
				else
					ofs << imgAry[i][j];
			}
			ofs << endl;
		}

		ofs.close();
	}
};

class lineDrawing{
	double angleInDegree, angleInRadians;
	int centerX, centerY;
	ImageProcessing* img;

public:
	lineDrawing(double angle){
		angleInDegree = angle;
		angleInRadians = angleInDegree * 3.14 / 180;
		img = new ImageProcessing();
		centerX = translateRow();
		centerY = translateCol();
	}

	void DrawLine(char *arg[]){
		for (int i = 0; i < img->numRows; i++){
			for (int j = 0; j < img->numCols; j++){
				int y = centerX - i;
				int x = centerY- j;
				
				if (y < 0)
					y = y * -1;
				if(x < 0)
					x= x * -1;
				
				if (angleInDegree <=90){
					if (round((180 * atan2(y, x)) / 3.14) == angleInDegree && ((i <= centerX && j >= centerY) || (i >= centerX && j <= centerY)))
						img->imgAry[i][j] = 2;
				}
				else{
					double ang = 180 - angleInDegree;
					if (round((180 * atan2(y, x)) / 3.14) == ang && ((i <= centerX && j <= centerY) || (i >= centerX && j >= centerY)))
						img->imgAry[i][j] = 2;
				}
			}
		}

		img->imgAry[centerX][centerY] = 1;
		img->outPutImgAry(arg);
		img->prettyPrint(arg);
	}

	int translateRow(){
		return img->rowTranslate;
	}

	int translateCol(){
		return img->colTranslate;
	}
};

int main(int argc, char *argv[]){

	if (argc < 3 || argc > 3){
		cout << "Wrong command argument!" << endl;
		return -1;
	}

	double degree;
	cout << "Please enter a degree from 0 to 179: " << endl;
	cin >> degree;
	lineDrawing lD(degree);
	lD.DrawLine(argv);

	return 0;
}
