package org.lightscript.runtime;

import org.lightscript.runtime.errors.RuntimeError;

public final class Null extends RuntimeObject {

	/**
	 * The only "Null" object alive at a time in the Runtime environment
	 */
	private static final RuntimeObject NULL = new Null();

	// Must not be instanciated elsewhere
	private Null() {
		super(RuntimeObjectKind.NULL);
	}
	
	@Override
	public void free() {}

	@Override
	public boolean isFreeOK() {
		return false;
	}

	@Override
	public void notifyFree() {}
	
	@Override
	public void requestFree() {}
	
	@Override
	@SuppressWarnings("unchecked")
	public Null copy() {
		return this;
	}

	@Override
	public String toString() {
		return typeString();
	}

	/**
	 * Use to retrieve the Null object reference.
	 * 
	 * @return the Null object only instance
	 */
	public static RuntimeObject getInstance() {
		if (NULL == null)
			throw new RuntimeError("no NULL for you !");
		return NULL;
	}


}
