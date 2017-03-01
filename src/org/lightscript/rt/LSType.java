package org.lightscript.rt;

public enum LSType {

	NONE("none"),
	NULL("null"),
	BOOLEAN("boolean"),
	FUNCTION("function"),
	INTEGER("int"),
	DECIMAL("float"),
	STRING("string"),
	VECTOR("vector"),
	TABLE("table"),
	OBJECT("object");

	private final String name;

	LSType(String name) {
		this.name = name;
	}

	public boolean isNull() {
		return this == NULL;
	}

	public boolean isNative() {
		return this != OBJECT;
	}

	public boolean isComposite() {
		return this == VECTOR || this == TABLE || this == OBJECT;
	}

	public boolean isCallable() {
		return this == FUNCTION;
	}

	public boolean isNumeric() {
		return this == INTEGER || this == DECIMAL || this == BOOLEAN;
	}

	public String toString() {
		return name;
	}

	public static void requireSameType(LSType t1, LSType t2) {
		if (t1 != t2)
			throw new TypeMismatchException(t2.toString() + " differs from " + t1.toString());
	}

	public static void requireSameTypeOrNull(LSType t1, LSType t2) {
		if (t1 == NULL || t2 == NULL) return;
		if (t1 != t2)
			throw new TypeMismatchException(t2.toString() + " differs from " + t1.toString());
	}
}
