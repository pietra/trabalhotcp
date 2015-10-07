/*
 * Created on 16 Dec 2013 16:17:23
 */
package bank.business;

import java.util.Date;
import java.util.List;
import java.util.Map;

import bank.business.domain.CurrentAccount;
import bank.business.domain.Employee;
import bank.business.domain.Pendency;
import bank.business.domain.Pendency.State;

/**
 * @author Ingrid Nunes
 * 
 */
public interface AccountManagementService {

	public CurrentAccount createCurrentAccount(long branch, String name,
			String lastName, int cpf, Date birthday, double balance)
			throws BusinessException;

	public Employee login(String username, String password)
			throws BusinessException;

	public void checkPendencies(Map<Pendency<?>, State> newStates)
			throws BusinessException;

	public List<Pendency<?>> getAllPendencies();
	
}
