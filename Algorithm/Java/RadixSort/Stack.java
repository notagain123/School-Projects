
public class Stack {

	private Node top;

	public Stack(){
		top = null;
	}

	public void push(String input){
	    Node newNode = new Node(input);
	    newNode.setNext(top);
	    top = newNode;
	}

	public Node pop(){
		Node temp = top;

		if (!isEmpty()){
			top = top.getNext();
		}

		return temp;
	}

	public boolean isEmpty(){
		return top == null;
	}

	public void printStack(){
		if (isEmpty()){
			System.out.println("The stack is empty, nothing to print.");
		}
		else{
			Node temp = top;
			while (temp != null){
				System.out.print(temp.getData() + " ");
				temp = temp.getNext();
				}
			System.out.println();
		}
	}

}
