#include <iostream> 
#include <cstdlib>
#include <fstream>
#include <string>
using namespace std;

class LinkedList{
	
	public:
	struct Node {
	    private:
		
		    int data;
		    Node* next;
			
	    public:
		
		    Node(int d){
			    data = d;
			    next = NULL;
		    }
		    int& getData(){
			    return data;
		    }
		    Node* &getNext(){
			    return next;
		    }
			
	};//Node

    private:
	
	    Node* head;
		
    public:	
	
	    LinkedList(){
		    head = new Node(-9999);//dummy node
	    }
		
	    ~LinkedList(){
		    Node* current = head->getNext();
		    while (current != NULL){
			    Node* next = current->getNext();
			    delete current;
			    current = next;
		    }
		    delete head;
	    }
		
	    void Insert(int x){
		    Node* newnode = new Node(x);
		    InsertionSort(newnode);
	    }
		
        void InsertionSort(Node* newnode){
		    Node* current = head;
		    while (current->getNext() != NULL && newnode->getData() > current->getNext()->getData()){
			    current = current->getNext();
		    }
			
			if (current->getNext() != NULL && newnode->getData() == current->getNext()->getData()){
			    cout << newnode->getData() << " is already in the list." << endl;
			    return;
		    }
            else{
			    newnode->getNext() = current->getNext();
			    current->getNext() = newnode;
		    }
	
		}
		
		Node* getHead(){
		    return head;
	    }
		
	   friend ostream& operator<<(ostream& os, const LinkedList& ll);
	   
};

ostream& operator<<(ostream& os, LinkedList& ll){
	int count = 1;
	LinkedList::Node* temp = ll.getHead();
	
	os << "ListHead ";
	
	while (temp->getNext() != NULL && count < 10){
		os << "--> (" << temp->getData() << ", " << temp->getNext()->getData() << ")";
		temp = temp->getNext();
		count++;
	}
	
	if(temp->getNext() == NULL)
	    os << "--> (" << temp->getData() << ", -1)" << endl;
	else 
		os << "--> (" << temp->getData() << ", " << temp->getNext()->getData() << ")" << endl;
	
	return os;
};


int main(int argc, char *argv[]){

	if (argc < 3 || argc > 3){
        cout << "Wrong command argument!" << endl;
        return -1;
	}

    ifstream ifs(argv[1]);

	if (!ifs){
        cout << "Cant' open the intput file" << endl;
        return -1;
	}
	
	ofstream ofs;
	ofs.open(argv[2]);
	
    LinkedList ll;
	int num;
	
	while (ifs >> num){
		ll.Insert(num);
		ofs << ll;
		cout << ll;
	}

	ifs.close();
	ofs.close();

	return 0;
}


