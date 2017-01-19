package bank4;

public class SavingsAccount extends Account {
private double interest;

		public SavingsAccount(long accno,String name,long balance) throws NegativeAmountException{
		super(accno,name,balance);
		this.interest=3.5;
		}
		
		public boolean withdraw(long amt) {
		
		if(amt<0) {
		System.out.println("\nInvalid Entry!");
		return false;
		}

		if(amt>super.getBalance()) {
		System.out.println("\nNot Enough Balance!");
		return false;
		}

		else if(amt==super.getBalance()) {
		System.out.println("\nBalance cannot become Zero!");
		return false;
		}

		else if(amt<super.getBalance()) {
		balance-=amt;
		System.out.println("\nNew Balance:"+balance);
		return true;
		}
		return false;
		}
}