package org.lightscript.rt;

import static org.lightscript.rt.LSType.DECIMAL;
import static org.lightscript.rt.LSType.INTEGER;

public final class LSNumber extends LSValue implements Comparable<LSNumber> {

	private Long	integerValue;
	private Double	decimalValue;

	public LSNumber(Double value) {
		super(DECIMAL);
		decimalValue = value;
	}

	public LSNumber(Long value) {
		super(INTEGER);
		integerValue = value;
	}
	
	public LSNumber(String value) {
		try {
			integerValue = Long.valueOf(value);
			type = INTEGER;
		} catch (Exception e) {
			try {
				decimalValue = Double.valueOf(value);
				type = DECIMAL;
			} catch (Exception e2) {
				System.err.println("illegal number format for input '" + value + "'");
			}
		}
	}

	public LSNumber toDecimal() {
		return type == DECIMAL ? new LSNumber(decimalValue) : new LSNumber(integerValue.doubleValue());
	}

	public LSNumber toInteger() {
		return type == DECIMAL ? new LSNumber(decimalValue.longValue()) : new LSNumber(integerValue);
	}
	
	public float jFloat() {
		return (float) jDouble();
	}
	
	public double jDouble() {
		return type == INTEGER ? integerValue.doubleValue() : decimalValue;
	}

	public char jChar() {
		return type == INTEGER ? (char) jInt() : 0;
	}
	
	public int jInt() {
		return (int) jLong();
	}
	
	public long jLong() {
		return type == INTEGER ? integerValue : decimalValue.longValue();
	}
	
	@SuppressWarnings("unchecked")
	public LSNumber copy() {
		return new LSNumber(type == DECIMAL ? decimalValue : integerValue);
	}
	
	@Override
	public int compareTo(LSNumber o) {
		if (o.type == type)
			return type == DECIMAL ? decimalValue.compareTo(o.decimalValue) : integerValue.compareTo(o.integerValue);
		return type == DECIMAL ? decimalValue.compareTo(o.integerValue.doubleValue()) : integerValue.compareTo(o.decimalValue.longValue());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LSNumber) {
			LSNumber n = (LSNumber) obj;
			if (type == n.type)
				return type == DECIMAL ? (decimalValue == n.decimalValue) : (integerValue == n.integerValue);
		}
		return false;
	}

	@Override
	public String toString() {
		return type == DECIMAL ? decimalValue.toString() : integerValue.toString();
	}
	
	public static LSNumber valueOf(String s) {
		return new LSNumber(s);
	}
	
}
