package org.lightscript.rt;

public class TypeMismatchException extends Error {
	private static final long serialVersionUID = -2375950705359478796L;

	public TypeMismatchException(String t1, String t2) {
		super(t2 + " does not match expected type " + t1);
	}

}
