package bank.business.domain;

/**
 * @author Ingrid Nunes
 * 
 */
public class Withdrawal extends TaxedTransaction {

	public Withdrawal(OperationLocation location, CurrentAccount account,
			double amount) {
		super(location, account, amount);
	}

}
