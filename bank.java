package bank4;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.io.*;
public class bank implements Serializable{
	private String name;
	private long lastAccountNo; 
	private Map<Long,Account> AccountMap=new HashMap<>();
	public Account ac;
	public bank(String name,int code){
		this.name=name;
		this.lastAccountNo=code*10000L;
	}
	public long openAccount(Account.AccType type,long openBal,String name)throws NegativeAmountException,NoSuchAccountException{
		if(openBal<0){
			throw new NegativeAmountException("\n openBal is Nagative",openBal);
		}
		if(type.getSign()==1){
			ac=new CurrentAccount(++lastAccountNo,name,openBal);
		}
		else{
			ac=new SavingsAccount(++lastAccountNo,name,openBal);
		}
		AccountMap.put(lastAccountNo,ac);
		return ac.getAccountNumber();
	}
	public void display(long acno) throws NoSuchAccountException{
		getAccount(acno).display();
	}
	public Stream<Account> getAccountStream(){
		return AccountMap.values().stream();
	}
	public void listAccounts(PrintWriter out){
		StringJoiner sj=new StringJoiner("\n","List of Account"+name,"End os Account list").setEmptyValue("No Account is opened yet");
		AccountMap.values().forEach(ac->sj.add(ac.toString()));
		out.println(sj.toString());
	}
	public void listAccounts(PrintStream out){
		listAccounts(new PrintWriter(out,true));
	}
	public void listAccounts(){
		listAccounts(System.out);
	}
	
	public void setPenalty(long acno,Penalty p) throws NoSuchAccountException{
		try{
		((CurrentAccount)getAccount(acno)).setPenalty(p);
		}catch (ClassCastException cce){
			throw new NoSuchAccountException("not a current account : ",acno);
		}
	}
	public void deposit(long acno,long amt)throws NoSuchAccountException,NegativeAmountException{
		getAccount(acno).deposit(amt);
	}
	public Account getAccount(long acno)throws NoSuchAccountException{
			Account ac=AccountMap.get(acno);
			if(ac==null/*AccountMap.isEmpty() || !AccountMap.containsKey(acno)*/){
				throw new NoSuchAccountException("\n Account is not available ",acno);
			}
			return AccountMap.get(acno);
	}
	public void printPassbook(long acno)throws NoSuchAccountException{
		getAccount(acno).printPassbook();
	}

	public boolean withdraw(long acno,long amt)throws NoSuchAccountException,NegativeAmountException{
		return getAccount(acno).withdraw(amt);
	}
	public void save()throws IOException{
		try(FileOutputStream fos=new FileOutputStream(name+".bank");
			ObjectOutputStream oos=new ObjectOutputStream(fos);){
				oos.writeObject(this);
			}
	}
	public static bank load(String bankName)throws IOException,ClassNotFoundException{
		try(FileInputStream fis=new FileInputStream(bankName+".bank");
			ObjectInputStream ois=new ObjectInputStream(fis);){
				return (bank)ois.readObject();
			}
	}
}