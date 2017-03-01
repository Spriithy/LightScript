package org.lightscript.rt;

import static org.lightscript.rt.LSType.BOOLEAN;

public final class LSBoolean extends LSValue implements Comparable<LSBoolean> {

	public static final LSBoolean TRUE = new LSBoolean(true);
	public static final LSBoolean FALSE = new LSBoolean(false);

	private final Boolean value;
	
	private LSBoolean(boolean value) {
		super(BOOLEAN);
		this.value = value;
	}
	
	@SuppressWarnings("unchecked")
	public LSBoolean copy() {
		return value ? TRUE : FALSE;
	}
	
	@Override
	public int compareTo(LSBoolean o) {
		return value.compareTo(o.value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LSBoolean) {
			return value == ((LSBoolean) obj).value;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	public static LSBoolean valueOf(String str) {
		return Boolean.valueOf(str) ? TRUE : FALSE;
	}

}
