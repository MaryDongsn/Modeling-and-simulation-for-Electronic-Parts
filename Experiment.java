package Experiment;



import Electronics.Manufacturing;
import Electronics.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;


public class Experiment {
      final static int [] NUM_BATCH_LEAST = { 2,3,4,5,6,7,8,9,10 };
      final static int MAX_ADD_PALLET_NUMBER = 32;
      final static double HOUR = 60*60.0;
      final static double WEEK = 5.0* 16.0 * HOUR;  // 5 day week, 16 hours/day
      final static double WARM_UP_PERIOD = 1*WEEK; 
      static final int NUMRUNS = 20;
      public static final int NUM_WEEKS = 4;
     
   static double [][] lostCost = new double[MAX_ADD_PALLET_NUMBER+1][NUM_BATCH_LEAST.length]; // one output data set per end time for each case
   static double[]lostCostWithoutBatch = new double[MAX_ADD_PALLET_NUMBER+1];
   

      
      
 public static void main(String[] args) {
	 double startTime = 0.0; // Observation interval starts at t = 0
	 double endTime = NUM_WEEKS * WEEK;
	 double ObservationTimeInWeek = NUM_WEEKS- WARM_UP_PERIOD/WEEK;
     Seeds [] sds = new Seeds[1];
     RandomSeedGenerator rsg = new RandomSeedGenerator();
     
     for(int i=0 ; i<1 ; i++) sds[i] = new Seeds(rsg);
     for(int add_pallet_number=0 ; add_pallet_number<=MAX_ADD_PALLET_NUMBER ; add_pallet_number++)
     {
            Manufacturing ManSysF = new Manufacturing(startTime,endTime,add_pallet_number,false,0,sds[0], false);
            ManSysF.setTimef(WARM_UP_PERIOD);
            ManSysF.runSimulation();
            ManSysF.clearLostCost(); 
            ManSysF.setTimef(endTime);
            ManSysF.runSimulation();             
            //System.out.println("ADD_PALLET_NUMBER: " + add_pallet_number);
            //System.out.println("NO_BATCHLEAST");
            lostCostWithoutBatch[add_pallet_number] = ManSysF.getLostCost()/ObservationTimeInWeek;
            
         for(int batchLeast = 0; batchLeast < NUM_BATCH_LEAST.length; batchLeast++)
         {
              Manufacturing ManSys = new Manufacturing(startTime,endTime,add_pallet_number,true,NUM_BATCH_LEAST[batchLeast],sds[0], false);
              ManSys.setTimef(WARM_UP_PERIOD);
              ManSys.runSimulation();
              ManSys.clearLostCost(); 
              ManSys.setTimef(endTime);
              ManSys.runSimulation();             
              //System.out.println("ADD_PALLET_NUMBER: " + add_pallet_number);
             // System.out.println("batchLeast: " + batchLeast);
              lostCost[add_pallet_number][batchLeast] = ManSys.getLostCost()/ObservationTimeInWeek;
         }
     }
     displayTable(lostCost);
 }
 
 private static void displayTable(double [][]lostCost)
 {
  
  System.out.print("NUM_BATCH_RELEASE     ");
  for (int i=0; i<NUM_BATCH_LEAST.length; i++) {
   System.out.print("|     "+NUM_BATCH_LEAST[i]+"    ");
  }
  System.out.println("|   Flase   |");
  for (int i=0; i<MAX_ADD_PALLET_NUMBER+1; i++) {
   if(i<10) {
    System.out.print("ADD_PALLET_NUMBER: "+i+"  |");
   }else {
    System.out.print("ADD_PALLET_NUMBER: "+i+" |");
   }
   for (int q=0; q<NUM_BATCH_LEAST.length;q++) {
    System.out.printf("%8.2f ", lostCost[i][q]);
    System.out.print(" |");
    
    
   }
   System.out.printf("%8.2f ",lostCostWithoutBatch[i]);
   System.out.println(" |");
  }
 }
}