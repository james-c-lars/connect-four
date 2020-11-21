import cf.DecisionTree;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ConnectFour
{
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		
		DecisionTree computer = new DecisionTree();

        game: while(true) {
			computer.print();

            // Getting valid input from player
            while(true) {
                try {
                    System.out.print("Enter a col: ");
                    
                     // Player's turn
			        if(computer.enterUserInput(in.nextInt())) {
                        computer.print();
                        System.out.println("You've beaten the odds and won!\nNice job!");
                        break game;
                    }

                    break;
                } catch(IllegalArgumentException e) {
                    System.out.println("Input the number of a valid column");
                } catch(InputMismatchException e) {
                    System.out.println("Input the number of a valid column");
                    in.nextLine();
                }
            }
			computer.print();
			System.out.println();
			
			// Computer's turn
			if(computer.computersTurn()) {
                computer.print();
                System.out.println("You've succumbed to the inevitable and lost.\nNo shame in losing to such a great program.");
                break game;
            }

            
            if(computer.checkTie()) {
                computer.print();
                System.out.println("You've managed to acheive a tie.\nThe computer refuses to submit, but so do you.");
            	break game;
            }
		}
	}
}
