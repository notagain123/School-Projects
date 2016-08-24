
public class hashtable {
	private Queue[] Hashtable;

	public hashtable(){
		Hashtable = new Queue[10];
		for(int i = 0; i < 10; i++){
			Hashtable[i] = new Queue();
		}
	}


	public void printTable(){
		for (int i = 0; i < 10; i++){
			if (!Hashtable[i].isEmpty()){
				System.out.print("Bucket " + i + "-->");
				Hashtable[i].printQueue();
			}
			else{
				System.out.println("Bucket " + i + "-->");
			}
		}
	}
	
	public Queue accessQueue(int i){
		return Hashtable[i];
	}

}
