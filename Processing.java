package Electronics;
import Electronics.Parts.uType;
import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.Activity;
class Processing extends Activity{
 static Manufacturing model;
 int cid; 
 int pid;
 uType type;

 
 
 public static boolean precondition() {
  //System.out.print("preconditation: " + cellId >= 0);
  boolean retVal =false;
  if (CellReadyForProcessing() != Constants.NONE)
   retVal = true;
  return retVal;
 }
 
 public void startingEvent() {
  cid = CellReadyForProcessing();
  type = model.rcPallets[model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[cid]]].part.type;
  pid = model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[cid]];
  model.rWorkCell[cid].busy=true;
  //System.out.println("start processing "+ cellId);
 }
 
 public double duration() {
  //System.out.println("duration "+ cellId +"             " +uMProceTime(cellId,type));
  return uMProcessTime(cid,type);
  
 }
 
 public void terminatingEvent() {
  model.rWorkCell[cid].busy=false;
  model.rcPallets[pid].processed = true;  //GAComment: not consistent with CM.
  
  
  
  model.rWorkCell[cid].prtConfig = type;
  model.rWorkCell[Constants.C2].prtConfig = null; 
  model.rWorkCell[Constants.C7].prtConfig = null;
  //startCid = CellReadyForProcessing();
  //System.out.println("finish processing" + cellId);
 }

 
 static int CellReadyForProcessing() {
  int cid;
  int retVal = -1;
  for (cid=Constants.C1; cid<=Constants.C7 && retVal ==-1; cid++)
  {
   if (model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[cid]] != model.rqPowerAndFreeConveyor.NO_PALLET &&
    model.rWorkCell[cid].busy == false &&
    model.rcPallets[model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[cid]]].processed == false && 
    model.rcPallets[model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[cid]]].part!= Pallets.NO_PART
    ) 
   {
    retVal = cid;
   }
   else retVal = -1;
   
  }
  //System.out.println("can process "+ retVal);
  return retVal;
 }
 
 static void initRvps(Seeds sd)
 {
  // Initialise Internal modules, user modules and input variables
  procATime_C2 = new TriangularVariate(36, 45, 52, new MersenneTwister(sd.processA));
  procATime_C7 = new TriangularVariate(27, 35, 41, new MersenneTwister(sd.processA));

  procBTime_C2 = new TriangularVariate(21, 32, 39, new MersenneTwister(sd.processB));
  procBTime_C7 = new TriangularVariate(31, 39, 43, new MersenneTwister(sd.processB));

  procCTime_C2 = new TriangularVariate(32, 36, 42, new MersenneTwister(sd.processC));
  procCTime_C7 = new TriangularVariate(22, 27, 38, new MersenneTwister(sd.processC));
 }
 
 static TriangularVariate procATime_C2;
 static TriangularVariate procBTime_C2;
 static TriangularVariate procCTime_C2;

 static TriangularVariate procATime_C7;
 static TriangularVariate procBTime_C7;
 static TriangularVariate procCTime_C7;
 
 
 public double uMProcessTime(int cid, Parts.uType type) {
  double totalTime = 0.0;
  int TypeToArrLocation;
  if (type==Parts.uType.A) {
   TypeToArrLocation=0;
  }else if(type==Parts.uType.B) {
	  TypeToArrLocation=1;
  }else {
	  TypeToArrLocation=2;
  }

//GAcomment: the following could be implemented using a single instruction and a 2D array:   
/*   
double[][] resetTimes =
{//       C1  C2  C3  C4  C5  C6  C7,  C8 not added
  { 25, -1, 52, 35, 29, -1, 11 },
  { 20, -1, 21, 22, 14, -1, 19 },
  { 17, -1, 34, 24, 37, -1, 17 }
};
resetTime = resetTimes[type][cid]); // This means that PA, PB, PC must be declares as ints  set to 0, 1, 2
The same can be done for process times.
*/
  int [][]resetTimes = {{25, 0, 52, 35, 29, 11, 0},
		  {20, 0, 21, 22, 14, 19, 0},
		  {17, 0, 34, 24, 37, 17, 0}};
  double [][]processTime = {{37, procATime_C2.next(), 39, 41, 33, 31, procATime_C7.next()}, 
		  {46, procBTime_C2.next(), 27, 38, 41, 24, procBTime_C7.next()},
          {39, procCTime_C2.next(), 23, 47, 35, 51, procCTime_C7.next()}};
  totalTime = processTime[TypeToArrLocation][cid];
  if (type!= model.rWorkCell[cid].prtConfig)
  totalTime = resetTimes[TypeToArrLocation][cid] + processTime[TypeToArrLocation][cid];
  return totalTime;
 }

 
}