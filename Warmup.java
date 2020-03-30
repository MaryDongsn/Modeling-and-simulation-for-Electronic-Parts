package Experiment;

import Electronics.Manufacturing;
import Electronics.Seeds;
import cern.jet.random.engine.*;
import outputAnalysis.WelchAverage;

class Warmup
{
   private static Manufacturing manufact;
   private static double[][] totalLostCostOutPut;

  /** main method **/
   public static void main(String[] args)
   {
       int NUMRUNS = 10;
       Seeds [] sds = new Seeds[NUMRUNS];
       double intervalStart, intervalEnd;  // start and end times of current interval
       double intervalLength=60*60;   // 1 hour intervals
       int numIntervals=60;            // Total 60 hours observation interval
       totalLostCostOutPut = new double[NUMRUNS][numIntervals];

       // Lets get a set of uncorrelated seeds
       RandomSeedGenerator rsg = new RandomSeedGenerator();
       for(int i=0 ; i<NUMRUNS ; i++)
           sds[i] = new Seeds(rsg);

       // Run for NUMRUNS simlation runs
       System.out.println("Base Case addBuffer = false; addNumPallets = 0; numInputRelease = 0");
    	   for (int i=0; i < NUMRUNS; i++) {
    		   manufact = new Manufacturing(0.0, intervalLength*numIntervals, 0, false, 0, sds[i], false);
    		   
    		   for (int interval=0; interval<numIntervals; interval++) {
    			   intervalStart = interval*intervalLength;
    			   intervalEnd = intervalStart+intervalLength;
    			   manufact.setTimef(intervalEnd);
    			   manufact.runSimulation();
    			   totalLostCostOutPut[i][interval]= manufact.getLostCost();
    			   manufact.clearLostCost();
    		   }
    	   }
    	   int [] wSizeTotalLostCost = {0,1,3,5};
    	   WelchAverage welchTotalLostCost  = new WelchAverage(totalLostCostOutPut, wSizeTotalLostCost);
    	   System.out.println("Total Number of Lost Cost");
    	   printWelchOutputMatrix(welchTotalLostCost.getWelchAvgOutput(), wSizeTotalLostCost, 1);  
    	}
       
    
   private static void  printWelchOutputMatrix(double[][] m, int [] w, double intervalLength)
   {
	  int ix, jx;
	  // Print out the window Sizes
	  System.out.print("t,");
	  for(ix=0; ix < w.length-1; ix++) System.out.print("w = "+w[ix]+",");
	  System.out.println("w = "+w[ix]); // Last one
	  // Let's output the data
	  for(jx = 0 ; jx < m[0].length ; jx++)  // print rows as columns
	  {
		  System.out.print( ((jx+1)*intervalLength)+", ");
	      for(ix = 0 ; ix < m.length ; ix++) // columns becomes rows
	      {
	    	  if(jx < m[ix].length)  System.out.print(m[ix][jx]); // rows have different lengths, assumes row 0 is longest		         
	    	  if(ix != m.length-1 && jx < m[ix+1].length) System.out.print(", "); // more to come
	    	  else if(jx<m[ix].length) System.out.println();   // Assumes that all rows decrease in length
	      }
	  }
   }
}
