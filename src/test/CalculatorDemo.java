package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class CalculatorDemo {
	public static void main(String args[]){
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SWT calculator");
		shell.setSize(280, 220);
		shell.setLayout(new FillLayout());

		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);

		CalculatorComposite calculator = new CalculatorComposite(tabFolder);
		HistoryComposite history = new HistoryComposite(tabFolder);

		TabItem tab = new TabItem(tabFolder, SWT.NONE);
		tab.setText("Calculator");
		tab.setControl(calculator);
		calculator.addCalculatedListener(history);

		tab = new TabItem(tabFolder, SWT.NONE);
		tab.setText("History");
		tab.setControl(history);

		shell.open();
		while(!shell.isDisposed())
			if(!display.readAndDispatch())
				display.sleep();
		display.dispose();
	}
}
