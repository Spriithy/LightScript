package org.lightscript.runtime.errors;

import org.lightscript.runtime.RuntimeObject;

/**
 * A RuntimeCastException is thrown whenever a dynamic cast cannot or couldn't
 * be performed by the internal LightScript runtime.
 * 
 * @author Theophile D.
 * @version 1.0
 * @since 1.0
 */
public class RuntimeCastException extends Exception {
	private static final long serialVersionUID = 3308314568636419103L;

	public RuntimeCastException() {
		this("cast failed during Runtime execution");
	}

	public RuntimeCastException(RuntimeObject o1, RuntimeObject o2, String message) {
		this("RuntimeObject of type <" + o1.typeString() + "> " + message + " <" + o2.typeString() + ">");
	}

	public RuntimeCastException(String message) {
		super(message);
	}

}
