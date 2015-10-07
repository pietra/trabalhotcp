package bank.business.domain;

import bank.business.BusinessException;

public abstract class Pendency<T extends Transaction> extends Transaction {

	public enum State {
		PENDING, APPROVED, REJECTED;
	}
	
	private State state;
	
	private T pendingTransaction;
	
	protected Pendency(OperationLocation location, CurrentAccount account,
			double amount) {
		super(location, account, amount);
		state = State.PENDING;
	}
	
 	public State getState() {
 		return state;
 	}
 	
 	protected void setState(State state) {
 		this.state = state;
 	}
 	
 	public T getTransaction() {
 		return (T) pendingTransaction;
 	}
 	
 	protected void setPendingTransaction(T pendingTransaction) {
 		this.pendingTransaction = pendingTransaction;
 	}
 	
	public abstract void approve() throws BusinessException;
	
	public abstract void reject() throws BusinessException;
}
