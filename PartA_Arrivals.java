package Electronics;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class PartA_Arrivals extends ScheduledAction
{
	static Manufacturing model;
	

	@Override
	protected double timeSequence()
	{
		return DuAPArr();
		
	}

	@Override
	protected void actionEvent(){
		Parts iCParts = new Parts();
		iCParts.type = Parts.uType.A;
		
		if(model.addBuffer == false && model.qConveyor[Constants.CONINPUT].getN()< model.qConveyor[Constants.CONINPUT].length){
			model.qConveyor[Constants.CONINPUT].spInsertQue(iCParts);	
		}else if (model.addBuffer == true && model.qConveyor[Constants.CONA].getN()< model.qConveyor[Constants.CONA].length){
			model.qConveyor[Constants.CONA].spInsertQue(iCParts);
		}else {
			model.output.lostCost += Constants.COST_PARTA;
		}
	}
	// Initialise the RVP
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
		arrADelayTime = new TriangularVariate(5, 15, 60, new MersenneTwister(sd.arrA));
		typeRandGen = new MersenneTwister(sd.PartType);
		
		
		
		
		//GAComment:  Why use a Uniform variate.  Can do this with the MerseenneTwister object that returns random numbers between 0 and 1.
		jamRanGen_A = new MersenneTwister(sd.jamP_A);
	}
	
	static public MersenneTwister jamRanGen_A; // used to calculate the Probability
	static public TriangularVariate arrADelayTime;
	static public MersenneTwister typeRandGen;
	
	static public double DuAPArr() {
		double arrTimArr = 0.0;
		arrTimArr = model.getClock() +2.8*60+ delayedTimeA();
		return arrTimArr;
	}
	
	static public double delayedTimeA() {
		double delayedTime = 0.0;

		// if part A
		
			if (jamRanGen_A.nextDouble() < 0.02) 
				delayedTime = arrADelayTime.next();
				//System.out.println("A Delayed time:" + delayedTime);
			
			else
				delayedTime = 0;
		 
		return delayedTime;

	}
}
