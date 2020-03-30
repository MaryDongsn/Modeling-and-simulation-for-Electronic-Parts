package Electronics;
import java.util.Scanner;
public class function {

	public static void main(String[] args) {
		System.out.print("Enter the number: ");
		
		Scanner scanner=new Scanner(System.in);
		
		double num = scanner.nextDouble();
		double res = 0, tempone = 0, temptwo = 0;
		System.out.print("The answer is ");
		for (double i = 1; i <= num; i++) {
			if (i == 1) {res = 0;tempone = 0;temptwo = 0;
				 }
			if (i == 2) {res = 1;tempone = 0;temptwo = 1;
				 }
			else {res = tempone +temptwo; 
				 tempone = temptwo;
					temptwo = res;

			}
			
			
			}
		System.out.println(res);
		
	

	     
		
		
	}
	}	     
