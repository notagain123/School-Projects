import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		if(args.length < 5){
			System.out.println("Wrong command argument.");
			System.exit(-1);
		}

		Scanner sc = new Scanner(new File("Kcurvature_Data2.txt"));
		int row, col, min, max, label, points;
		row = sc.nextInt();
		col= sc.nextInt();
		min = sc.nextInt();
		max = sc.nextInt();
		label = sc.nextInt();
		points = sc.nextInt();
		
		arcChordDistance acd = new arcChordDistance(row, col, min, max, args, points);
		try {
			acd.loadData(sc, args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
