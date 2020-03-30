package Electronics;


import simulationModelling.ScheduledAction;

class Initialise extends ScheduledAction
{
	static Manufacturing model;
	
	// Constructor
	public Initialise(Manufacturing model) { Initialise.model = model; }

	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	protected double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	protected void actionEvent() 
	{
		// System Initialisation
                // Add initilisation instructions 
		
		int id; // Machine/Conveyor identifiers
		for(id = Constants.CONA ; id <= Constants.CONINPUT ; id++)
		{
			model.qConveyor[id] = new Conveyor();  // Creates the object/entity
		}
		
		for (int cid=Constants.C1; cid<=Constants.C8; cid++) { 
			model.rWorkCell[cid] = new WorkCell();
			model.rWorkCell[cid].auto = true;
			model.rWorkCell[cid].busy = false;
			if (cid==Constants.C2 || cid==Constants.C7) {  
				model.rWorkCell[cid].auto = false;
				
			}
		}
		
		//GAcomment:  The following is not presented in the CM.
		//             Instead of using -1, should be using NO_PALLET, as specified in the CM.
		for (int i=0; i< model.rqPowerAndFreeConveyor.list.length;i++) {
			model.rqPowerAndFreeConveyor.list[i] = model.rqPowerAndFreeConveyor.NO_PALLET;  
		}
	
		
		for (int pid=0; pid< model.rcPallets.length;pid++) {
			model.rcPallets[pid] = new Pallets ();
			model.rcPallets[pid].part = null;
			model.rcPallets[pid].processed= false;
			model.rcPallets[pid].moving= false;
		}
		
		//GAComment:         Reflect this logic in the CM - the two are not consistent.
		for (int pos=71-38-model.addNumPallets,pid = 1; pos<model.rqPowerAndFreeConveyor.list.length; pos++, pid++) {
			model.rqPowerAndFreeConveyor.list[pos] = pid;
		}
		model.rqPowerAndFreeConveyor.list[0] = 0;
		for (int i=Constants.CONA; i<=Constants.CONINPUT; i++) {
			if (i!=Constants.CONINPUT) {
				model.qConveyor[i].length=10;
			}else {
				model.qConveyor[i].length=40;
			}
		} 
		
	}
	

}
