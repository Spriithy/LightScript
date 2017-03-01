package org.lightscript.rt;

public class TypeMismatchException extends Error {
	private static final long serialVersionUID = -2375950705359478796L;

	public TypeMismatchException() {}

	public TypeMismatchException(String message) {
		super(message);
	}

	public TypeMismatchException(Throwable cause) {
		super(cause);
	}

	public TypeMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeMismatchException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
