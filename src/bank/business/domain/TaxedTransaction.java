package bank.business.domain;

public class TaxedTransaction extends Transaction {

	public static final double TAX = 0.002;
	
	private double tax;
	
	protected TaxedTransaction(OperationLocation location,
			CurrentAccount account, double amount) {
		super(location, account, amount);
		tax = amount * TAX;
	}
	
	public double getTax() {
		return tax;
	}
	
	public double getAmountPlusTax() {
		return tax + getAmount();
	}
}
