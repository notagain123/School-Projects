#include <iostream>
#include <fstream>
using namespace std;

class HeapSort{

private:
	int* heapArray;
	int size;

public:
	HeapSort(int n){
		size = n + 1;
		heapArray = new int[size];
		heapArray[0] = 0;
	}

	~HeapSort(){
		delete[] heapArray;
	}

	void buildHeap(char* arg[]){
		ifstream ifs(arg[1]);
		ofstream ofs;
		ofs.open(arg[2], ofs.app);
		int data;
        
		ofs << "Build Heap:" << endl;
		
		ofs.close();
		
		while (ifs >> data){
			insertOneDataItem(data);
			ofs.open(arg[2], ofs.app);
			
			if(data < 10)
			    ofs << "insert " << data << "           ";
		    else
			    ofs << "insert " << data << "          ";
			
			ofs.close();
			printHeap(arg);
		}

		ifs.close();
	}

	void insertOneDataItem(int number){
		if (!isFull()){
			heapArray[0]++;
			heapArray[heapArray[0]] = number;
			bubbleUp();
		}
		else{
			cout << "The Heap Array is full." << endl;
		}
	}

	void bubbleUp(){
		for (int i = heapArray[0]; i != 1; i /= 2){
			if (heapArray[i] < heapArray[i / 2]){
				swap(heapArray[i], heapArray[i / 2]);
			}
		}
	}

	void deleteRoot(char* arg[]){
        ofstream ofs;
		ofs.open(arg[2], ofs.app);
		
		ofs << "Delete Heap:" << endl;
		
		ofs.close();
		
		if (!isEmpty()){
			while (heapArray[0] > 0){
		        ofs.open(arg[2], ofs.app);
				int temp = heapArray[1];
				
				if(temp < 10)
				    ofs << "delete " << temp << "           ";
				else
					ofs << "delete " << temp << "          ";
				
				ofs.close();
				
				heapArray[1] = heapArray[heapArray[0]];
				heapArray[0]--;
				bubbleDown();
				printHeap(arg);
			}
		}
		else{
			cout << "The Heap Array is empty." << endl;
		}

	}

	void bubbleDown(){
		int i = 1;

		while (i * 2 <= heapArray[0]){
			int min = findMin(heapArray[i * 2], heapArray[i * 2 + 1]);
			if (heapArray[i] > min){
				if (heapArray[i * 2] == min){
					swap(heapArray[i], heapArray[i * 2]);
					i *= 2;
				}//if
				else{
					swap(heapArray[i], heapArray[i * 2 + 1]);
					i = i * 2 + 1;
				}//else if
			}
			else
				break;
		}
	}

	void swap(int &x, int &y){
		int temp = x;
		x = y;
		y = temp;
	}

	int findMin(int x, int y){
		if (x < y)
			return x;
		else
			return y;
	}

	bool isFull(){
		return heapArray[0] == size;
	}

	bool isEmpty(){
		return heapArray[0] == 0;
	}

	void printHeap(char* arg[]){
		ofstream ofs;
		ofs.open(arg[2], ofs.app);

		int totalElements = heapArray[0];
		int count = 0;
         
		while (count < 10 && count != totalElements){
			ofs << heapArray[count + 1] << " ";
			count++;
		}

		ofs << endl;
		ofs.close();
	}
	
	int& operator[](int i){
	    return heapArray[i];
	}

	friend ostream& operator<<(ostream&, const HeapSort& array);
};

ostream& operator<<(ostream& os, HeapSort& array){
	int count = 0;
	
	if(array[0] == 0){
		os << endl << "Final Heap: " << endl; 
	}
	else{
		os << endl << "Final Heap: " << endl;
		for(int i = 0; i < array[0]; i++){
			os << array[i + 1] << " ";
		}
		os << endl << endl;
	}
	
	return os;
};

int main(int argc, char* argv[]){

	if (argc < 3 || argc > 3){
		cout << "Wrong command argument." << endl;
		return -1;
	}

	ifstream ifs(argv[1]);

	if (!ifs){
		cout << "Can't open the file." << endl;
		return -1;
	}

	int data, count = 0;

	while (ifs >> data){
		count++;
	}

	ifs.close();

	HeapSort myArray(count);
    
	ofstream ofs;
	ofs.open(argv[2], ofs.app);
		
	myArray.buildHeap(argv);
	ofs << myArray;
	
	ofs.close();
	
	ofs.open(argv[2], ofs.app);
	
	myArray.deleteRoot(argv);
	ofs << myArray;
	
	ofs.close();

	return 0;
}
