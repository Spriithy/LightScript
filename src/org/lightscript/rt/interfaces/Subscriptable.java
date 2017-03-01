package org.lightscript.rt.interfaces;

import org.lightscript.rt.LSValue;

public interface Subscriptable<T extends LSValue> {
	
	public T get(int index);
	public T getCopy(int index);
	
	public T set(int index, T t);
	public T setCopy(int index, T t);

	public int size();
	public int capacity();
	
}
