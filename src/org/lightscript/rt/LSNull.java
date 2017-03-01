package org.lightscript.rt;

public class LSNull extends LSValue {

	public static final LSNull NULL = new LSNull();

	LSNull() {
		super(LSType.NULL);
	}

	@SuppressWarnings("unchecked")
	public LSNull copy() {
		return NULL;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LSNull)
			return (LSNull) obj == NULL;
		return false;
	}
	
	@Override
	public String toString() {
		return "null";
	}

	public static boolean isNull(LSValue value) {
		return NULL == value;
	}

}
