package bank4;
import java.io.*;
import java.net.*;
public class BankServer{
	private bank bank1;
	private ServerSocket board;
	public BankServer(bank bank1,int port)throws IOException{
		this.bank1=bank1;
		this.board=new ServerSocket(port);
		
	}
	public void run() throws IOException{
		while(true){
				Socket phone=board.accept();
				BankClerk Clerk=new BankClerk(bank1,phone.getInputStream(),phone.getOutputStream());
				Thread thread =new Thread(Clerk);
				thread.start();
		}	
	}
	
	
	public static void main(String[] args)throws IOException,ClassNotFoundException{
		String bankName = args[0];
		int portNo=Integer.parseInt(args[1]);
		bank Bank1=null;
		File bankFile=new File(bankName+".bank");
		if(bankFile.exists()){
			Bank1 = bank.load(bankName);
			//System.out.println("  Readinng from file : ");
		}
		else{
			Bank1=new bank(bankName,5);
			//System.out.println("  writing from file : ");
		}
		BankServer server=new BankServer(Bank1,portNo);
		server.run();
	}
	
}