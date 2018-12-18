package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class HistoryComposite extends Composite implements CalculatedListener {

	private List list;

	public HistoryComposite(Composite parent){
		super(parent, SWT.NONE);
		buildControls();
	}

	private void buildControls() {
		setLayout(new FillLayout());
		list = new List(this, SWT.SINGLE | SWT.V_SCROLL);
	}

	@Override
	public void calculated(CalculatedEvent event) {
		list.add(String.valueOf(event.operand1)
				+ String.valueOf(event.operation)
				+ String.valueOf(event.operand2)
				+ " = "
				+ String.valueOf(event.result),
				0);
	}
}
