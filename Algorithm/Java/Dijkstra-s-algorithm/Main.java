import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length < 4 || args.length > 4){
			System.out.println("Wrong command argument.");
			System.exit(0);
		}
		
        Scanner sc = new Scanner(new File(args[0]));
        Scanner sc1 = new Scanner(new File(args[1]));
        
        int n, source;
        
        n = sc.nextInt();
        
        source = sc1.nextInt();
        sc1.close();
        DijkstraSSS dij = new DijkstraSSS(n, source);
        try {
			dij.Dijkstras(sc, args);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
      
        sc.close();
        sc1.close();
	}

}
