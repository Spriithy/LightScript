package org.lightscript.runtime;

import static org.lightscript.runtime.RuntimeObject.RuntimeObjectKind.FREE;
import static org.lightscript.runtime.RuntimeObject.RuntimeObjectKind.NONE;

import org.lightscript.runtime.Pointers.Pointer;
import org.lightscript.runtime.errors.DuplicatePointerException;

/**
 * A RuntimeObject is the top-level object in use in the Runtime model of the Virtual
 * Machine. It is identified by its reference {@link Pointers.Pointer} and its Kind which
 * defaults to {@link RuntimeObjectKind.NONE} for obvious reasons.
 * 
 * @author Theophile D.
 * @version 1.0
 * @since 1.0
 */
public abstract class RuntimeObject {

	/**
	 * This field represents the virtual address of the current RuntimeObject instance. It
	 * is used to identify whether an Object is identical to another one (for Pointers are
	 * unique).
	 */
	transient protected final Pointer pointer;

	/**
	 * Keep track of the reference count for this object's instance.
	 */
	transient protected int refCount;

	/**
	 * Keep track of the Object's kind. The field defaults to
	 * {@link RuntimeObjectKind.NONE} so that any new undefined instance of a
	 * RuntimeObject is considered as such anyways.
	 */
	protected RuntimeObjectKind kind = NONE;

	/**
	 * Constructs a new RuntimeObject
	 * 
	 * @throws DuplicatePointerException
	 *             if the pointer assigned to the new instance is already in use
	 */
	public RuntimeObject() {
		// Get unique pointer assigned
		pointer = Pointers.create();

		// And bind it to this instance
		pointer.bind(this);
		
		// Object is not referenced by itself
		refCount = 0;
	}

	/**
	 * The only real RuntimeObject super constructor.
	 * 
	 * @param kind
	 *            the starting kind of the RuntimeObject
	 * 
	 * @throws DuplicatePointerException
	 *             if the pointer assigned to the new instance is already in use
	 */
	public RuntimeObject(RuntimeObjectKind kind) {
		this();
		this.kind = kind;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RuntimeObject)
			return pointer.equals(((RuntimeObject) obj).pointer);
		return false;
	}

	// TODO request environment to free this
	public abstract void free();

	public boolean isFreeOK() {
		return kind.is(FREE) && refCount == 0;
	}

	/**
	 * Marks the basic RuntimeObject for either collection from the environment or free
	 * from parent object.
	 */
	public void notifyFree() {
		if (!isFreeOK())
			kind = FREE; // Primary free notification
	}

	/**
	 * This method is used by the Pointer itself to free the Object it is bound to, the
	 * environment or any parent objects that wants this object to be freed.
	 */
	public void requestFree() {
		if (!isFreeOK())
			notifyFree(); // Notify the object it must be freed

		// Condition must be true if notifyFree() is not overwritten and doesn't change
		// the kind state change behavior, or doesn't call super.notifyFree()
		if (isFreeOK())
			free();
	}

	/**
	 * Copies the current RuntimeObject instance into a new one. The contract is that such
	 * a copy must only differ from the original by their Pointer values.
	 * 
	 * @return a new copy of the current RuntimeObject instance
	 */
	public abstract <T extends RuntimeObject> T copy();

	/**
	 * Returns a comprehensive, print-ready, String that identifies the RuntimeObject
	 * instance's type.
	 * 
	 * @return the type String of the RuntimeObject's instance
	 */
	public String typeString() {
		return kind.toString();
	}

	/**
	 * Returns the hash String of the RuntimeObject instance, based on its pointer and
	 * hashCode to ensure (close to) uniqueness.
	 * 
	 * @return the hash String of the RuntimeObject instance
	 */
	public String hashString() {
		return "<" + typeString() + "@0x" + Integer.toHexString(pointer.getAddress() + hashCode()) + ">";
	}

	@Override
	public String toString() {
		return hashString();
	}

	/**
	 * 
	 * Enumerates the several possible {@link RuntimeObject} kinds in the Runtime
	 * environment.
	 * 
	 * @author Theophile D.
	 * @version 1.0
	 * @since 1.0
	 */
	protected static enum RuntimeObjectKind {

		/**
		 * FREE is used to mark objects for garbage collection on free requests.
		 * 
		 * The FREE kind enumeration constant
		 */
		FREE("free"),

		/**
		 * NONE is used to indicate a RuntimeObject has no actual type. That is, doesn't
		 * have a defined behavior.
		 * 
		 * The NONE kind enumeration constant
		 */
		NONE("Object"),

		/**
		 * The NULL kind enumeration constant
		 */
		NULL("null"),

		/**
		 * The BOOLEAN kind enumeration constant
		 */
		BOOLEAN("boolean"),

		/**
		 * The INTEGER kind enumeration constant
		 */
		INTEGER("int"),

		/**
		 * The DECIMAL kind enumeration constant
		 */
		DECIMAL("float"),

		/**
		 * The STRING kind enumeration constant
		 */
		STRING("string"),

		//
		// Non-Atomic types
		//

		/**
		 * The VECTOR kind enumeration constant
		 */
		VECTOR("Vector"),

		/**
		 * The TABLE kind enumeration constant
		 */
		TABLE("Table"),

		/**
		 * The OBJECT kind enumeration constant
		 */
		OBJECT("Object"),

		/**
		 * The THREAD kind enumeration constant
		 */
		THREAD("Thread"),

		/**
		 * The FUNCTION kind enumeration constant
		 */
		FUNCTION("Function");

		// The type String
		private final String type;

		private RuntimeObjectKind(String text) {
			this.type = text;
		}

		/**
		 * Returns whether the kind is atomic.
		 * 
		 * @return {@code true} if the kind is atomic, {@code false} otherwise
		 */
		public boolean isAtomic() {
			// NULL and NONE are not considered atomic due to their particular status
			return ordinal() > NULL.ordinal() && ordinal() <= STRING.ordinal();
		}

		/**
		 * Returns whether the kind is composite.
		 * 
		 * @return {@code true} if the kind is composite, {@code false} otherwise
		 */
		public boolean isComposite() {
			// A composite Object is an Object that is neither bound to execution nor
			// atomic
			return this == VECTOR || this == TABLE || this == OBJECT;
		}

		/**
		 * Simply compares two kinds
		 * 
		 * @param ot
		 *            the other type to compare with
		 * @return true if the two kinds are equal, false otherwise
		 */
		public boolean is(RuntimeObjectKind ot) {
			return this == ot;
		}

		@Override
		public String toString() {
			return type;
		}
	}

}
