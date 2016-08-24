import java.util.Random;
import java.util.concurrent.Semaphore;


public class SmartPants extends Thread{

    public static boolean[] talk;
    public static boolean[] approach;
    public static String[] information;
    public static boolean[] hasArrived;
    public static int availableDate;
    public static int group_size;
    public static int numberOfContestant;
    public static int totalContestant;
    public static Semaphore gameEnd = new Semaphore(0); 
    public static int contestant_done;
    public volatile static boolean show_end = false;
    public static int talkTo = 0;
    public static long time = System.currentTimeMillis();    
    private Random random = new Random();

    
    public SmartPants(int contestantNumber, int dateNumber, int rounds, int size){
    	totalContestant = contestantNumber;
    	availableDate = dateNumber;
    	setName("SmartPants");
     	talk = new boolean[contestantNumber + 1];
     	approach = new boolean[contestantNumber + 1];
     	information = new String[contestantNumber + 1];
     	contestant_done = contestantNumber;
     	group_size = size;
     	hasArrived = new boolean[contestantNumber + 1];
    }
    
	public void run() {
		msg("is ready to start the show!");
		while(talkTo != totalContestant){//looping until SmartPants has talked to every contestant
			int x = random.nextInt(totalContestant) + 1;
			if(hasArrived[x] == true){//If boolean array hasArrived is true, then SmartPants will talk to contestant
				talkingTo(x);
			}//if
		}//while
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {}
		
		msg("has talked to everyone. The game is about to start.");
		everyOneReady();//release everyOneReady semaphore after SmartPants talk to every contestant
		
	    try {
			gameEnd.acquire();//waiting for contestant_done == 0
			msg("Congratulation, this show is over!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    
	    for(int i = 0; i < totalContestant; i++){
	    	Contestant.showEnd.release();//Show ends, then contestant can leave
	    }

	}
	
	public void talkingTo(int x){
		talkTo++;
		msg("is talking to Contestant-" + x);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		hasArrived[x] = false;//Set boolean array hasArrived to false after SmartPants talked to a contestant (prevent infinite loop)
		msg("Contestant-" + x + " is ready for the show.");
	}

	public void everyOneReady(){
		for (int i = 0; i < totalContestant; i++){
			Contestant.everyOneReady.release();
		}
	}
	
	public static boolean end(){
		if(contestant_done == 0)
		   show_end = true;
		return show_end;
	}
	
	public static boolean isApproached(){
		for(int i = 0; i < approach.length; i++){
			if(approach[i])
				return true;
		}
		return false;
	}
	
	
	public void msg(String m) {
	    System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
	public static void decrement(){
		contestant_done--;
	}

}
