package bank.business.domain;

/**
 * @author Ingrid Nunes
 * 
 */
public class EnvelopeDeposit extends Deposit {

	private long envelope;
	
	public EnvelopeDeposit(OperationLocation location, CurrentAccount account,
			long envelope, double amount) {
		super(location, account, amount);
		this.envelope = envelope;
	}

	/**
	 * @return the envelope
	 */
	public long getEnvelope() {
		return envelope;
	}
}
