package Electronics;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;

import simulationModelling.ConditionalActivity;
class Load_Unload extends ConditionalActivity {
	static Manufacturing model;
	Parts icPart;
	int pid;
//	protected Load_Unload(Manufacturing model) {
//		Load_Unload.model = model;
//	}
	
	protected static boolean precondition() {
		//System.out.println(" *********loadunload being called ");
		return model.qConveyor[Constants.CONINPUT].getN() > 0 && 
			   model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[Constants.C8]] != -1 && 
			   model.rWorkCell[Constants.C8].busy == false && 
			   model.rcPallets[model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[Constants.C8]]].processed == false&&
			   model.rcPallets[model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[Constants.C8]]].moving == false; // GAComment:  This is not in the CM.  Please keep CM and SM aligned
		
			   
	}
	
	public void startingEvent() {
		//System.out.println ("Load_Unload");
		pid = model.rqPowerAndFreeConveyor.list[Constants.CELLPOS[Constants.C8]];  //GAcComment: This is missing in the CM.
		model.rWorkCell[Constants.C8].busy = true;
		//model.qConveyor[Constants.CONINPUT].spRemoveQue();
		icPart = model.qConveyor[Constants.CONINPUT].spRemoveQue();  
	}
	
	protected double duration() {
		double r = uLProcTime();
		//System.out.println("##########loading time      " + r);
		return uLProcTime();
		
	}
	
	protected void terminatingEvent() {
		
		model.rcPallets[pid].part = icPart;
		model.rWorkCell[Constants.C8].busy=false;
		model.rcPallets[pid].processed = true;
	}

	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
		//GAComment: do not need Uniform variate - can be implemente with MerseenTwister only.
		jamRanGen_Cell8 = new MersenneTwister(sd.jamP_Cell8);
		clearJamTime = new TriangularVariate(5, 15, 75, new MersenneTwister(sd.jamT));
	}
	
	static MersenneTwister jamRanGen_Cell8; // used to calculate the Probability
	static TriangularVariate clearJamTime;
	
	
	// Processing time in WorkCell[C8].
	static public double uLProcTime() {  // GAComment: would be wise to prefix the method, rvp_uLProcTime() to better reflect the CM.
			double ProcTime = 0;
			ProcTime = 25 + clearJamTime(jamRanGen_Cell8);
			return ProcTime;

		}
	
	static public double clearJamTime(MersenneTwister jamRanGen) {
		double clearTime = 0.0;
		if (jamRanGen.nextInt() < 0.01)
			clearTime = clearJamTime.next();
		return clearTime;

	}
	
}

