import java.util.Random;
import java.util.concurrent.Semaphore;


public class Date extends Thread{

	public static boolean talkDone = false;
	public static long time = System.currentTimeMillis();
	public static Semaphore checkGameEnd = new Semaphore(1); 
	public static Semaphore checkApproached = new Semaphore(1); 
	public static Semaphore talking = new Semaphore(1); 
	public static Semaphore talk = new Semaphore(1); 
	public static Semaphore makeAvailable = new Semaphore(1); 
	private Random random = new Random();
	private int dateId;
	private int decision;
	public  boolean end = false;
	
	
	public Date(int id) {
		super("Date-" + id);
		this.dateId = id;
	}

	public void run() {
		try {
			while(!checkEnd()){	//looping until contestant_done == 0
				checkApproached.acquire();//Semaphore, check if a contestant is approaching
				if(!SmartPants.isApproached()){
					checkApproached.release();
					continue;
				}
				checkApproached.release();
				try {
					talking.acquire();
					talkNotdone();//Simulation, Date is talking to a contestant
					talking.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				this.setPriority(MAX_PRIORITY);
				try {
					Thread.sleep(random.nextInt(10000) + 10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					talk.acquire();
					makeDecision();//Decide to give contact information or not
					talkdone();//Simulation, Date finished the conversation with a contestant
					talk.release();
					Contestant.checkConversation.release();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
		        
				try {
					makeAvailable.acquire();
					setAvailable();//availableDate++ if a Date is available
					makeAvailable.release();
					Contestant.wait.release();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.setPriority(NORM_PRIORITY);
			}
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
	}
	
	public void talkdone(){
		talkDone = true;
	}
	
	public void talkNotdone(){
		talkDone = false;
	}
	
	public boolean checkEnd(){
		try {
			checkGameEnd.acquire();
			end = SmartPants.end();
			checkGameEnd.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return end;
	}
	
	public static boolean notAvailable(){
		if(SmartPants.availableDate == 0){
			return true;
		}else{
		   return false;
		}
	}
	
	public void setAvailable(){
		SmartPants.availableDate++;
	}
	
	public void makeDecision(){
		decision = random.nextInt(50);
		if(decision % 2 == 0){
			return;
		}
		else{
			Contestant.info = "Date-" + dateId;
		}
	}
	
	public void msg(String m) {
		System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }
}
