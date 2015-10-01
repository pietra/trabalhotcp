package bank.business.domain;

public class Deposit extends Transaction {
	
	// Valor do imposto na transa��o
		private double tax;

	protected Deposit(OperationLocation location, CurrentAccount account,
			double amount) {
		super(location, account, amount);
		tax = amount * TAX;
	}

	public double getTax() {
		return tax;
	}
	
}
