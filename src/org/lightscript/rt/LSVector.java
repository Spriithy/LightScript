package org.lightscript.rt;

import static org.lightscript.rt.LSType.NULL;
import static org.lightscript.rt.LSType.VECTOR;

import java.util.Vector;

import org.lightscript.rt.interfaces.Growable;
import org.lightscript.rt.interfaces.Subscriptable;

public final class LSVector extends LSValue
	implements Subscriptable<LSValue>, Growable<LSValue> {

	private Vector<LSValue> values;

	private LSType	ofType;
	private String	ofComposite	= VECTOR.toString();
	private boolean	ofExpected	= false;

	public LSVector(LSType of) {
		super(VECTOR);

		if (of == NULL)
			throw new TypeMismatchException("cannot create org.lightscript.rt.Vector of null objects");

		ofType = of;
		ofComposite = ofType.toString();

		if (of.isComposite())
			ofExpected = true;

		values = new Vector<>();
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
			ofComposite = ofType.toString();

		values = new Vector<>(initialCapacity);

		while (initialCapacity-- > 0)
			append(LS_NULL);
	}
	
	public LSType ofType() {
		return ofType;
	}

	@Override
	public boolean append(LSValue value) {
		if (value == null)
			return append(LS_NULL);

		if (ofExpected)
			handleNewEntry(value);

		checkType(value);

		return values.add(value);
	}

	@Override
	public boolean appendCopy(LSValue value) {
		return append(value.copy());
	}
	
	@Override
	public LSValue set(int index, LSValue value) {
		checkBounds(index);
		
		if (value == null)
			return set(index, LS_NULL);

		if (ofExpected)
			handleNewEntry(value);

		checkType(value);

		return values.set(index, value);
	}
	
	@Override
	public LSValue setCopy(int index, LSValue value) {
		return set(index, value.copy());
	}

	@Override
	public LSValue get(int index) {
		checkBounds(index);
		return values.get(index);
	}
	
	@Override
	public LSValue getCopy(int index) {
		checkBounds(index);
		return values.get(index).copy();
	}
	
	@Override
	public int size() {
		return values.size();
	}
	
	@Override
	public int capacity() {
		return values.capacity();
	}

	private void checkType(LSValue value) {
		if (value.isNull())
			return;

		if (!ofComposite.equals(value.typeString()))
			throw new TypeMismatchException(value.typeString() + " differs from " + ofComposite);
	}

	private void handleNewEntry(LSValue value) {
		if (value.isNull()) return;

		if (value instanceof LSVector)
			ofComposite = ((LSVector) value).typeString();
		else
			ofComposite = value.typeString();

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
			if (ofComposite != ((LSVector) obj).ofComposite) return false;
			return values.equals(((LSVector) obj).values);
		}
		return false;
	}

	@Override
	public String typeString() {
		return ofComposite + "[]";
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
	
	private void checkBounds(int index) {
		if (index >= values.size() || index < 0)
			throw new IndexOutOfBoundsException(Integer.toString(index));
	}

}
