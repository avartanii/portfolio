import java.util.Arrays;

public class ChangeMaker {
	
	public static int getQuarters(int cents){
		return cents / 25;
	}

	public static int getDimes(int cents) {
		return cents / 10;
	}

	public static int getNickels(int cents) {
		return cents / 5;
	}

	public static int getPennies(int cents) {
		return cents / 1;
	}

	public static int[] getChange(int cents) {
		int[] coins = new int[4];
		int money = cents;
		coins[0] = getQuarters(money);
		money -= coins[0] * 25;
		coins[1] = getDimes(money);
		money -= coins[1] * 10;
		coins[2] = getNickels(money);
		money -= coins[2] * 5;
		coins[3] = getPennies(money);
		return coins;
	}

	public static void main(String[] args) {
		try{
			int amount = Integer.parseInt(args[0]);
			if (amount < 0) {
				System.out.println("Cannot make change for negative amount");
			} else {
				int[] answer = getChange(amount);
				String output = new String("Quarters: " + answer[0] + ", Dimes: " + answer[1] + ", Nickels: " + answer[2] + ", Pennies: " + answer[3]);
				System.out.println(output);
			}
		} catch (ArrayIndexOutOfBoundsException e)  {
			System.out.println("Usage: java ChangeMaker <amount in cents>");
		} catch (NumberFormatException e2) {
			System.out.println("Given value is not an integer");
		}
	}
}