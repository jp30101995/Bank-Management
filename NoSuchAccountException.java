package bank4;
public class NoSuchAccountException extends Exception{
	private long Acno;
	NoSuchAccountException(String msg,long amt){
		super(msg);
		this.Acno=amt;
	}
	public long getAcno(){
		return this.Acno;
	}
	public String toString(){
		return super.toString()+":"+this.Acno;
	}
}