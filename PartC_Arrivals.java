package Electronics;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class PartC_Arrivals extends ScheduledAction
{
	static Manufacturing model;
	

	@Override
	protected double timeSequence()
	{
		return DuCPArr();
		
	}

	@Override
	protected void actionEvent()
	{
		Parts iCParts = new Parts();
		iCParts.type = Parts.uType.C;
		
		if(model.addBuffer == false && model.qConveyor[Constants.CONINPUT].getN()< model.qConveyor[Constants.CONINPUT].length){
			model.qConveyor[Constants.CONINPUT].spInsertQue(iCParts);	
		}else if (model.addBuffer == true && model.qConveyor[Constants.CONC].getN()< model.qConveyor[Constants.CONC].length){
			model.qConveyor[Constants.CONC].spInsertQue(iCParts);
		}else {
			model.output.lostCost += Constants.COST_PARTC;
		}
	}
	// Initialise the RVP
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
		arrCDelayTime = new TriangularVariate(5, 15, 65, new MersenneTwister(sd.arrC));
		typeRandGen = new MersenneTwister(sd.PartType);
		jamRanGen_C = new MersenneTwister(sd.jamP_C);
	}
	
	static public MersenneTwister jamRanGen_C; // used to calculate the Probability
	static public TriangularVariate arrCDelayTime;
	static public MersenneTwister typeRandGen;
	
	static public double DuCPArr() {
		double arrTimArr = 0.0;
		arrTimArr = model.getClock() +2*60+ delayedTimeC();
		return arrTimArr;
	}
	
	
	static public double delayedTimeC() {
		double delayedTime = 0.0;

		// if part C
		
		if (jamRanGen_C.nextDouble() < 0.005)
			delayedTime = arrCDelayTime.next();
		else
			delayedTime = 0;
		 
		return delayedTime;

	}
	
}
