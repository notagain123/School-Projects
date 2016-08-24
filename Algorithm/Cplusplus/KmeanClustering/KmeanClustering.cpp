#include <iostream>
#include <fstream>
#include <string>
#include <cmath>
using namespace std;

class Node{
private:
	int xCoordinate, yCoordinate, clusterLabel;
	double distance; 
	

public:
	Node* next;
	Node(int x, int y, int label){
		xCoordinate = x;
		yCoordinate = y;
		clusterLabel = label;
		distance = 0;
		next = NULL;
	}

	~Node(){
		delete next;
	}

	int& getX(){
		return xCoordinate;
	}

	int& getY(){
		return yCoordinate;
	}

	int& getLabel(){
		return clusterLabel;
	}

	double& getDistance(){
		return distance;
	}

	Node* &getNext(){
		return next;
	}

	void setNext(Node* n){
		next = n;
	}

	void setDistance(double d){
		distance = d;
	}

	void setLabel(int x){
		clusterLabel = x;
	}

	friend ostream& operator<<(ostream& os, const Node& n);
};

ostream& operator<<(ostream& os, Node* n){
	os << n->getX() << " " << n->getY() << " " << n->getLabel() << endl;
	return os;
}



class LinkedList{
private:
	Node* listHead;

public:
	LinkedList(){
		listHead = new Node(-9999, -9999, 0);
	}

	~LinkedList(){
		Node* current = listHead->getNext();
		while (current != NULL){
			Node* next = current->getNext();
			current = next;
		}
		delete (listHead);
	}

	void inerstion(int x, int y, int label){
		Node* newNode = new Node(x, y, label);
		Node* temp = listHead;
		while (temp->getNext() != NULL){
			temp = temp->getNext();
		}
        temp->setNext(newNode);
	}

	void deletion(){
		Node* current = listHead;
		while (current->getNext()->getNext() != NULL){
			current = current->getNext();
		}
		current->setNext(NULL);
		Node* temp = current->getNext();
		delete (temp);
	}

	Node* getHead(){
		return listHead;
	}
	
	friend ostream& operator<<(ostream& os, const LinkedList& ll);
};

ostream& operator<<(ostream& os, LinkedList& ll){
	Node* temp = ll.getHead()->getNext();
	while (temp != NULL){
		os << temp;
		temp = temp->getNext();
	}
	return os;
}

class Kmean{
	struct xycoord{
		int xCoord;
		int yCoord;
	};

private:
	int K, numRow, numCol;
	int **imageArray;
	LinkedList ll;
	xycoord* Kcentroids;
	int minID;
	bool noChange;

public:
	Kmean(int k,int r, int c){
		K = k;
		numRow = r;
		numCol = c;
		Kcentroids = new xycoord[K];
		imageArray = new int *[r];
		minID = 0;
		noChange = false;

		for (int i = 0; i < r; i++)
			imageArray[i] = new int[c]();
	}

	~Kmean(){
		delete[] Kcentroids;

		for (int i = 0; i < numRow; i++)
			delete[] imageArray[i];

		delete[] imageArray;
	}

	void kMean(ifstream& ifs, char* arg[]){
		int inputX, inputY, counter = 1;

		while (ifs >> inputX){
			if (counter > K)
				counter = 1;

			ifs >> inputY;
			ll.inerstion(inputX, inputY, counter);

			counter++;
		}

		outputList(arg);
		fillInArray();
		displayImage(arg);

		while (!noChange){
			restCentroid();
			computeCentroid();
			computeKdistance();
			outputList(arg);
			fillInArray();
			displayImage(arg);
		}

	}

	void fillInArray(){
		Node* temp = ll.getHead()->getNext();
		while (temp != NULL){
			imageArray[temp->getX()][temp->getY()] = temp->getLabel();
			temp = temp->getNext();
		}
	}

	void restCentroid(){
		for (int i = 0; i < K; i++){
			Kcentroids[i].xCoord = 0;
			Kcentroids[i].yCoord = 0;
		}
	}

	void computeCentroid(){
		int *n = new int[K]();
		Node* temp = ll.getHead()->getNext();
		while (temp != NULL){
			Kcentroids[temp->getLabel() - 1].xCoord += temp->getX();
			Kcentroids[temp->getLabel() - 1].yCoord += temp->getY();
			n[temp->getLabel() - 1]++;
			temp = temp->getNext();
		}
		for (int i = 0; i < K; i++){
			Kcentroids[i].xCoord /= n[i];
			Kcentroids[i].yCoord /= n[i];
		}
		delete [] n;
	}

	void computeKdistance(){
		int count = 0;
		Node* temp = ll.getHead()->getNext();
		while (temp != NULL){
			temp->setDistance(minDistance(temp->getX(), temp->getY(), temp));
			if (temp->getLabel() != (minID + 1)){
				temp->setLabel(minID + 1);
				count++;
			}
			temp = temp->getNext();
		}
	
		if (count == 0)
			noChange = true;

	}

	double minDistance(int x, int y, Node* n){
		double min;
		double *distance = new double[K];
		minID = 0;
		for (int i = 0; i < K; i++){
			distance[i] = sqrt((Kcentroids[i].xCoord - x) * (Kcentroids[i].xCoord - x) +
				(Kcentroids[i].yCoord - y) * (Kcentroids[i].yCoord - y));
		}

		min = distance[0];
		for (int i = 0; i < K; i++){
			if (min > distance[i]){
				min = distance[i];
				minID = i;
			}
		}
		delete [] distance;
		return min;
	}

	void outputList(char* arg[]){
		ofstream ofs;
		ofs.open(arg[2], ofs.app);

		if (noChange)
			ofs << "Final List: " << endl;

		ofs << K << endl;
		ofs << numRow << " " << numCol << endl;

		ofs << ll;

		if (!noChange)
			ofs << "--------------------------------------" << endl;
		
		ofs.close();
	}

	void displayImage(char* arg[]){
		ofstream ofs;
		ofs.open(arg[3], ofs.app);

		if (noChange)
			ofs << "Final image: " << endl;

		for (int i = 0; i < numRow; i++){
			for (int j = 0; j < numCol; j++){
				if (imageArray[i][j] != 0)
					ofs << imageArray[i][j];
				else
					ofs << " ";
			}
			ofs << endl;
		}

		if (!noChange)
			ofs << "--------------------------------------" << endl;

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
	int k, r, c;

	ifs >> k;
	ifs >> r;
	ifs >> c;

	Kmean km(k, r, c);

	km.kMean(ifs, argv);

	ifs.close();

	return 0;
}
