package org.lightscript.rt;

import static org.lightscript.rt.LSType.*;

public final class LSString extends LSValue {

	private String buffer;
	
	public LSString() {
		this("");
	}
	
	public LSString(String text) {
		super(STRING);
		buffer = text;
	}
	
	public LSString(LSValue of) {
		super(STRING);
		if (of instanceof LSString)
			buffer = ((LSString)of).buffer;
		else
			buffer = of.printString();
	}
	
	@SuppressWarnings("unchecked")
	public LSString copy() {
		return new LSString(buffer);
	}
	
}
