import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

	public static void main(String[] args) {
		
		if(args.length > 4){
			System.out.println("Wrong command line arguments.");
			System.exit(0);
		}
		
		int contestanSize = 0;
		int dateSize = 0;
		int rounds = 0;
		int groupSize = 0;
		
		if(args.length == 0 || args.length == 1 || args.length == 2 || args.length == 3){
			contestanSize = 10;
			dateSize = 6;
			rounds = 3;
			groupSize = 3;
		}else{
		   try{
			  contestanSize = Integer.parseInt(args[0]);
			  dateSize = Integer.parseInt(args[1]);
			  rounds = Integer.parseInt(args[2]);
			  groupSize = Integer.parseInt(args[3]);
			  
		   }catch(Exception e){
			  System.out.println("Please enter integers.");
		   } 
	    }
		
		Thread[] array = new Thread[contestanSize + 1];
	    
		Thread sp = new Thread(new SmartPants(contestanSize, dateSize, rounds, groupSize));
		
		sp.start();
		
		array[contestanSize] = new Thread();
				
		for(int i = contestanSize; i > 0; i--){
			array[i - 1] = new Thread(new Contestant(i, rounds));
			array[i - 1].start();
	    }
		
		for(int i = 0; i < dateSize; i++){
			new Thread(new Date(i + 1)).start();
		}
		
	}

}
