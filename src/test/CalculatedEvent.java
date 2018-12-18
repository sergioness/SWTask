package test;

import java.util.EventObject;

public class CalculatedEvent extends EventObject {
	public double operand1;
	public char operation;
	public double operand2;
	public double result;

	public CalculatedEvent(Object source, double o1, char op, double o2, double r){
		super(source);
		operand1 = o1;
		operation = op;
		operand2 = o2;
		result = r;
	}
}
