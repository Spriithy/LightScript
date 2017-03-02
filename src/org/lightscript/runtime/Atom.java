package org.lightscript.runtime;

import static org.lightscript.runtime.RuntimeObject.RuntimeObjectKind.*;

public class Atom extends RuntimeObject {

	protected Double	decimalValue;
	protected Long		integerValue;
	protected byte[]	bytes;

	Atom() {
		super();
	}

	public Atom(float f) {
		this(DECIMAL, 0);
		decimalValue = new Double(f);
	}

	public Atom(double d) {
		this(DECIMAL, 0);
		decimalValue = new Double(d);
	}

	public Atom(char c) {
		this(INTEGER, 0);
		integerValue = new Long(c);
	}

	public Atom(byte b) {
		this(INTEGER, 0);
		integerValue = new Long(b);
	}

	public Atom(short s) {
		this(INTEGER, 0);
		integerValue = new Long(s);
	}

	public Atom(int i) {
		this(INTEGER, 0);
		integerValue = new Long(i);
	}

	public Atom(long l) {
		this(INTEGER, 0);
		integerValue = new Long(l);
	}

	public Atom(String s) {
		this(STRING, 0);
		bytes = s.getBytes();
	}

	public Atom(byte[] bytes) {
		this(STRING, 0);
		bytes = bytes.clone();
	}

	public Atom(RuntimeObjectKind k, int index) {
		if (!k.isAtomic())
			throw new IllegalArgumentException(k.toString() + " is not an Atomic type");
		kind = k;

		switch (kind) {
		case DECIMAL:
			decimalValue = Constants.getDouble(index);
			break;
		case INTEGER:
			integerValue = Constants.getInteger(index);
			break;
		case STRING:
			// Copy is implicit in Constants.getString()
			bytes = Constants.getString(index);
			break;
		default:
			break;
		}
	}

	public float floatValue() {
		return kind.is(DECIMAL) ? decimalValue.floatValue() : (float) integerValue.intValue();
	}

	public double doubleValue() {
		return kind.is(DECIMAL) ? decimalValue : integerValue.doubleValue();
	}

	public char charValue() {
		return kind.is(DECIMAL) ? 0 : (char) integerValue.intValue();
	}

	public byte byteValue() {
		return kind.is(DECIMAL) ? 0 : (byte) integerValue.intValue();
	}

	public short shortValue() {
		return kind.is(DECIMAL) ? 0 : (short) integerValue.intValue();
	}

	public int intValue() {
		return kind.is(DECIMAL) ? decimalValue.intValue() : integerValue.intValue();
	}

	public long longValue() {
		return kind.is(DECIMAL) ? decimalValue.longValue() : integerValue.longValue();
	}

	
	@Override
	public boolean isFreeOK() {
		return true;
	}

	@Override
	public void free() {

	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Atom)
			return ((Atom) obj).pointer.equals(pointer);
	
		if (obj instanceof RuntimeObject)
			return ((RuntimeObject) obj).pointer.equals(pointer);
		
		return false;
	}

	@Override
	public int hashCode() {
		switch (kind) {
		case STRING:
			return bytes.hashCode();
		case INTEGER:
		case DECIMAL:
		case NONE:
			return super.hashCode();
			
		default:
			break;
		}
		
		// Should never happen
		return super.hashCode();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Atom copy() {
		switch (kind) {
		case STRING:
			return new Atom(bytes);
		case INTEGER:
			return new Atom(integerValue);
		case DECIMAL:
			return new Atom(decimalValue);
		case NONE:
			return new Atom();

		// Should not happen anyways
		default:
			return null;
		}
	}

}
