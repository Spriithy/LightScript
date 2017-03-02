package org.lightscript.runtime.errors;

public class RuntimeError extends Error {
	private static final long serialVersionUID = -4464682320030913438L;

	public RuntimeError() {
		this("unexposed error");
	}

	public RuntimeError(String message) {
		super(message);
	}

}
