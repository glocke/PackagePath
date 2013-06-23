package packagepath;

import java.util.Map;
import java.util.Set;

/**
 * An interface for defining contracts for email controllers
 * 
 * @author Brandon
 */
public interface EmailControllerInterface {

	/**
	 * The main method for returning a set of tracking numbers
	 * 
	 * @return Set of tracking numbers
	 */
	public Map<String, Set<String>> retrieveTrackingNumbers();
	
}
