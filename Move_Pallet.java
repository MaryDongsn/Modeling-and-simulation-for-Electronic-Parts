package Electronics;

import simulationModelling.Activity;
class Move_Pallet extends Activity {
	protected static Manufacturing model;
	int[]pos = new int[40+model.addNumPallets];
    

	public static boolean precondition() {
		//System.out.println("can move check  ");
		boolean retVal = false;
		for (int counter  = 0, pos = -1;counter< 72&& pos ==-1; counter++) {
			if (CanMovePallet(counter)>-1) {
			retVal = true;
			pos++;
			}
			//System.out.println("can move   "+ CanMovePallet(counter)+ pos);	
		}
		
		return retVal;	
	}

	public void startingEvent() {
		StartMovingPallet();
	}
		
	private void StartMovingPallet() {
		for (int i = 0;i<pos.length;i++){
			pos[i]=-1;
		 }
		int palPos =-1; 
		int i = -1;
		int pid;
		//System.out.println(" *********list being called ");
		int counter;
		for (counter=143; counter> -1;counter --){
			if (counter >= 72) {
				palPos = counter-72;
				//System.out.println("++check pos" +  palPos);
				if (CanMovePallet(palPos)!= PowerAndFreeConveyor.NO_PALLET) {
					//System.out.println("++will move" +  palPos);
					i++;
					pos[i] = palPos;
					pid = PalletIdAtPosition(palPos);
					model.rcPallets[pid].moving =true;
					}
				
				//System.out.println(" Counter1: " + counter + "palPos:    " +palPos);	
			}
			//System.out.println(" #########Counter1: " + i);	
			if (counter < 72) {
				palPos = counter;
				if (CanMovePallet(palPos)!= PowerAndFreeConveyor.NO_PALLET) {
					//System.out.println("++will move" +  palPos);
					i++;
					pos[i] = palPos;
					pid = PalletIdAtPosition(palPos);
					model.rcPallets[pid].moving =true;
					
				}
				//System.out.println(" Counter2: " + counter + "palPos:    " +palPos);
			}
			
			
			//System.out.println("list of pallet that can move    "+ pos[i]);
		}
		//System.out.println(" #########Counter2: " + i);	
	}

	public double duration() 
	{
		return Constants.MOVE_TIME;
	}
	
	@Override
	
	protected void terminatingEvent() {
		int temppid = 0;
		int pid = 0;
		for (int i = 0; i < pos.length; i++ ) {
			//System.out.println("-----pos    "+ pos[i]);
			if (pos[i] == 71) {
				temppid = PalletIdAtPosition(71);
				//model.rPowerAndFreeConveyor.list[71]= -1;
			}
				int palletPos = pos[i];
				if (palletPos != model.rqPowerAndFreeConveyor.NO_PALLET) {
				//System.out.println("length    "+ pos.length);
				
				pid = PalletIdAtPosition(palletPos);
				//System.out.println("finish moving    "+ palletPos+"        the pid is      "+ pid);
				
				model.rcPallets[pid].moving = false;
				model.rqPowerAndFreeConveyor.list[(palletPos+1)%72] = pid;
				model.rqPowerAndFreeConveyor.list[palletPos]= model.rqPowerAndFreeConveyor.NO_PALLET;
				model.rcPallets[pid].processed = false;
				}
				
				
			}
		
		for (int i = 0; i < pos.length; i++ ) {
			if (pos[i] == 71) {
				model.rqPowerAndFreeConveyor.list[0] = temppid;
				
			}
		}
	}
	
	private static int CanMovePallet(int palPos) {
		int retVal = -1;
		int nextPos = (palPos+1)%72;
			if (model.rqPowerAndFreeConveyor.list[palPos]!= -1){
				if ((IsWorkCell(palPos) == true && 
						(model.rcPallets[model.rqPowerAndFreeConveyor.list[palPos]].processed == true||
						(palPos != Constants.CELLPOS[Constants.C8] && model.rcPallets[model.rqPowerAndFreeConveyor.list[palPos]].part == null)))  ||
						(IsWorkCell(palPos) == false) ) 
				{
					if (model.rcPallets[model.rqPowerAndFreeConveyor.list[palPos]].moving == false &&
							((model.rqPowerAndFreeConveyor.list[nextPos] == -1) ||
									(model.rcPallets[model.rqPowerAndFreeConveyor.list[nextPos]].moving==true))) 
					{	
				 
					retVal = palPos;	
				 //System.out.println(" ------------Check conveyor posotion ");
				 //System.out.println(" Check conveyor posotion Can move pallet at POS: " + posRetVal);
					}					 
				}
			}
			
		


		return retVal;
	}	
			
	protected static int PalletIdAtPosition(int npos) {
		int retVal =  PowerAndFreeConveyor.NO_PALLET;
		
		if (npos != PowerAndFreeConveyor.NO_PALLET)
		retVal = model.rqPowerAndFreeConveyor.list[npos];
		//System.out.println("Move pallet ID: " + retVal);
		return retVal;  // GAComment:  Not sure why this method is required?
		//return the Pallet ID that of the Pallet that can move	
	}
	
	protected static boolean IsWorkCell(int palpos) {
		boolean flag = false;
		for (int p=0; p<Constants.CELLPOS.length && !flag; p++) {
			if (Constants.CELLPOS[p] == palpos) {
				flag = true;
			}
		}
		//System.out.println("is work cell: " + flag);
		return flag;
	}
}