package org.lightscript.runtime;

import org.lightscript.runtime.Pointers.Pointer;

public class RuntimeObject {

	public static final RuntimeObject NULL = new RuntimeObject(Pointers.NULL);

	public transient Pointer pointer = Pointers.create();

	private RuntimeObject(Pointer ptr) {
		pointer = ptr;
	}

}
