package Electronics;

import java.util.ArrayList;

public class Conveyor 
{
	//public static Manufacturing model;
	// Implement the queue using an ArrayList object
	protected ArrayList<Parts> converyor = new ArrayList<Parts>();  // Size is initialised to 0
	 
	protected int length = -1;  // Gives the maximum length of the conveyor. -1 indicates infinite size
	
	// getters/setters and standard procedures
	protected int getN() { return(converyor.size()); }
	protected void spInsertQue(Parts part) { converyor.add(part); 
	
	}
	
	protected Parts spRemoveQue() 
	{ 
		Parts part = null;
		if(converyor.size() != 0) part = converyor.remove(0);
		return(part);
		
	}
	
}