#include <iostream>
#include <fstream>
using namespace std;

class graphNode{
private:
	int nodeID;
	graphNode* next;
public:
	graphNode(int id){
		nodeID = id;
		next = NULL;
	}

	~graphNode(){
		delete next;
	}

	int& getID(){
		return nodeID;
	}

	graphNode* &getNext(){
		return next;
	}

	void setNext(graphNode* n){
		next = n;
	}
};

class edgeNode{
private:
	int Ni;
	int Nj;
	int edgeCost;
	edgeNode* next;

public:
	edgeNode(int i, int j, int cost){
		Ni = i;
		Nj = j;
		edgeCost = cost;
		next = NULL;
	}

	~edgeNode(){
		delete next;
	}

	int& getNi(){
		return Ni;
	}

	int& getNj(){
		return Nj;
	}

	int& getCost(){
		return edgeCost;
	}

	edgeNode* &getNext(){
		return next;
	}

	void setNext(edgeNode* n){
		next = n;
	}

	friend ostream& operator<<(ostream& os, const edgeNode& n);
};

ostream& operator<<(ostream& os, edgeNode* n){
	os << n->getNi() << " " << n->getNj() << " " << n->getCost() << endl;
	return os;
}

class graphList{
private:
	graphNode* head;
public:
	graphList(){
		head = new graphNode(-999);
	}

	~graphList(){
		graphNode* current = head->getNext();
		while (current != NULL){
			graphNode* next = current->getNext();
			current = next;
		}
		delete (head);
	}

	void insertion(int id){
		graphNode* newNode = new graphNode(id);
		graphNode* temp = head;
		while (temp->getNext() != NULL){
			temp = temp->getNext();
		}
		temp->setNext(newNode);
	}

	int deletion(int id){
		graphNode* temp = head->getNext();
		graphNode* current = head;
		while (temp != NULL){
			if (id == temp->getID()){
				current->setNext(temp->getNext());
				break;
			}
			current = current->getNext();
			temp = temp->getNext();
		}

		return temp->getID();
	}

	bool search(int n){
		graphNode* temp = head->getNext();
		while (temp != NULL){
			if (n == temp->getID())
				return true;

			temp = temp->getNext();
		}
		return false;
	}

	bool isEmpty(){
		return head->getNext() == NULL;
	}

	graphNode* getHead(){
		return head;
	}

	friend ostream& operator<<(ostream& os, const graphList& gnl);
};

ostream& operator<<(ostream& os, graphList& gnl){
	graphNode* temp = gnl.getHead()->getNext();
	while (temp != NULL){
		os << temp->getID() << endl;
		temp = temp->getNext();
	}
	return os;
}

class edgeList{
private:
	edgeNode* head;
public:
	edgeList(){
		head = new edgeNode(-999, -999, -999);
	}

	~edgeList(){
		edgeNode* current = head->getNext();
		while (current != NULL){
			edgeNode* next = current->getNext();
			current = next;
		}
		delete (head);
	}

	void insertion(int i, int j, int cost){
 		edgeNode* newNode = new edgeNode(i, j, cost);
		edgeNode* temp = head;
		while (temp->getNext() != NULL && newNode->getCost() > temp->getNext()->getCost()){
			temp = temp->getNext();
		}
		newNode->setNext(temp->getNext());
		temp->setNext(newNode);
	}

	void deletion(edgeNode* n){
		edgeNode* temp = head->getNext();
		edgeNode* current = head;
		while (temp != NULL){
			if (temp == n){
				current->setNext(temp->getNext());
				return;
			}
			temp = temp->getNext();
			current = current->getNext();
		}

	}

	edgeNode* getHead(){
		return head;
	}

	bool isEmpty(){
		return head->getNext() == NULL;
	}

	friend ostream& operator<<(ostream& os, const edgeList& enl);
};

ostream& operator<<(ostream& os, edgeList& enl){
	edgeNode* temp = enl.getHead()->getNext();
	while (temp != NULL){
		os << temp;
		temp = temp->getNext();
	}
	return os;
}

class PrimMST{
private:
	int N;
	int totalCost;
	int *graphNodeIDArray;
	edgeList el;
	edgeList MSTofG;
	graphList setA;
	graphList setB;
public:
	PrimMST(int n){
		N = n;
		totalCost = 0;
		graphNodeIDArray = new int[n]();
	}

	~PrimMST(){
		delete[] graphNodeIDArray;
	}

	void prims(ifstream& ifs, char *arg[]){
		int i, j, cost;
		while (ifs >> i){
			ifs >> j;
			ifs >> cost;
			el.insertion(i, j, cost);
			graphNodeIDArray[i - 1]++;
			graphNodeIDArray[j - 1]++;
		}

		printNodeArray(arg);

		initializeSet();
		findMST(arg);
		print(arg);

	}

	void findMST(char *arg[]){
		while (!setB.isEmpty()){
			edgeNode* temp = el.getHead()->getNext();
			while (temp != NULL){
				if ((setA.search(temp->getNi()) && setA.search(temp->getNj())) ||
					(setB.search(temp->getNi()) && setB.search(temp->getNj()))){
					temp = temp->getNext();
					continue;
				}
				else{
					if (!setA.search(temp->getNi())){
						setA.insertion(setB.deletion(temp->getNi()));
					}
					else{
						setA.insertion(setB.deletion(temp->getNj()));
					}
					el.deletion(temp);
					totalCost += temp->getCost();
					MSTofG.insertion(temp->getNi(), temp->getNj(), temp->getCost());
					printOutput2(arg);
					break;
				}
			}
		}
	}

	void initializeSet(){
		int firstNoneZero = 0;

		for (int i = 0; i < N; i++){
			if (graphNodeIDArray[i] != 0 && firstNoneZero == 0){
				firstNoneZero = 1;
				setA.insertion(i + 1);
			}
			else if (graphNodeIDArray[i] != 0){
				setB.insertion(i + 1);
			}

		}
	}

	void printNodeArray(char *arg[]){
		ofstream ofs;
		ofs.open(arg[3]);
		ofs << "Graph Node ID Array: " << endl;
		for (int i = 0; i < N; i++){
			ofs << "Node: " << (i + 1) << " " << graphNodeIDArray[i] << endl;
		}

		ofs << endl << "Edge List: " << endl;
		ofs << el;
		ofs << "-------------------" << endl;

		ofs.close();
	}

	void printOutput2(char *arg[]){
		ofstream ofs;
		ofs.open(arg[3], ofs.app);
		ofs << "Set A: " << endl;
		ofs << setA;
		ofs << "Set B: " << endl;
		ofs << setB;
		ofs << "MSTofG: " << endl;
		ofs << MSTofG << endl << endl;
		ofs.close();
	}

	void print(char *arg[]){
		ofstream ofs;
		ofs.open(arg[2]);
		ofs << N << endl;
		ofs << MSTofG;
		ofs << "Total Cost is: " << totalCost << endl;

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

	int n;
	ifs >> n;
	PrimMST p(n);
	p.prims(ifs, argv);

	return 0;
}
