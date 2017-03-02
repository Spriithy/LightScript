package org.lightscript.runtime;

import java.security.SecureRandom;
import java.util.Vector;

import org.lightscript.runtime.errors.DuplicatePointerException;

/**
 * This class wraps everything related to pointers within the Virtual Machin and it's
 * environment.
 * 
 * @author Theophile D.
 * @since 1.0
 * @version 1.1
 */
public final class Pointers {

	// Must not be instantiated
	private Pointers() {}

	// This vector is used to keep track of used addresses, each newly generated Pointer's
	// address is stored in there
	static Vector<Pointer> pointers = new Vector<>();

	public static final Pointer NULL_POINTER = new Pointer(0);

	/**
	 * The Pointer class is used to identify and reference {@link RuntimeObject}s as such
	 * and differentiate them. All living Pointer objects are ensured to be unique,
	 * otherwise a {@link DuplicatePointerException} is thrown when acquiring the next
	 * already in use Pointer value.
	 * 
	 * <br>
	 * 
	 * It is important that <b>only</b> methods of this class update the pointers list, so
	 * the system can ensure the list is not updated by other methods.
	 * 
	 * @author Theophile D.
	 * @version 1.0
	 * @since 1.0
	 */
	public static final class Pointer {
		/**
		 * The fake address used by a RuntimeObject to be referenced with. Do not
		 * serialize addresses to ensure security.
		 */
		transient final int addr;

		transient RuntimeObject boundTo;

		/**
		 * Only way to acquire a new {@code Pointer} is by calling
		 * {@link Pointers.create()}
		 * 
		 * @param addr
		 *            the virtual address of the Pointer
		 */
		private Pointer(int addr) {
			this.addr = addr;

			// Throws a DuplicatePointerException if the address is already in use
			checkDuplicates(addr);

			pointers.add(this); // Remember the entry is now in use
		}

		/**
		 * Returns the integer value of the pointer.
		 * 
		 * @return the address of the pointer as an {@code int}
		 */
		public int getAddress() {
			return addr;
		}

		/**
		 * Binds the actual Pointer to a RuntimeObject.
		 * 
		 * @param to
		 *            the RuntimeObject to bind the Pointer to
		 */
		public void bind(RuntimeObject to) {
			boundTo = to;
		}

		/**
		 * Called upon free-ing the pointer.
		 */
		public void onFree() {
			if (boundTo.isFreeOK()) {
				boundTo.free();
			} else {
				boundTo.requestFree();
			}
			pointers.remove(this);
		}

		/**
		 * Returns whether the pointer is the NULL pointer.
		 * 
		 * @return true if the pointer is NULL, false otherwise
		 */
		public boolean isNull() {
			return this.equals(NULL_POINTER);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null) return false;

			// Simply compare addresses valuess
			if (obj instanceof Pointer)
				return addr == ((Pointer) obj).addr;

			return false;
		}

		@Override
		public String toString() {
			return String.format("0x%x", addr);
		}
	}

	/**
	 * This function generates a fresh new Pointer object that is ensured to be unique.
	 * 
	 * @return a new {@link Pointer} instance
	 * @throws DuplicatePointerException
	 *             if the pointer is already in use
	 */
	public static Pointer create() {
		return new Pointer(getNextVirtualAddress());
	}

	/**
	 * Returns how many unique Pointers have been generated yet.
	 * 
	 * @return the size of the Pointer vector
	 */
	public static int count() {
		return pointers.size();
	}

	/**
	 * Frees a Pointer.
	 * 
	 * @param ptr
	 *            the Pointer to free
	 */
	public static void free(Pointer pointer) {
		pointer.onFree();
	}

	/**
	 * Frees every assigned Pointers
	 */
	public static void freeAll() {
		for (Pointer pointer : pointers)
			free(pointer);
	}

	// Check for duplicates in the Pointer vector
	private static void checkDuplicates(int addr) {
		for (Pointer pointer : pointers) {
			if (pointer.addr == addr)
				throw new DuplicatePointerException(addr);
		}
	}

	// Used to generate random and unique addresses
	private static SecureRandom magic;

	// Uses the SecureRandom class to acquire most-likely random & unique integers to use
	// as pointer values.
	private static int getNextVirtualAddress() {
		// Get new instance if needed
		if (magic == null)
			magic = new SecureRandom();

		return magic.nextInt();
	}

}
