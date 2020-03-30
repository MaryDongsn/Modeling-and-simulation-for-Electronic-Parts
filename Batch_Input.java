package Electronics;

import simulationModelling.ConditionalAction;

public class Batch_Input extends ConditionalAction {
	static Manufacturing model;
	
	//GAComment: Delete the commented out code please.
	//protected BATCH_INPUT(Manufacturing model) {
	//	this.model = model;
	//}
	
	protected static boolean precondition() {
		return CanMoveBatch() != Constants.NONE;
	}
	
	protected void actionEvent() {
		MoveBatch(CanMoveBatch());
	}
	
	static int startBid = -1;//last conveyer with release	
	protected static int CanMoveBatch() {
		//System.out.println(" *********MoveBatch being called ");
		int nBid = (startBid+1)%3;
		int retVal = -1;
		if (model.addBuffer == true && model.qConveyor[Constants.CONINPUT].getN() <= model.qConveyor[Constants.CONINPUT].length - model.numInputRelease) {
			for (int counter=0; counter<3 && retVal == -1; counter++) {
				//System.out.println ("nBid= " + nBid + "     n = " + model.rConveyor[nBid].getN());
				if (model.qConveyor[nBid].getN() >= model.numInputRelease ) {
					retVal = nBid;
				}
				else {
					nBid = (nBid+1)%3;
				}
			}	
		}
		//System.out.println ("retVal = " + retVal);
		return retVal;	
	}
	
	protected static void MoveBatch(int Bid) {
		//System.out.println("MoveBatch    " + Bid);
		if (Bid >= 0) {
			for (int i=0; i<model.numInputRelease;i++) {
				model.qConveyor[3].spInsertQue(model.qConveyor[Bid].spRemoveQue());
			}
			startBid = Bid;
		}
	}
}
