package bank.business.domain;

import bank.business.BusinessException;

/**
 * @author Ingrid Nunes
 * 
 */
public class EnvelopeDeposit extends Pendency<Deposit> {

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

	@Override
	public void approve() throws BusinessException {
		setState(State.APPROVED);
		Deposit deposit = getAccount().deposit(getLocation(), getAmount());
		getAccount().getDeposits().remove(deposit); // Remove depósito da lista para evitar duplicados (não é melhor solução)
		
		setPendingTransaction(deposit);
	}
	
	@Override
	public void reject() throws BusinessException {
		setState(State.REJECTED);
	}

}
