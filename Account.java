package bank4;
import java.util.*;
import java.io.*;
public abstract class Account implements Serializable {
	private long AccountNumber;
	private String name;
	long balance;
	private long minBal;
	
	//private Penalty penalty=(balance,minBal)->balance>minBal?0:100;
	//private Transaction[] passbook=new Transaction[100];
	private int nextIndex;
	private List<Transaction> passbook=new ArrayList<>();
/*	public void setPenalty(Penalty p){
		this.penalty=p;
	}*/
	public class Transaction implements Serializable{
		private long dateTime=System.currentTimeMillis();
		private String naration;
		private TransactionType type;
		private long amount;
		Transaction(String n,TransactionType t,long amt){
			this.naration=n;
			this.type=t;
			this.amount=amt;
			
			balance+=getNetAmount();
			passbook.add(this);
			//passbook[nextIndex++]=this;
		}
		public long getDateTime(){ return this.dateTime;}
		public String getNaration(){ return this.naration;}
		public TransactionType getType(){ return this.type;}
		public long getAmount(){ return this.amount;}
		public long getNetAmount(){ return type.getSign()*amount;}
	}
	public enum AccType{
		SAVINGS{
			public int getSign(){return -1;}
		},
		CURRENT{
			public int getSign(){return 1;}
		},
		;
		public abstract int getSign();
	}
	
	public void printPassbook(){
	/*for(i=0;i<nextIndex;i++){
	System.out.printf("balance = %d  naration= %d ",passbook[i].amount,passbook[i].naration);
	}*/
	long runningBalance=0;
	
	for(Transaction T:passbook){
		runningBalance+=T.getNetAmount();
		//System.out.printf("%tc %15s %12d %12d %12d\n",T.getDateTime(),T.getNaration(),T.getType()==TransactionType.DEBIT?T.getAmount():0,T.getType()==TransactionType.CREDIT?T.getAmount:0,this.runningBalance);
		System.out.println(" Balance : "+runningBalance+" Date of Transaction : "+T.getDateTime()+" Transaction  : "+T.getNaration()+" TransactionType "+T.getType()+" Amount "+T.getAmount());
	}	
	}
	public enum TransactionType{
		CREDIT{
			public int getSign(){ return 1;}
		},
		DEBIT{
			public int getSign(){ return -1;}
		},
		;
		public abstract int getSign();
	}
	public List<Transaction> getPassbook(){
		return Collections.unmodifiableList(passbook);
	}
	Account(long AccountNumber,String name,long balance)throws NegativeAmountException {        //constructor of Accout
	this.AccountNumber=AccountNumber;
	this.name=name;
	//this.responseWriter=p;
	new Transaction("opening Balance", TransactionType.CREDIT,balance);
	if(balance<0){
		throw new NegativeAmountException("\n plzz Enter positive balance ",balance);
	}
	else{
	this.balance=balance;
	}
	}
	
	public long getAccountNumber() {
	return AccountNumber;
	}
	public long getBalance(){
		return this.balance;
	}
	 
	synchronized public void deposit(long amt)throws NegativeAmountException {
	if(amt<0){
		throw new NegativeAmountException("\n plzz Enter positive deposit value : ",amt);
	}
	else{
		new Transaction("Deposit",TransactionType.CREDIT,amt);
			this.balance+=amt;
			this.notifyAll();
			}
	
	//System.out.println("\nTransaction Successful.\nNew Balance=$"+balance);
	}
	
	
	synchronized public boolean withdraw(long amt) throws NegativeAmountException{

		while(this.balance<=amt){
			try{
			this.wait(20000);
			}catch(InterruptedException io){
				System.out.println("Plzz not interrupt : ");
			}
			//System.out.println("\n balance = "+balance);
			//System.out.println("\n balance = "+amt);
			//return false;
		}
		this.balance-=amt;
		
		/*this.setPenalty((getBalance(),minBalance)->{
		System.out.println("\n Bal :"+balance+"  minbal : "+minBalance);
		long l;
		if(getBalance()<minBalance){
			return getBalance()/10;
		}
		else return 0;
	
	});*/
		new Transaction("Deposit",TransactionType.DEBIT,amt);
		return true;
	}
	
	//	this.balance-=amt;
		
	
	
	public void display() {
	System.out.println("\nName: "+this.name);
	System.out.println("\nAccount Number: "+this.AccountNumber);
	System.out.println("\nBalance:"+this.balance);
	}

	public String toString(){
	return this.getClass().getName()+":<"+this.AccountNumber+"><"+this.name+"><"+this.balance;
	}
	public int compareTo(Object o){
	return ((Long)this.AccountNumber).compareTo(((Account)o).AccountNumber);
	}
/*	public void setPenalty(Penalty p){
		
	}*/
	public boolean equals(Object o){
	if(this.getClass()!=o.getClass()){
	return false;}
	return this.AccountNumber==((Account)o).AccountNumber;
	}
	
	public int hashCode(){
	return ((Long)AccountNumber).hashCode();
	}
	/*public void setPenalty(Penalty p){
		this.penalty=p;
	}*/
}