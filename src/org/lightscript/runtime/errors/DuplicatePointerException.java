package org.lightscript.runtime.errors;

import static java.lang.Integer.toHexString;
import static org.lightscript.runtime.Pointers.count;

public class DuplicatePointerException extends Error {
	private static final long serialVersionUID = -4184038697438755159L;

	// Statistically happened around 100k generations on several computers (8Gb RAM each)

	public DuplicatePointerException(int ptr) {
		super("after " + count() + " generations (0x" + toHexString(ptr) + ")");
	}

}
