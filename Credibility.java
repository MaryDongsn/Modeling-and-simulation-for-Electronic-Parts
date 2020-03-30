package Experiment;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import Electronics.Manufacturing;
import Electronics.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;
import outputAnalysis.ConfidenceInterval;


public class Credibility {
		 final static int NUMRUNS = 100;   // for exploring number of runs
		 public static final int [] NUM_WEEKS_OPERATION = { 2, 4, 8 };
	     final static double HOUR = 60.0*60.0; //60 seconds/minutes, 60 minutes/day
	     final static double WEEK = 5*16*HOUR;// 5 day/week, 16 hours/day
	     final static double WARM_UP_PERIOD = 1*WEEK;  // 8 hours warm up period
	     
	     public static double [][] totalLostCost = new double[NUM_WEEKS_OPERATION.length][NUMRUNS];
		 final static double CONF_LEVEL = 0.95;
	     final static int [] NUM_RUNS_ARRAY = {10, 20, 30, 40, 60, 80, 100};
	     
	     
	public static void main(String[] args) {
		int i, ixNWeeks;
		double startTime=0.0;        // Observation interval starts at t = 0
	    double endTime,ObservationTimeInWeek;              // End time, rhs of obserservation interval - to be determined experimentally
	    Seeds [] sds = new Seeds[NUMRUNS];
	    //double[]lostCost = new double [NUMRUNS];
	    RandomSeedGenerator rsg = new RandomSeedGenerator();
	    for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
	    System.out.println("Base Case");
	    for(ixNWeeks = 0; ixNWeeks < NUM_WEEKS_OPERATION.length; ixNWeeks++)
 	    {
 	       endTime=NUM_WEEKS_OPERATION[ixNWeeks] * WEEK;
 	       ObservationTimeInWeek = NUM_WEEKS_OPERATION[ixNWeeks]- WARM_UP_PERIOD/WEEK;
 	       
 	       
 	       System.out.println("End Time = "+NUM_WEEKS_OPERATION[ixNWeeks]+" weeks ("+endTime+" seconds), TimeStamp: "+
 	                          new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
	    
	    //System.out.println("Base Case");
	       for(i=0 ; i < NUMRUNS ; i++)
	       {
	    	   Manufacturing ManSys = new Manufacturing(startTime,endTime,0,false,0,sds[i], false); 
	              ManSys.setTimef(WARM_UP_PERIOD);
	              ManSys.runSimulation();
	              ManSys.clearLostCost();
	              
	              ManSys.setTimef(endTime);
	              ManSys.runSimulation();
	              totalLostCost [ixNWeeks][i] = ManSys.getLostCost()/ObservationTimeInWeek;
	              //lostCost[i] = ManSys.getLostCost();
	       }
	       
 	   }
	   displayTable (totalLostCost);
	}
	private static void displayTable(double [][] totalLostCost)
	{
		
		   printALine(1); //-----------------------------------------------------------------------------------------
	       System.out.print("|                                                    Base Case                                                 \n");
		   printALine(1); //-----------------------------------------------------------------------------------------
	       System.out.printf("    tf:  |");
	       for(int ix2 = 0; ix2 < NUM_WEEKS_OPERATION.length; ix2++) System.out.printf("               %d weeks              |", NUM_WEEKS_OPERATION[ix2]);
	       System.out.printf("\n");
		   printALine(1); //-----------------------------------------------------------------------------------------
	       System.out.printf("    n    |");
	       for(int ix2 = 0; ix2 < NUM_WEEKS_OPERATION.length; ix2++) System.out.printf(" yb(n)      s(n)    z(n)  z(n)/yb(n)|");
	       System.out.printf("\n");
		   printALine(1); //-----------------------------------------------------------------------------------------
		   printCFIntervals(totalLostCost);
	
	}
	
	private static void printALine(int numLines) 
	{ 
		for(int i=0; i<numLines; i++) 
			System.out.printf("+--------+------------------------------------+------------------------------------+------------------------------------+\n"); 
	}
	
	private static void printCFIntervals(double [][] output)
	{
		int numRuns;
		
		for(int ix1 = 0; ix1 <  NUM_RUNS_ARRAY.length; ix1++)
		{
	       numRuns = NUM_RUNS_ARRAY[ix1];
	       System.out.printf("%8d |", numRuns);
	       for(int ix2 = 0; ix2 < NUM_WEEKS_OPERATION.length; ix2++)
	       {         			
	    	   double [] values = new double[numRuns];
	    	   for(int ix3 = 0 ; ix3 < numRuns; ix3++) values[ix3] = output[ix2][ix3];
	    	   ConfidenceInterval ci = new ConfidenceInterval(values, CONF_LEVEL);
	    	   System.out.printf("%8.3f %8.3f %8.3f %8.4f |",
	    			               ci.getPointEstimate(), ci.getStdDev(), ci.getZeta(),
	    			               ci.getZeta()/ci.getPointEstimate());
	       }
	       System.out.printf("\n");
		}
		printALine(1);
	}
}


	
	
	/*private static void displayTable(double [] allValues)
	{
	       System.out.printf("------------------------------------------------------------------\n");
	       System.out.printf("                             Base Case \n");
	       System.out.printf("------------------------------------------------------------------\n");
	       System.out.printf("  n     y(n)     s(n)   zeta(n)   CI Min    CI Max  zeta(n)/y(n)\n");
	       System.out.printf("------------------------------------------------------------------\n");
	       for(int ix1 = 0; ix1 < NUM_RUNS_ARRAY.length; ix1++)
	       {
	    	   int numruns = NUM_RUNS_ARRAY[ix1];
	    	   double [] values = new double[numruns];
	    	   for(int ix2 = 0 ; ix2 < numruns; ix2++) values[ix2] = allValues[ix2];
	    	   ConfidenceInterval ci = new ConfidenceInterval(values, CONF_LEVEL);
	    	   System.out.printf("%3d %10.3f %6.3f %8.3f %10.3f %10.3f %8.3f\n",
	    			               numruns, ci.getPointEstimate(), ci.getStdDev(), ci.getZeta(),
	    			               ci.getCfMin(), ci.getCfMax(), ci.getZeta()/ci.getPointEstimate());
	       }
	       System.out.printf("------------------------------------------------------------------\n");
	}

}*/

	    
	  
