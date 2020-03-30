package Electronics;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;

public class Manufacturing extends AOSimulationModel{
	protected int addNumPallets;
	protected boolean addBuffer;
	protected int numInputRelease;
	
	// Entities
	protected Conveyor [] qConveyor = new Conveyor[4];
	protected WorkCell [] rWorkCell = new WorkCell[8];
	protected Pallets [] rcPallets;
	protected PowerAndFreeConveyor rqPowerAndFreeConveyor = new PowerAndFreeConveyor();
	
	
	// References to RVP and DVP objects
	
	//protected RVPs rvp;  // Reference to rvp object - object created in constructor
   // Reference to dvp object
	//protected UDPs udp = new UDPs(this);

	// Output object
	protected Output output = new Output();
	
	public double getLostCost() { return output.lostCost; }
	public void clearLostCost() { output.clearLostCost();}
	
	// Output values - define the public methods that return values
	// required for experimentation.
	//Constructor
	public Manufacturing(double t0time, double tftime, int addNumPallets, boolean addBuffer, int numInputRelease, Seeds sd,boolean log)
	{
		// Initialise parameters here
		this.addNumPallets = addNumPallets;
		this.addBuffer = addBuffer;
		this.numInputRelease = numInputRelease;
		this.logFlag = log;
		rcPallets  = new Pallets [addNumPallets+40];
		initializeClasses(sd);
		// Create RVP object with given seed
		//rvp = new RVPs(sd);
		// Initialise the simulation model
		this.initAOSimulModel(t0time,tftime);   

		// Schedule the first arrivals and employee scheduling
		Initialise init = new Initialise(this);
		scheduleAction(init);  // Should always be first one scheduled.
		PartA_Arrivals aArr = new PartA_Arrivals();
		scheduleAction(aArr);
		PartB_Arrivals bArr = new PartB_Arrivals();
		scheduleAction(bArr);
		PartC_Arrivals cArr = new PartC_Arrivals();
		scheduleAction(cArr);
		// Schedule other scheduled actions and acitvities here
	}
	
	
	void initializeClasses(Seeds sd) {
		// Add reference to Standard Classes
		Initialise.model = this;
		Output.model = this;
		//RVPs.model = this;
		//UDPs.model = this;
		// Add reference to activity/action Classes
		Batch_Input.model = this;
		Load_Unload.model = this;
		Move_Pallet.model = this;
		PartA_Arrivals.model = this;
		PartB_Arrivals.model = this;
		PartC_Arrivals.model = this;
		Processing.model = this;
		//  Initialize RVPs in the classes
		PartA_Arrivals.initRvps(sd);
		PartB_Arrivals.initRvps(sd);
		PartC_Arrivals.initRvps(sd);
		Load_Unload.initRvps(sd);
		Processing.initRvps(sd);	
	}
	

	@Override
	protected void testPreconditions(Behaviour behObj) {
		// TODO Auto-generated method stub
		reschedule(behObj);
		while (scanPreconditions() == true) /* repeat */;
	}
	
