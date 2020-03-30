package Electronics;

public class Parts {
	  protected enum uType {A, B, C, NO_PART};
	  protected uType type;
	  protected static final uType No_PART = null;  //GAComment - Confusing - NO_PART is defined twice why? NO_PART does not have to equal the NULL pointer.  In fact it can be any value, so why redefine it? 
}
