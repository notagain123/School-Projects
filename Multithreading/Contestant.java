import java.util.*;
import java.util.concurrent.Semaphore;
import java.lang.*;

public class Contestant extends Thread{

	public static Queue<Integer> q = new LinkedList<Integer>();
	public static long time = System.currentTimeMillis();
	public static Semaphore checkDateAvailable = new Semaphore(1); 
	public static Semaphore wait = new Semaphore(0);
	public static Semaphore doneDecrement = new Semaphore(1); 
	public static Semaphore everyOneReady = new Semaphore(0); 
	public static Semaphore checkRound = new Semaphore(1); 
	public static Semaphore checkConversation = new Semaphore(0); 
	public static Semaphore checkApproaching = new Semaphore(1); 
	public static Semaphore finishApproaching = new Semaphore(1); 
	public static Semaphore showEnd = new Semaphore(0); 
	public static Semaphore groupUP = new Semaphore(0); 
	public static Semaphore done = new Semaphore(1); 
	public static Semaphore checkSize = new Semaphore(1); 
	public static String info;
	public static int groupNumber;
	private int num_rounds;
	private int contestantId; 
	private Random random = new Random();
	
	public Contestant(int id, int rounds){
		super("Contestant-" + id);
		num_rounds = rounds;
		this.contestantId = id;
	}
	
	public void run() {
		
		try {
			Thread.sleep(random.nextInt(30000) + 10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		msg("has arrived at the club.");
		arrived();//Set boolean array hasArrived to true, so SmartPants will know
		
		try {
			everyOneReady.acquire();//Wait until SmartPants talk to every contestant
			checkRound.acquire();//Semaphore before checking num_rounds
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		while(!checkRound()){//Check num_rounds
			try {
				checkRound.release();
				checkDateAvailable.acquire();//Semaphore before checking available Date
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(checkDateAvailable()){//Check for available Date
				msg("is waiting for an available Date.");
				checkDateAvailable.release();
				try {
					Thread.sleep(1000);
					wait.acquire();//Semaphore, waiting for an available Date
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else{
				checkDateAvailable.release();
				try {
					checkApproaching.acquire();
					msg("has chance: " + num_rounds);
					approaching();//Set boolean array approach to true, so Date will know contestant is approaching
					checkApproaching.release();
					
					checkConversation.acquire();//Semaphore, waiting conversation done(simulate Date is talking to a contestant)
					num_rounds--;
					getInformaion(info);//Get contact information from Date
					finishApproaching.acquire();
					finishApproach();//Set boolean array approach to false, so Date will know contestant finished approaching
					finishApproaching.release();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}	
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}
				
		}
		
		try {
			doneDecrement.acquire();
			SmartPants.decrement();//contestant_done-- when a contestant is done
			if (SmartPants.contestant_done == 0)
				SmartPants.gameEnd.release();//release gameEnd when contestant_done == 0
			doneDecrement.release();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		try {
			showEnd.acquire();//wait until contestant_done == 0
		} catch (Exception e) {}
		
		brag();
		
		try {
			checkSize.acquire();//Semaphore, before checking the group size
			SmartPants.numberOfContestant++;
			if((SmartPants.numberOfContestant % SmartPants.group_size == 0) || SmartPants.numberOfContestant == SmartPants.totalContestant){
				try {
					groupNumber++;
					checkSize.release();
					groupUP.release(SmartPants.group_size - 1);
					msg("is in group " + groupNumber);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				checkSize.release();
				groupUP.acquire();
				msg("is in group " + groupNumber);
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
			done.acquire();
			isDone();//Contestants leave, and print out the contact information
			done.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public  void arrived(){
		SmartPants.hasArrived[contestantId] = true;
	}
	
	public boolean checkRound(){
		return num_rounds == 0;
	}
	
	public boolean checkDateAvailable(){
		if(Date.notAvailable())
			return true;
		else{
			SmartPants.availableDate--;
			return false;
		}
	}
	
	public void approaching(){
		 SmartPants.approach[contestantId] = true;
	}
	
	public void finishApproach(){
		 SmartPants.approach[contestantId] = false;
	}
	
	public void isDone(){
		msg("has left.");
		if(SmartPants.information[contestantId] == null){
			msg("has no contact information.");
		}else{
			msg("has contact information: " + SmartPants.information[contestantId]);
		}
	}
	
	public void getInformaion(String s){
		if(s == null){
			msg("didn't get the contact infomation.");
			return;
		}
		
		msg(s + " gave her contact infomation.");
		if(SmartPants.information[contestantId] == null)
			SmartPants.information[contestantId] = s + " ";
		else 
			SmartPants.information[contestantId] += s + " ";	
	}
	
	public static boolean conversationCheck(){
		return Date.talkDone;
	}
	
	public void brag(){
		try {
			Thread.sleep(random.nextInt(5000) + 5000);
		} catch (InterruptedException e) {}
	}
	
	public void msg(String m) {
	    System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
	}
	
}
