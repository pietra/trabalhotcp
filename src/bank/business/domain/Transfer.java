package bank.business.domain;

/**
 * @author Ingrid Nunes
 * 
 */
public class Transfer extends TaxedTransaction {

	private CurrentAccount destinationAccount;

	public Transfer(OperationLocation location, CurrentAccount account,
			CurrentAccount destinationAccount, double amount) {
		super(location, account, amount);
		this.destinationAccount = destinationAccount;
	}

	/**
	 * @return the destinationAccount
	 */
	public CurrentAccount getDestinationAccount() {
		return destinationAccount;
	}
}
