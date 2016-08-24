#include <string>
#include <iostream>
#include <fstream>
using namespace std;

class Node{
private:
	string data;
	Node* next;

public:
	Node(string input){
		data = input;
		next = NULL;
	}

	~Node(){
		delete next;
	}

	string& getData(){
		return data;
	}

	Node* &getNext(){
		return next;
	}

	void setNext(Node* n){
		next = n;
	}
};

class Stack{
private:
	Node* top;

public:
	Stack(){
		top = NULL;
	}

	void push(string input){
		Node* newNode = new Node(input);
		newNode->setNext(top);
		top = newNode;
	}

	Node* pop(){
		Node* temp = top;

		if (!isEmpty()){
			top = top->getNext();
		}

		return temp;
	}

	bool isEmpty(){
		return top == NULL;
	}

	void printStack(){
		if (isEmpty()){
			cout << "The stack is empty, nothing to print." << endl;
		}
		else{
			Node* temp = top;
			while (temp != NULL){
				cout << temp->getData() << " ";
				temp = temp->getNext();
			}
			delete(temp);
			cout << endl;
		}
	}
};

class Queue{

public:
	Node* head;
	Node* tail;

	Queue(){
		head = new Node("dummy");
		tail = head;
	}

	void addTail(string input){
		Node* newNode = new Node(input);
		if (isEmpty()){
			head->setNext(newNode);
		}
		else{
			tail->setNext(newNode);
		}
		tail = newNode;
	}

	Node* deleteHead(){
		Node* temp = head->getNext();

		if (!isEmpty()){
			head->setNext(head->getNext()->getNext());
		}
	
		return temp;
	}

	bool isEmpty(){
		return head->getNext() == NULL;
	}

	void printQueue(){
		if (isEmpty()){
			cout << "The queue is empty, nothing to print" << endl;
		}
		else{
			Node* temp = head->getNext();
			while (temp != NULL){
				cout << temp->getData() << " ";
				temp = temp->getNext();
			}
			delete(temp);
			cout << endl;
		}
	}

};

class hashTable{

private:
	Queue* Hashtable;

public:

	hashTable(){
		Hashtable = new Queue[10];
	}

	~hashTable(){
		delete[] Hashtable;
	}

	void printTable(){
		for (int i = 0; i < 10; i++){
			if (!Hashtable[i].isEmpty()){
				cout << "Bucket " << i << "-->";
				Hashtable[i].printQueue();
			}
			else
			{
				cout << "Bucket " << i << "-->" << endl;
			}
		}
	}

	Queue& operator[](int x){
		return Hashtable[x];
	}
};

class RadixSort{
private:
	hashTable Table[2];
	int totalDigit, currentDigit, currenTable, previousTable, index;
public:
	RadixSort(int t){
		totalDigit = t;
		currentDigit = 0;
		currenTable = 0;
		previousTable = 1;
	}

	void radixSort(Stack s){
		while (currentDigit != totalDigit){
			if (currentDigit == 0){
				while (!s.isEmpty()){
					Node* temp = s.pop();
					Hash(temp->getData());
				}//while
				cout << "\n\nCurrent Table*************************************" << endl;
				Table[currenTable].printTable();
				swapTable();
			}//if
			else{
				cout << "\n\nPrevious Table*************************************" << endl;
				Table[previousTable].printTable();

				for (int i = 0; i < 10; i++){
					while (!Table[previousTable][i].isEmpty()){
						Node* temp = Table[previousTable][i].deleteHead();
						Hash(temp->getData());
					}//while
				}//for
				swapTable();
			}//else
		}//while

		cout << "\n\nCurrent Table*************************************" << endl;
		if (totalDigit % 2 == 0){
			Table[1].printTable();
		}
		else{
			Table[0].printTable();
		}
	}

	void swapTable(){
		int temp = currenTable;
		currenTable = previousTable;
		previousTable = temp;
		currentDigit++;
	}

	void outPutToFile(char* arg[]){
		ofstream ofs;
		ofs.open(arg[2]);
		if (totalDigit % 2 == 0){
			for (int i = 0; i < 10; i++){
				while (!Table[1][i].isEmpty()){
					Node* temp = Table[previousTable][i].deleteHead();
					ofs << temp->getData() << " ";
				}
			}
		}
		else{
			for (int i = 0; i < 10; i++){
				while (!Table[0][i].isEmpty()){
					Node* temp = Table[previousTable][i].deleteHead();
					ofs << temp->getData() << " ";
				}
			}
		}
		ofs << endl;
		ofs.close();
	}

	void Hash(string input){
		int length = input.length();

		if (length < (currentDigit + 1))
			index = 0;
		else
			index = input.at(length - (currentDigit + 1)) - 48;

		if (currenTable % 2 == 0)
			Table[0][index].addTail(input);
		else
			Table[1][index].addTail(input);
	}

};

string findMax(string x, string y){
	if (x.length() > y.length())
		return x;
	else if (y.length() > x.length())
		return y;
	else if (x > y)
		return x;
	else
		return y;
}


int main(int argc, char* argv[]){

	if (argc < 3 || argc > 3){
		cout << "Wrong command argument." << endl;
		return -1;
	}

	ifstream ifs(argv[1]);

	if (!ifs){
		cout << "Can't open the file" << endl;
		return -1;
	}

	Stack s;

	string input, largest;

	while (ifs >> input){
		largest = findMax(input, largest);
		s.push(input);
	}

	cout << "The Stack:" << endl;
	s.printStack();
	ifs.close();

	RadixSort rs(largest.length());
	rs.radixSort(s);
	rs.outPutToFile(argv);

	return 0;
}
