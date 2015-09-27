package bank.business.domain;

/**
 * @author Ingrid Nunes
 * 
 */
public class Deposit extends Transaction {

	private long envelope;
	
	// Valor do imposto na transação
	private double imposto;

	public Deposit(OperationLocation location, CurrentAccount account,
			long envelope, double amount) {
		super(location, account, amount);
		this.envelope = envelope;
		imposto = amount * IMPOSTO;
	}

	/**
	 * @return the envelope
	 */
	public long getEnvelope() {
		return envelope;
	}
	
	public double getImposto() {
		return imposto;
	}

}
