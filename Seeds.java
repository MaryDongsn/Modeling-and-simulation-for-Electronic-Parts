package Electronics;


import cern.jet.random.engine.RandomSeedGenerator;
public class Seeds {
	int arrA;
	int arrB;
	int arrC;
	int jamT;
	
	int jamP_Cell8;
	int jamP_A;
	int jamP_B;
	int jamP_C;
	
	int PartType;
	
	int processA;
	int processB;
	int processC;


    public Seeds(RandomSeedGenerator rsg) {
    	arrA = rsg.nextSeed();
    	arrB = rsg.nextSeed();
    	arrC = rsg.nextSeed();
    	jamT = rsg.nextSeed();
    	
    	jamP_Cell8 = rsg.nextSeed();
    	jamP_A = rsg.nextSeed();
    	jamP_B = rsg.nextSeed();
    	jamP_C = rsg.nextSeed();
    	PartType = rsg.nextSeed();
    	
    	processA= rsg.nextSeed();
    	processB= rsg.nextSeed();
    	processC= rsg.nextSeed();

    }
}