	protected boolean scanPreconditions() {
		boolean statusChanged = false;
		if (Batch_Input.precondition() == true)
		{
			Batch_Input act = new Batch_Input(); // Generate instance																// instance
			act.actionEvent();
			statusChanged = true;
		}
		
		if (Load_Unload.precondition() == true) {
			Load_Unload act = new Load_Unload(); // Generate instance																// instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
		
		if (Move_Pallet.precondition() == true) {
			Move_Pallet act = new Move_Pallet(); // Generate instance																// instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
		
		if (Processing.precondition() == true) {
			Processing act = new Processing(); // Generate instance																// instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
		return statusChanged;
	}
	
	public void eventOccured()
	{		
		//output.updateSequence(); // for updating trajectory sets	
		if(logFlag) printDebug();
	}
	
	boolean logFlag = false;
	protected void printDebug()
	{
		System.out.printf("Clock = %10.4f\n", getClock());
		
		
		System.out.println("   Q.Conveyors[CONA].n:          "+qConveyor[Constants.CONA].getN());
		System.out.println("   Q.Conveyors[CONB].n:          "+qConveyor[Constants.CONB].getN());
		System.out.println("   Q.Conveyors[CONC].n:          "+qConveyor[Constants.CONC].getN());
		System.out.println("   Q.Conveyors[CONINPUT].n:      "+qConveyor[Constants.CONINPUT].getN());
		
		System.out.println();
		
		System.out.println("   Q.Conveyors[CONTINPUT].list: ");
		if(qConveyor[Constants.CONINPUT].getN()==0) {
			System.out.println("     Input Conveyor Is Empty");
		}
		for (int i=0; i< qConveyor[Constants.CONINPUT].getN()&& i<20; i++) {
			System.out.print(qConveyor[Constants.CONINPUT].converyor.get(i).type + " ");
		}
		System.out.println();
		if(qConveyor[Constants.CONINPUT].getN() >= 20) {
			for (int i=20; i< qConveyor[Constants.CONINPUT].getN()&& i>=20; i++) {
				System.out.print(qConveyor[Constants.CONINPUT].converyor.get(i).type + " ");
			}
			
		}

		System.out.println();

		
		/*
		for (int pos=0; pos<72; pos++) {
			if (pos<10) {
				System.out.print("0"+pos + "    " );
				if (pos%18 == 0) {
					System.out.println("\n");
				}
			}
			else System.out.print(pos + "    " );
		}
		    
		System.out.println();
		*/
		
		System.out.println("   RQ.PFConveyor.list:     ");
		System.out.println("-----------------------------------------------------------------------");
		for (int pos=0; pos<18; pos++) {
			if (pos == 0) {
				System.out.print("C8"+"  ");
			}else if(pos == 9) {
				System.out.print("C1"+"  ");
			}else {
				if (pos<10) {
					System.out.print("0"+pos+"  ");
				}else {
					System.out.print(+pos+"  ");
				}
			}
		}
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");;
		for (int pos=0; pos<18; pos++) {
			if (rqPowerAndFreeConveyor.list[pos] == -1) 
			{
			System.out.print("NP  ");
			}else if (rcPallets[rqPowerAndFreeConveyor.list[pos]].part == null ){
				System.out.print("EP  ");
			}else{
				System.out.print(" "+ rcPallets[rqPowerAndFreeConveyor.list[pos]].part.type + "  " );
			}
		}
		
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");
		for (int pos=18; pos<36; pos++) {
			if (pos == 18) {
				System.out.print("C2"+"  ");
			}else if(pos == 27) {
				System.out.print("C3"+"  ");
			}else {
				System.out.print(+pos+"  ");
			}
		}
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");
		for (int pos=18; pos<36; pos++) {
			if (rqPowerAndFreeConveyor.list[pos] == -1) 
			{
				System.out.print("NP  ");
			}else if (rcPallets[rqPowerAndFreeConveyor.list[pos]].part == null ){
				System.out.print("EP  ");
			}else{
				System.out.print(" "+ rcPallets[rqPowerAndFreeConveyor.list[pos]].part.type + "  " );
			}
		}
		
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");
		for (int pos=36; pos<54; pos++) {
			if (pos == 36) {
				System.out.print("C4"+"  ");
			}else if(pos == 45) {
				System.out.print("C5"+"  ");
			}else {
				System.out.print(+pos+"  ");
			}
		}
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");
		for (int pos=36; pos<54; pos++) {
			if (rqPowerAndFreeConveyor.list[pos] == -1) 
			{
				System.out.print("NP  ");
			}else if (rcPallets[rqPowerAndFreeConveyor.list[pos]].part == null ){
				System.out.print("EP  ");
			}else{
				System.out.print(" " + rcPallets[rqPowerAndFreeConveyor.list[pos]].part.type + "  " );
			}
		}
		
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");
		for (int pos=54; pos<72; pos++) {
			if (pos == 54) {
				System.out.print("C6"+"  ");
			}else if(pos == 63) {
				System.out.print("C7"+"  ");
			}else {
				System.out.print(+pos+"  ");
			}
		}
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");
		for (int pos=54; pos<72; pos++) {
			if (rqPowerAndFreeConveyor.list[pos] == -1) 
			{
				System.out.print("NP  ");
			}else if (rcPallets[rqPowerAndFreeConveyor.list[pos]].part == null ){
				System.out.print("EP  ");
			}else{
				System.out.print(" "+rcPallets[rqPowerAndFreeConveyor.list[pos]].part.type + "  " );
			}
		}
		System.out.print("\n");
		System.out.println("-----------------------------------------------------------------------");
		
		/*
		for (int pos=0; pos<72; pos++) {
			
			if (rPowerAndFreeConveyor.list[pos] == -1) 
				{
				System.out.print("NP    ");
				}
			else if (rcPallets[rPowerAndFreeConveyor.list[pos]].part == null )
				{
				System.out.print("EP    ");
				}
			else
				{
				System.out.print(rcPallets[rPowerAndFreeConveyor.list[pos]].part.type + "    " );
				}
			
		}*/
		System.out.print("\n");
		System.out.println();
		//System.out.println("     WorkCell1:   "+ rWorkCell[0].busy + "     WorkCell2:   "+ rWorkCell[1].busy + "     WorkCell3:   "+ rWorkCell[2].busy + "     WorkCell4:   "+ rWorkCell[3].busy + "     WorkCell5:   "+ rWorkCell[4].busy + "     WorkCell6:   "+ rWorkCell[5].busy +  "     WorkCell7:   "+ rWorkCell[6].busy + "     WorkCell8:   "+ rWorkCell[7].busy );
		System.out.println("---------------------------------------");
		System.out.println("CID      auto      busy      prtConfig");
		System.out.println("---------------------------------------");
		for (int i=0; i<rWorkCell.length; i++) {
			if (i == 1 || i==6) {
				System.out.println("C" + (i+1) + "      " + rWorkCell[i].auto + "     " + rWorkCell[i].busy + "        " + rWorkCell[i].prtConfig);
			}else {
				System.out.println("C" + (i+1) + "      " + rWorkCell[i].auto + "      " + rWorkCell[i].busy + "        " + rWorkCell[i].prtConfig);
			}
			
		}
		System.out.println("---------------------------------------");
		System.out.println();
		
		showSBL();
		System.out.println("\n" + "Total Lost Cost:     " + output.lostCost);
		System.out.println("\n" + ">----------------------------------------------------------------------<");
		
	}
	
	
}
