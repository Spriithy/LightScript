package org.lightscript.runtime;

public final strictfp class Constants {

	// Should not be instantiated
	private Constants() {}

	public static final int	MIN_POOL_SIZE	= 1 << 5;
	public static final int	MAX_POOL_SIZE	= 1 << 10;

	public static final double[]	DECIMAL_POOL	= loadDecimalPool();
	public static final long[]		INTEGER_POOL	= loadIntegerPool();
	public static final byte[][]	STRING_POOL		= loadStringPool();

	public static double getDouble(int index) {
		if (index >= DECIMAL_POOL.length)
			throw new IndexOutOfBoundsException("Double pool index out of bounds (index=" + index + ")");

		// overflow-aware
		return DECIMAL_POOL[index];
	}

	public static long getInteger(int index) {
		if (index >= INTEGER_POOL.length)
			throw new IndexOutOfBoundsException("Integer pool index out of bounds (index=" + index + ")");

		// overflow-aware
		return INTEGER_POOL[index];
	}

	public static byte[] getString(int index) {
		if (index >= STRING_POOL.length)
			throw new IndexOutOfBoundsException("String pool index out of bounds (index=" + index + ")");

		// It's important to clone the char array so further modifications happen on a safe copy of the string
		// to ensure constant values of data pools.
		// overflow-aware
		return STRING_POOL[index].clone();
	}

	private static double[] loadDecimalPool() {
		// TODO real implementation

		double[] pool = new double[MIN_POOL_SIZE];
		pool[0] = 0.0;
		pool[1] = 1.0;
		pool[2] = -1.0;
		return pool;
	}

	private static long[] loadIntegerPool() {
		// TODO real implementation

		long[] pool = new long[MIN_POOL_SIZE];
		pool[0] = 0;
		pool[1] = 1;
		pool[2] = -1;
		pool[3] = 2;
		pool[4] = -2;
		return pool;
	}

	private static byte[][] loadStringPool() {
		// TODO real implementation

		byte[][] pool = new byte[100][];
		pool[0] = "".getBytes();
		return pool;
	}

}
