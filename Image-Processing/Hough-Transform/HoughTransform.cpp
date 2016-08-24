#include <iostream>
#include <math.h>
#include <fstream>
using namespace std;

class ImageProcessing{
	friend class HoughTransform;
	int numRows, numCols, minVal, maxVal;
	int** imgAry;

public:
	ImageProcessing(int r, int c, int min, int max){
		numRows = r;
		numCols = c;
		minVal = min;
		maxVal = max;

		imgAry = new int*[numRows]();

		for (int i = 0; i < numRows; i++){
			imgAry[i] = new int[numCols]();
		}
	}

	~ImageProcessing(){
		for (int i = 0; i < numRows; i++){
			delete[] imgAry[i];
		}

		delete[] imgAry;
	}

	void loadImage(ifstream& ifs, char *arg[]){
		int data;

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				ifs >> data;
				imgAry[i][j] = data;
			}
		}
	}
};

class HoughTransform{
	struct xyCoord{
		int x, y;
	};

	xyCoord point;
	int angleInDegree, numRows, numCols, minVal, maxVal, distance;
	double angleInRadians;
	int** HoughAry;
	double pi;
	ImageProcessing* img;

public:
	HoughTransform(int r, int c, int min, int max){
		img = new ImageProcessing(r, c, min, max);
		numRows = 180;
		numCols = round(sqrt((r * r) + (c * c)));
		minVal = 0;
		maxVal = 0;
		distance = 0;
		pi = 3.1415926;

		HoughAry = new int*[numRows]();

		for (int i = 0; i < numRows; i++){
			HoughAry[i] = new int[numCols]();
		}
	}

	~HoughTransform(){
		for (int i = 0; i < numRows; i++){
			delete[] HoughAry[i];
		}

		delete[] HoughAry;
	}

	void coumpute(ifstream& ifs, char *arg[]){
		img->loadImage(ifs, arg);

		for (int i = 0; i < img->numRows; i++){
			for (int j = 0; j < img->numCols; j++){
				if (img->imgAry[i][j] > 0){
					point.x = j;
					point.y = i;
					for (int a = 0; a < 180; a++){
						angleInRadians = findAngle(point.x, point.y, a);
						distance = computeDistance(point, angleInRadians);
						HoughAry[a][distance]++;
					}
				}
			}
		}

		determineHeader();
		printHoughAry(arg);
		prettyPrint(arg);
	}

	double findAngle(int x, int y, int angle){
		double t;
		t = (angle * pi / 180) - atan2(y, x) - (pi / 2);
		return t;
	}

	int computeDistance(xyCoord p, double Radians){
		int distance;
		distance = round(sqrt((point.x * point.x) + (point.y * point.y)) * cos(Radians));
		if (distance < 0)
			distance = distance * -1;
		
		return distance;
	}

	void determineHeader(){
		int min = 0, max = 0, count = 0;

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				if (count == 0){
					min = HoughAry[i][j];
					count++;
				}
				if (HoughAry[i][j] > max)
					max = HoughAry[i][j];
				if (HoughAry[i][j] < min)
					min = HoughAry[i][j];
			}
		}

		minVal = min;
		maxVal = max;
	}

	void prettyPrint(char *arg[]){
		ofstream ofs;
		ofs.open(arg[3]);

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				if (HoughAry[i][j] == 0)
					ofs << " ";
				else
					ofs << HoughAry[i][j] << " ";
			}
			ofs << endl;
		}

		ofs.close();
	}

	void printHoughAry(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2]);

		ofs << numRows << " " << numCols << " " << minVal << " " << maxVal << endl;

		for (int i = 0; i < numRows; i++){
			for (int j = 0; j < numCols; j++){
				ofs << HoughAry[i][j] << " ";
			}
			ofs << endl;
		}

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

	HoughTransform hF(row, col, min, max);
	hF.coumpute(ifs, argv);

	ifs.close();

	return 0;
}
