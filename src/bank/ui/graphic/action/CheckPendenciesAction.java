package bank.ui.graphic.action;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import bank.business.AccountManagementService;
import bank.business.BusinessException;
import bank.business.domain.EnvelopeDeposit;
import bank.business.domain.Pendency;
import bank.business.domain.Pendency.State;
import bank.ui.TextManager;
import bank.ui.graphic.BankGraphicInterface;
import bank.ui.graphic.GUIUtils;

public class CheckPendenciesAction extends BankAction {
	
	private class PendencyTableModel extends AbstractTableModel {

		private static final long serialVersionUID = 2497950520925208080L;

		private List<Pendency<?>> pendencies;
		private List<State> states;

		public PendencyTableModel(List<Pendency<?>> transactions) {
			this.pendencies = new ArrayList<>(transactions);
			states = new ArrayList<>();
			for (Pendency<?> pendency : pendencies)
				states.add(pendency.getState());
		}
		
		@Override
		public boolean isCellEditable(int row, int column) {
			switch (column) {
			case 6:
				return true;
			default:
				return false;
			}
		}

		@Override
		public int getColumnCount() {
			return 7;
		}

		@Override
		public String getColumnName(int column) {
			String key = null;
			switch (column) {
			case 0:
				key = "date";
				break;
			case 1:
				key = "location";
				break;
			case 2:
				key = "account";
				break;
			case 3:
				key = "operation.type";
				break;
			case 4:
				key = "details";
				break;
			case 5:
				key = "amount";
				break;
			case 6:
				key = "state";
				break;
			default:
				assert false;
				break;
			}
			return textManager.getText(key);
		}

		@Override
		public int getRowCount() {
			return pendencies.size();
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Pendency<?> t = pendencies.get(rowIndex);
			Object val = null;
			switch (columnIndex) {
			case 0:
				val = GUIUtils.DATE_TIME_FORMAT.format(t.getDate());
				break;
			case 1:
				val = t.getLocation();
				break;
			case 2:
				val = String.format("AG %d CC %d", t.getAccount().getId().getBranch().getNumber(), t.getAccount().getId().getNumber());
				break;
			case 3:
				val = textManager.getText("operation."
						+ t.getClass().getSimpleName());
				break;
			case 4:
				if (t instanceof EnvelopeDeposit) {
					val = ((EnvelopeDeposit) t).getEnvelope();
				} else {
					assert false;
				}
				break;
			case 5:
				if (t instanceof EnvelopeDeposit) {
					val = "+ " + t.getAmount();
				} else {
					assert false;
				}
				break;
			case 6:
				val = states.get(rowIndex);
				break;
			default:
				assert false;
				break;
			}
			return val;
		}
		
		@Override
		public void setValueAt(Object value, int row, int column) {
			if (column != 6) return;
			
			states.set(row, (State) value);
		}

	}

	private static final long serialVersionUID = 5090183202921964451L;

	private AccountManagementService accountManagementService;

	private JPanel cards;
	private JDialog dialog;
	private JTable pendenciesTable;
	
	public CheckPendenciesAction(BankGraphicInterface bankInterface,
			TextManager textManager,
			AccountManagementService accountManagementService) {
		super(bankInterface, textManager);
		this.accountManagementService = accountManagementService;
		
		super.putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		super.putValue(Action.NAME, textManager.getText("action.check.pendencies"));
	}

	public void close() {
		dialog.dispose();
		dialog = null;
	}

	@Override
	public void execute() {
		JPanel accountPanel = new JPanel(new GridLayout(2, 2, 5, 5));

		// Cards
		JPanel radioBtPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		this.cards = new JPanel(new CardLayout());

		JPanel cardsPanel = new JPanel();
		cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.PAGE_AXIS));
		cardsPanel.add(accountPanel);
		cardsPanel.add(radioBtPanel);
		cardsPanel.add(cards);

		// Confirmation Buttons
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton cancelButton = new JButton(textManager.getText("button.close"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
		});
		buttonsPanel.add(cancelButton);
		JButton okButton = new JButton(textManager.getText("button.ok"));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				executeChanges();
			}
		});
		buttonsPanel.add(okButton);

		// Statement result
		JPanel transactionsPanel = new JPanel();
		transactionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		pendenciesTable = new JTable();
		JScrollPane scrollPane = new JScrollPane(pendenciesTable,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		transactionsPanel.add(scrollPane);
		
		pendenciesTable.setModel(new PendencyTableModel(accountManagementService.getAllPendencies()));

		TableColumn column = pendenciesTable.getColumnModel().getColumn(6);
		column.setCellEditor(new DefaultCellEditor(new JComboBox<State>(State.values())));
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(cardsPanel, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		JPanel pane = new JPanel(new BorderLayout());
		pane.add(mainPanel, BorderLayout.NORTH);
		pane.add(transactionsPanel, BorderLayout.CENTER);

		this.dialog = GUIUtils.INSTANCE.createDialog(bankInterface.getFrame(),
				"action.statement", pane);
		this.dialog.setVisible(true);
	}
	
	private void executeChanges() {
		try {
			Map<Pendency<?>, State> newStates = new HashMap<Pendency<?>, State>();
			
			PendencyTableModel model = (PendencyTableModel) pendenciesTable.getModel();
			
			for (int i = 0; i < model.pendencies.size(); i++)
				newStates.put(model.pendencies.get(i), model.states.get(i));
			
			accountManagementService.checkPendencies(newStates);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}


}