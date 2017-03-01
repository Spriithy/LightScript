package org.lightscript.rt;

import static org.lightscript.rt.LSType.NULL;
import static org.lightscript.rt.LSType.VECTOR;

import java.util.Vector;

public final class LSVector extends LSValue {

	private Vector<LSValue> elements;

	private LSType	ofType;
	private String	ofString	= VECTOR.toString();
	private boolean	ofExpected	= false;

	public LSVector(LSType of) {
		super(VECTOR);

		if (of == NULL)
			throw new TypeMismatchException("cannot create org.lightscript.rt.Vector of null objects");

		ofType = of;
		ofString = ofType.toString();

		if (of.isComposite())
			ofExpected = true;

		elements = new Vector<>();
	}

	public LSVector(LSType of, int initialCapacity) {
		super(VECTOR);

		if (of == NULL)
			throw new TypeMismatchException("cannot create org.lightscript.rt.Vector of null objects");

		if (initialCapacity < 0)
			throw new IllegalArgumentException("initial org.lightscript.rt.Vector cannot be negative");

		ofType = of;

		if (of.isComposite())
			ofExpected = true;
		else
			ofString = ofType.toString();

		elements = new Vector<>(initialCapacity);

		while (initialCapacity-- > 0)
			append(LS_NULL);
	}

	public LSType ofType() {
		return ofType;
	}

	public int size() {
		return elements.size();
	}
	
	public boolean append(LSValue element) {
		if (element == null)
			return append(LS_NULL);

		if (ofExpected)
			handleNewEntry(element);

		checkType(element);

		return elements.add(element);
	}

	public boolean appendCopy(LSValue element) {
		return append(element.copy());
	}
	
	public LSValue set(int idx, LSValue element) {
		if (element == null)
			return set(idx, LS_NULL);

		if (ofExpected)
			handleNewEntry(element);

		checkType(element);

		return elements.set(idx, element);
	}
	
	public LSValue setCopy(int idx, LSValue element) {
		return set(idx, element.copy());
	}

	public LSValue get(int idx) {
		return elements.get(idx);
	}

	private void checkType(LSValue el) {
		if (el.isNull())
			return;

		if (!ofString.equals(el.typeString()))
			throw new TypeMismatchException(el.typeString() + " differs from " + ofString);
	}

	private void handleNewEntry(LSValue el) {
		if (el.isNull()) return;

		if (el instanceof LSVector)
			ofString = ((LSVector) el).typeString();
		else
			ofString = el.typeString();

		ofExpected = false;
	}
	
	
	@SuppressWarnings("unchecked")
	public LSVector copy() {
		LSVector vector = new LSVector(ofType, size());
		for (int i = 0; i < size(); i++)
			vector.setCopy(i, get(i));
		return vector;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LSVector) {
			if (ofType != ((LSVector) obj).ofType) return false;
			if (ofString != ((LSVector) obj).ofString) return false;
			return elements.equals(((LSVector) obj).elements);
		}
		return false;
	}

	@Override
	public String typeString() {
		return ofString + "[]";
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(typeString());
		sb.append("{");
		for (int i = 0; i < size() - 1; i++)
			sb.append(getShortStringOf(get(i)) + ", ");

		if (size() > 0)
			sb.append(getShortStringOf(get(size() - 1)));

		return sb.toString() + "}";
	}

}
