
public class Node {
	private String data;
	private Node next;

	public Node(String input){
			data = input;
			next = null;
	}
	
	public String getData(){
		return data;
	}
	
	public Node getNext(){
		return next;
	}
	
	public void setNext(Node n){	
		next = n;		
	}

}
