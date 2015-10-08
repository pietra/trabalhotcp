package bank.ui.text.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.business.AccountManagementService;
import bank.business.domain.EnvelopeDeposit;
import bank.business.domain.Pendency;
import bank.business.domain.Pendency.State;
import bank.ui.text.BankTextInterface;
import bank.ui.text.UIUtils;

public class CheckPendenciesCommand extends Command {

	private final AccountManagementService accountManagementService;

	public CheckPendenciesCommand(BankTextInterface bankInterface,
			AccountManagementService accountManagementService) {
		super(bankInterface);
		this.accountManagementService = accountManagementService;
	}
	
	@Override
	public void execute() throws Exception {
		UIUtils uiUtils = UIUtils.INSTANCE;
		
		List<Pendency<?>> pendencies = accountManagementService.getAllPendencies();
		
		Map<Pendency<?>, State> newStates = new HashMap<>();
		
		printStateCode();
		
		for (Pendency<?> pend : pendencies) {
			printPendency(pend);
			State newState = pend.getState();
			switch (uiUtils.readInteger("state.new")) {
			case 0:
				newState = State.PENDING;
				break;
			case 1:
				newState = State.APPROVED;
				break;
			case 2:
				newState = State.REJECTED;
				break;
			default:
				break;
			}
			newStates.put(pend, newState);
		}
			
		accountManagementService.checkPendencies(newStates);
		
	}
	
	private void printStateCode() {
		StringBuffer sb = new StringBuffer();
		sb.append(getTextManager().getText("message.state.codes")).append("\n");
		sb.append(String.format("0 - %s", getTextManager().getText("state.PENDING"))).append("\n");
		sb.append(String.format("1 - %s", getTextManager().getText("state.APPROVED"))).append("\n");
		sb.append(String.format("2 - %s", getTextManager().getText("state.REJECTED"))).append("\n");
		
		System.out.println(sb);
	}

	private void printPendency(Pendency<?> pend) {
		StringBuffer sb = new StringBuffer();
		sb.append(getTextManager().getText("date")).append("\t\t\t");
		sb.append(getTextManager().getText("location")).append("\t");
		sb.append(getTextManager().getText("account")).append("\t\t");
		sb.append(getTextManager().getText("operation.type")).append("\t");
		sb.append(getTextManager().getText("details")).append("\t");
		sb.append(getTextManager().getText("amount")).append("\t\t\t");
		sb.append(getTextManager().getText("state")).append("\n");
		
		sb.append(UIUtils.INSTANCE.formatDateTime(pend.getDate()))
		.append("\t");
		sb.append(pend.getLocation()).append("\t");
		sb.append(String.format("AG %d CC %d", 
				pend.getAccount().getId().getBranch().getNumber(), pend.getAccount().getId().getNumber())).append("\t");
		sb.append(getTextManager().getText(
						"operation." + pend.getClass().getSimpleName())).append("\t");
		if (pend instanceof EnvelopeDeposit) {
			sb.append(((EnvelopeDeposit) pend).getEnvelope()).append("\t\t");
			sb.append(pend.getAmount()).append("\t");
		}
		sb.append(getTextManager().getText("state." + pend.getState().toString())).append("\t");

		System.out.println(sb);
	}

}
