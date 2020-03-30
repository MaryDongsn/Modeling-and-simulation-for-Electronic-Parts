package Electronics;


public class Output 
{
	static Manufacturing model;
	double lostCost;
	
	
	
    // SSOVs
	

	public Output () {
		clearLostCost ();
	}

	protected void clearLostCost() {
		this.lostCost = 0.0;
	
	}
}