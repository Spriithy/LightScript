package org.lightscript.rt;

public abstract class LSValue {
	
	public static final LSValue LS_NULL = LSNull.NULL;
	public static final LSValue LS_TRUE = LSBoolean.TRUE;
	public static final LSValue LS_FALSE = LSBoolean.FALSE;

	protected LSType type;
	
	LSValue() {}
	
	protected LSValue(LSType type) {
		this.type = type;
	}
	
	public boolean isNull() {
		return type.isNull();
	}
	
	public boolean isNative() {
		return type.isNative();
	}
	
	public boolean isComposite() {
		return type.isComposite();
	}
	
	public boolean isCallable() {
		return type.isCallable();
	}
	
	public boolean isNumeric() {
		return type.isNumeric();
	}
	
	public LSType type() {
		return type;
	}
	
	public abstract <T extends LSValue> T copy();
	
	public String typeString() {
		return type.toString();
	}
	
	public String printString() {
		return toString();
	}
	
	public String hashString() {
		return String.format("<%s@%x>", typeString(), hashCode());
	}
	
	@Override
	public String toString() {
		return hashString();
	}
	
	public static String getShortStringOf(LSValue val) {
		if (val.isComposite())
			return val.hashString();
		return val.toString();
	}
	
}
