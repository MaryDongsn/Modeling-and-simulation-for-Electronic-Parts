package Electronics;
import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class PartB_Arrivals extends ScheduledAction
{
	static Manufacturing model;
	

	@Override
	protected double timeSequence()
	{
		return DuBPArr();
		
	}

	@Override
	protected void actionEvent()
	{
		Parts iCParts = new Parts();
		iCParts.type = Parts.uType.B;
		
		if(model.addBuffer == false && model.qConveyor[Constants.CONINPUT].getN()< model.qConveyor[Constants.CONINPUT].length){
			model.qConveyor[Constants.CONINPUT].spInsertQue(iCParts);	
		}else if (model.addBuffer == true && model.qConveyor[Constants.CONB].getN()< model.qConveyor[Constants.CONB].length){
			model.qConveyor[Constants.CONB].spInsertQue(iCParts);
		}else {
			model.output.lostCost += Constants.COST_PARTB;
		}
	}
	// Initialize the RVP
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
		arrBDelayTime = new TriangularVariate(5, 20, 65, new MersenneTwister(sd.arrB));
		typeRandGen = new MersenneTwister(sd.PartType);
		jamRanGen_B = new MersenneTwister(sd.jamP_B);
	}
	
	static public MersenneTwister jamRanGen_B; // used to calculate the Probability
	static public TriangularVariate arrBDelayTime;
	static public MersenneTwister typeRandGen;
	
	static public double DuBPArr() {
		double arrTimArr = 0.0;
		arrTimArr = model.getClock() +1.4*60+ delayedTimeB();
		return arrTimArr;
	}
	
	
	static public double delayedTimeB() {
		double delayedTime = 0.0;

		// if part B
		
		if (jamRanGen_B.nextDouble() < 0.0175)
			delayedTime = arrBDelayTime.next();
		else
			delayedTime = 0;
		 
		return delayedTime;

	}
	
}

