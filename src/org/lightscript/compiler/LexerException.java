package org.lightscript.compiler;

import java.io.IOException;

public class LexerException extends IOException {
	private static final long serialVersionUID = -693154014568482078L;

	public LexerException() {}

	public LexerException(String message) {
		super(message);
	}

	public LexerException(Throwable cause) {
		super(cause);
	}

	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}

}
