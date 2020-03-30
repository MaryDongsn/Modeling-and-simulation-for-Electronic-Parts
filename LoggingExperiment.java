package Experiment;

import Electronics.Manufacturing;
import Electronics.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;

public class LoggingExperiment {
	public static void main(String[] args)
	   {
	       int i, NUMRUNS = 1; 
	       double startTime=0.0, endTime=120000.0;
	       Seeds[] sds = new Seeds[NUMRUNS];
	       Manufacturing manu;  // Simulation object

	       // Lets get a set of uncorrelated seeds
	       RandomSeedGenerator rsg = new RandomSeedGenerator();
	       for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
	       
	       
	       // Loop for NUMRUN simulation runs for each case
	       // Case 1
	       System.out.println(" Base Case - no additional pallets; no batch conveyor");
	       //System.out.println(" Case 2 - with additional pallets; no batch conveyor");
	       //System.out.println(" Case 3 - no additional pallets; with batch conveyor release 2 parts at one time");
	       //System.out.println(" Case 4 - 4 additional pallets; with batch conveyor release 4 parts at one time");
	       for(i=0 ; i < NUMRUNS ; i++)
	       {
	    	   manu = new Manufacturing(startTime,endTime,0,false,0,sds[i],true);
	    	   manu.runSimulation();
	    	   
	       }
	       
	   }
	

}
