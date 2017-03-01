package org.lightscript.rt.interfaces;

import org.lightscript.rt.LSValue;

public interface Growable<T extends LSValue> {
	
	public boolean append(T t);
	public boolean appendCopy(T t);
	
}
