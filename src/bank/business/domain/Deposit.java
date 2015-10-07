package bank.business.domain;

public class Deposit extends Transaction {
	
	protected Deposit(OperationLocation location, CurrentAccount account,
			double amount) {
		super(location, account, amount);
	}
	
}
