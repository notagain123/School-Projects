
public class Queue {
	
	public Node head;
	public Node tail;

	public Queue(){
		head = new Node("dummy");
		tail = head;
	}

	public void addTail(String input){
		Node newNode = new Node(input);
		if (isEmpty()){
			head.setNext(newNode);
		}
		else{
			tail.setNext(newNode);
		}
		tail = newNode;
	}

	public Node deleteHead(){
		Node temp = head.getNext();

		if (!isEmpty()){
			head.setNext(head.getNext().getNext());
		}
		
		return temp;
	}

	public boolean isEmpty(){
		return head.getNext() == null;
	}

	public void printQueue(){
		if (isEmpty()){
			System.out.println("The queue is empty, nothing to print");
		}
		else{
			Node temp = head.getNext();
			while (temp != null){
				System.out.print(temp.getData() + " ");
				temp = temp.getNext();
			}
			System.out.println();
		}
	}

}
