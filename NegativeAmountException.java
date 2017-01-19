package bank4;
public class NegativeAmountException extends Exception{
	private long Amount;
	NegativeAmountException(String msg,long amt){
		super(msg);
		this.Amount=amt;
	}
	public long getAmount(){
		return this.Amount;
	}
	public String toString(){
		return super.toString()+":"+this.Amount;
	}
}