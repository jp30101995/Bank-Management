package bank4;
public class CurrentAccount extends Account {
	public static final long minBalance=5000;
	
	public CurrentAccount(long accno,String name,long balance)throws NegativeAmountException,NoSuchAccountException {
	super(accno,name,balance);
	}
	
	private Penalty penalty=(balance,minBal)->balance>=minBal?0:100;
	public void setPenalty(Penalty p){
		this.penalty=p;
	}
	public boolean withdraw(long amt)throws NegativeAmountException{
		if(amt<0){
				throw new NegativeAmountException("\n plzz Enter positive wthdraw value : ",amt);
		}
		else if(!super.withdraw(amt)){
		/*	this.setPenalty((getBalance(),minBalance)->{
		System.out.println("\n Bal :"+balance+"  minbal : "+minBalance);
		long l;
		if(getBalance()<minBalance){
			return getBalance()/10;
		}
		else return 0;
	
	});*/

			return false;
		}
		
		else{
		long penaltyAmount = penalty.compute(this.balance,minBalance);
		if(penaltyAmount>0){
		new Transaction("Penalty",TransactionType.DEBIT,penaltyAmount);
		}
		return true;
		}
	}
/*	public boolean withdraw(long amt) {
		if(amt<0) {
		System.out.println("\nInvalid Entry!");
		return false;
		}
		
		if(amt>balance)
		{
		System.out.println("\nNot Enough Balance!\nPlease Try Again...");
		return false;
		}

		else if(amt==balance)
		{
		System.out.println("\nBalance cannot become Zero!\nPlease Try Again...");
		return false;
		}
		
		else if(amt<balance) {
		balance-=amt;
			if(balance<5000) {
			balance-=100;
			System.out.println("\nBalance below $5000.\n$100 Penalty Deducted");
			}
			System.out.println("\nNew Balance:"+balance);
		return true;
		}
	return false;
	}*/
}