package org.lightscript.runtime.errors;

/**
 * When a builtin action requires a specific data type to be provided yet received something else.
 * 
 * @author Theophile D.
 * @version 1.0
 */
public class TypeMismatchException extends Error {
	private static final long serialVersionUID = -2375950705359478796L;
	
	/**
	 * Constructs a new TypeMismatchException.
	 * 
	 * @param t1 the type received that didn't match
	 * @param t2 the expected type
	 */
	public TypeMismatchException(String t1, String t2) {
		super(t2 + " does not match expected type " + t1);
	}

}
