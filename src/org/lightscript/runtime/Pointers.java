package org.lightscript.runtime;

import java.security.SecureRandom;
import java.util.Vector;

import org.lightscript.runtime.errors.DuplicatePointerException;

public final class Pointers {

	//
	// Static members
	//

	// Must not be instantiated
	private Pointers() {}

	static Vector<Integer> ptrs = new Vector<>();

	public static final Pointer NULL = new Pointer(0);

	public static final class Pointer {
		/**
		 * The fake address used by an object to be referenced with. Do not serialize addresses to ensure security.
		 */
		final transient int addr;

		/**
		 * Only way to acquire a new {@code Pointer} is by calling {@link Pointers.create()}
		 * 
		 * @param addr
		 *            the virtual address of the Pointer
		 */
		private Pointer(int addr) {
			ptrs.add(addr);
			this.addr = addr;
		}

		public int getAddress() {
			return addr;
		}

		public boolean isNull() {
			return this.equals(NULL);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;

			if (obj instanceof Pointer)
				return addr == ((Pointer) obj).addr;

			return false;
		}

		@Override
		public String toString() {
			return String.format("0x%x", addr);
		}
	}

	public static Pointer create() {
		return new Pointer(getNextVirtualAddress());
	}

	public static int count() {
		return ptrs.size();
	}

	public static void free(Pointer ptr) {
		ptrs.remove((Integer) ptr.addr);
	}

	public static void freeAll() {
		ptrs.clear();
	}

	// Used to generate random and unique addresses
	private static SecureRandom magics;

	private static int getNextVirtualAddress() {
		if (magics == null)
			magics = new SecureRandom();

		int ptr = magics.nextInt();

		// Check for duplicates
		if (ptrs.contains(ptr))
			throw new DuplicatePointerException(ptr);

		return ptr;
	}

}
