package test;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CalculatorComposite extends Composite {

	private Text operand1;
	private Text operand2;
	private Text result;
	private Combo operationPicker;
	private Button calculateOnTheFly;
	private Button calculate;
	private Label label;
	private Label stub;

	public static final char[] OPERATIONS = new char[]{
			'+', '-', '/', '*'
	};

	private VerifyListener verifyListener = new VerifyListener(){

		@Override
		public void verifyText(VerifyEvent e) {
			if(!e.text.equals("")){
				if(e.text.charAt(0) == '.'
					|| Character.isDigit(e.text.charAt(0)))
					e.doit = true;
				else if(!Character.isDigit(e.text.charAt(0)))
					e.doit = false;
			}
		}

	};

	private ModifyListener modifyListener = new ModifyListener(){

		@Override
		public void modifyText(ModifyEvent e) {
			setResult();
		}

	};

	private SelectionAdapter selectionAdapter = new SelectionAdapter(){

		@Override
		public void widgetSelected(SelectionEvent e) {
			setResult();
		}
	};

	private ArrayList<CalculatedListener> calculatedListeners = new ArrayList<>();

	public CalculatorComposite(Composite parent){
		super(parent, SWT.NONE);
		buildControls();
		setupLayout();
	}

	private void buildControls() {
		// 1st operand
		operand1 = new Text(this, SWT.SINGLE | SWT.RIGHT);
		operand1.setMessage("1st number");
		operand1.addVerifyListener(verifyListener);

		// operation picker
		operationPicker = new Combo(this, SWT.READ_ONLY);
		for(char operation : OPERATIONS)
			operationPicker.add(String.valueOf(operation));
		operationPicker.select(0);

		// 2nd operand
		operand2 = new Text(this, SWT.SINGLE);
		operand2.setMessage("2nd number");
		operand2.addVerifyListener(verifyListener);

		// fills the space between input and output fields
		stub = new Label(this, SWT.NONE);

		// Option 'Calculate on the fly'
		calculateOnTheFly = new Button(this, SWT.CHECK);
		calculateOnTheFly.setText("Calculate on the fly");
		calculateOnTheFly.addSelectionListener(new SelectionAdapter(){
			@Override
	        public void widgetSelected(SelectionEvent event) {
				if(calculateOnTheFly.getSelection()){
					calculate.setEnabled(false);
					operand1.addModifyListener(modifyListener);
					operand2.addModifyListener(modifyListener);
					operationPicker.addSelectionListener(selectionAdapter);
				} else{
					calculate.setEnabled(true);
					operand1.removeModifyListener(modifyListener);
					operand2.removeModifyListener(modifyListener);
					operationPicker.removeSelectionListener(selectionAdapter);
				}
	        }
		});

		// Calculate button
		calculate = new Button(this, SWT.PUSH);
		calculate.setText("Calculate");
		calculate.addSelectionListener(selectionAdapter);

		// Result label
		label = new Label(this, SWT.RIGHT);
		label.setText("Result:");

		// Result field
		result = new Text(this, SWT.SINGLE | SWT.RIGHT);
		result.setMessage("0.000");
		result.setEditable(false);
	}

	private void setupLayout(){
		setLayout(new GridLayout(3, false));

		GridData data = new GridData(GridData.BEGINNING);
		operand1.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		operationPicker.setLayoutData(data);

		data = new GridData(GridData.END | GridData.VERTICAL_ALIGN_CENTER);
		operand2.setLayoutData(data);

		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		data.verticalSpan = 2;
		stub.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		calculateOnTheFly.setLayoutData(data);

		data = new GridData(GridData.END);
		calculate.setLayoutData(data);

		data = new GridData(GridData.BEGINNING);
		// data.horizontalAlignment = SWT.END;
		label.setLayoutData(data);

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		result.setLayoutData(data);
	}

	private void setResult(){
		result.setText(String.valueOf(calculate(
				getFirstOperand(),
				getSecondOperand(),
				operation())));
	}


	protected double calculate(double first, double second, char operation) {
		double result;
		switch(operation){
			case '+': result = first + second; break;
			case '-': result = first - second; break;
			case '/': result = first / second; break;
			case '*': result = first * second; break;
			default:  result = Double.NaN;
		}

		notifyCalculatedListeners(first, operation, second, result);

		return result;
	}

	private void notifyCalculatedListeners(double first, char operation, double second, double result) {
		for(CalculatedListener listener : calculatedListeners)
			listener.calculated(new CalculatedEvent(this, first, operation, second, result));
	}

	protected char operation() {
		return OPERATIONS[operationPicker.getSelectionIndex()];
	}

	protected double getSecondOperand() {
		String str = operand2.getText();
		if(str.equals(""))
			return Double.NaN;
		else
			return Double.parseDouble(str);
	}

	protected double getFirstOperand() {
		String str = operand1.getText();
		if(str.equals(""))
			return Double.NaN;
		else
			return Double.parseDouble(str);
	}

	public void addCalculatedListener(CalculatedListener listener){
		calculatedListeners.add(listener);
	}

	public void removeCalculatedListener(CalculatedListener listener){
		calculatedListeners.remove(listener);
	}
}
