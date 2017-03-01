package org.lightscript.rt;

import static org.lightscript.rt.LSType.STRING;

import org.lightscript.rt.interfaces.Subscriptable;

public final class LSString extends LSValue
		implements Subscriptable<LSValue> {

	private char[] buffer;

	public LSString(String of) {
		super(STRING);
		buffer = of.toCharArray();
	}

	public LSString(char[] of) {
		super(STRING);
		buffer = new char[of.length];
		System.arraycopy(of, 0, buffer, 0, of.length);
	}

	public char jGet(int index) {
		checkBounds(index);
		return buffer[index];
	}

	public char jSet(int index, char to) {
		checkBounds(index);
		return buffer[index] = to;
	}

	@Override
	public LSValue get(int index) {
		checkBounds(index);
		return new LSNumber((long) buffer[index]);
	}

	@Override
	public LSValue getCopy(int index) {
		return get(index);
	}

	@Override
	public LSValue set(int index, LSValue t) {
		checkBounds(index);
		if (t instanceof LSNumber) {
			jSet(index, ((LSNumber) t).jChar());
			return t;
		}

		throw new TypeMismatchException("expected org.lightscript.rt.LSNumber type, got " + t.typeString());
	}

	@Override
	public LSValue setCopy(int index, LSValue t) {
		return set(index, t);
	}

	public int capacity() {
		return buffer.length;
	}

	@Override
	public int size() {
		return strlen(buffer);
	}

	@Override
	@SuppressWarnings("unchecked")
	public LSString copy() {
		return new LSString(buffer);
	}

	private void checkBounds(int index) {
		if (index < 0 || index >= capacity())
			throw new StringIndexOutOfBoundsException(index);
	}

	@Override
	public String printString() {
		return new String(buffer);
	}

	@Override
	public String toString() {
		return "'" + decodeEscapes(printString()) + "'";
	}

	private static int strlen(final char[] str) {
		int len = 0;
		while (str[len++] != 0);
		return len;
	}

	public static String decodeEscapes(String of) {
		String str = new String();
		char[] chars = of.toCharArray();
		for (char ch : chars)
			switch (ch) {
			case '\n':
				str += "\\n";
				break;
			case '\r':
				str += "\\r";
				break;
			case '\0':
				str += '\0';
				break;
			case '\b':
				str += "\\b";
				break;
			case '\f':
				str += "\\f";
				break;
			case '\t':
				str += "\\t";
				break;
			case '"':
				str += "\\\"";
				break;
			case '\\':
				str += "\\\\";
				break;
			case 0x3f: // \?
				str += "\\?";
				break;
			case 0x1b: // \e
				str += "\\e";
				break;
			default:
				str += ch;
				break;
			}
		return str;
	}

}
