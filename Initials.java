//printing MH manually seemed like the same thing as Hello IACT, so I thought I should try something different to fully get the hang of eclipse.
public class Initials { 																//class declaration
	public static void pa() { 															//easily print an asterisk
		System.out.print("*");	
	}
	public static void ps() { 															//Easily print a space
		System.out.print(" ");
	}

	static int x = 1,  y = 1, size;														//Size and 2 counting variables
	public static void main(String args[]){												//Main method
		System.out.println();
		try{
			size = Integer.parseInt(args[0]);											//Check for command line argument for size
		}catch (Exception e){
			size = 9;																	//Default size
		}
		int hs = (int)Math.ceil((double)(size)/2);
		for (x = 1; x <= hs; x++ ){														//Print the top part of the M
			pa();																		//Vertical line on the left of the M
			for(y = 1; y < x; y++) {													//First set of spaces (left of the M)
				ps();
			}
			pa();																		//Add an asterisk to the middle part of the M
			for(y = 1; y < ((size+1) - (x*2)); y++) {									//Second set of spaces (center of the M)
				ps();
			}
			if (x != (double)(size)/2 + 0.5) {											//Add another asterisk to the middle unless only 1 is needed
				pa();
			}
			for(y = 1; y < x; y++) {													//Third set of spaces (right of the M)
				ps();
			}
			pa();																		//Vertical line on the left of the M
			ps();
			pa();																		//Print the top part of the H
			for(y = 1; y <= size; y++){													//Make H the same width as M
				if (x == hs){															//Asterisks for the middle line...
					pa();
				}else{
					ps();																//...and spaces for all other lines
				}
			}
			pa();																		//One last asterisk
			System.out.println();														//New line
		}
		for(x = 1; x <= (size - hs); x++){												//Now for the bottom half.
			pa();
			for (y = 1; y <= size; y++){												//Keeping the width consistent with the top half
				ps();
			}
			pa();
			ps();
			pa();
			for (y = 1; y <= size; y++){												//...And again for the H
				ps();
			}
			pa();
			System.out.println();
		}
	}
}
