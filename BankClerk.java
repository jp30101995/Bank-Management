package bank4;
import java.util.*;
import java.io.*;
import java.util.regex.*;
import java.net.*;

public class BankClerk implements Runnable{
	private bank bank1;
	private Scanner commandScanner;
	private PrintWriter responseWriter;
	
	public BankClerk(bank b, InputStream in, OutputStream out){
		this.bank1 = b;
		this.commandScanner = new Scanner(in);
		this.responseWriter=new PrintWriter(out,true);
	}
	
	public BankClerk(bank b){
		this(b,System.in,System.out);
	}
	
	public enum Command{
		OPEN("[Oo][Pp][Ee][Nn]\\s+([SsCc])\\s+(\\w+)\\s+(\\d+)","Open <type> <name> <openBal>"),
		WITHDRAW("[Ww][Ii][Tt][Hh][Dd][Rr][Aa][Ww]\\s+(\\d+)","withdraw <openBal>"),
		DEPOSIT("[Dd][Ee][Pp][Oo][Ss][Ii][Tt]\\s+(\\d+)","Deposit<openBal>"),
		QUIT("[Qq][Uu][Ii][Tt]","Quit"),
		INVALID("Invalid Command",null),
		SAVE("[Ss][Aa][Vv][Ee]","save"),
		LIST("[Ll][iI][Ss][Tt]","list"),
		;
		
		private Pattern syntax;
		private Optional<String> help;
		
		Command(String reg, String h){
			this.syntax = Pattern.compile(reg);
			this.help = Optional.ofNullable(h);
		}
		
		public Pattern getSyntax(){
			return this.syntax;
		}
		
		public Optional<String> getHelp(){
			return this.help;
		}	

		public static Command getCommand(Matcher m){
			Command[] commands = Command.values();
			
			for(Command command : commands){
				m.usePattern(command.getSyntax());
				if(m.matches()){
					return command;
				}
			}
			
			return Command.INVALID;
		}
	}
	
	public void run() /*throws NoSuchAccountException,NegativeAmountException,IOException*/{
		Matcher matcher = Pattern.compile("").matcher("");
		Command command = null;
		
		while((command = (Command.getCommand(matcher.reset(commandScanner.nextLine())))) != Command.QUIT){
			
			switch(command){
				
				case DEPOSIT:
						long amt=Long.parseLong(matcher.group(2));
						bank1.deposit(50001,amt);
						continue;
				case SAVE:
				try{
				bank1.save();}
				catch(IOException ioe){
						responseWriter.println("\n Cann't save ");
				
				}continue;
				case LIST:
						bank1.listAccounts(responseWriter);
				case OPEN : 
						char typeChar = matcher.group(1).charAt(0);
						Account.AccType type = Account.AccType.CURRENT;	// default
							if(typeChar == 's' || typeChar == 'S'){
								type = Account.AccType.SAVINGS;
							}
						long openBal = Long.parseLong(matcher.group(3));	
						String name = matcher.group(2);
						long acno=0;
						try{
						acno = bank1.openAccount(type,openBal,name);
						responseWriter.println("\n Congratulations! Opened New Bank Account!");
						bank1.display(acno);
						}
						catch(NegativeAmountException|NoSuchAccountException nae){
							nae.printStackTrace();
							System.out.println("Negative amoutis entered : ");
							continue;
						}
						//use try catch for negative amount
						
						
						continue;
				case INVALID : 
						//System.out.println("\n Invalid Command ");
						responseWriter.println("\n Invalid Command ");
						continue;	
			}
		}
		
	}
	
	/*public static void main(String[] args) throws Exception{
		String bankName = args[0];
		int bankCode = Integer.parseInt(args[1]);
		bank bank = new bank(bankName , bankCode);
		BankClerk clerk = new BankClerk(bank);
		clerk.run();
	}*/
	public static void main(String[] args) throws Exception{
		String bankName = args[0];
		int portNo=Integer.parseInt(args[1]);
		bank Bank1=null;
		File bankFile=new File(bankName+".bank");
		if(bankFile.exists()){
			Bank1 = bank.load(bankName);
			System.out.println("  Readinng from file : ");
		}
		else{
			Bank1=new bank(bankName,5);
			System.out.println("  writing from file : ");
		}
		ServerSocket board=new ServerSocket(portNo);
		while(true){
			Socket phone=board.accept();
			BankClerk clerk = new BankClerk(Bank1,phone.getInputStream(),phone.getOutputStream());
			clerk.run();
		}
		
	}
} 