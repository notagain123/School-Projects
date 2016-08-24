import java.io.PrintWriter;
import java.rmi.RemoteException;

public interface RMI__NotifyListeners_Interface extends java.rmi.Remote {

	void notifyNewMessage(long userid, String message) throws RemoteException;
	
	void waitForMessage(long userid, PrintWriter out)throws RemoteException;
	
	
}
