package org.lightscript.compiler;

public class Token {

	public TokenType	type;
	public String		text;
	
	public int line, col;

	@SuppressWarnings("unused")
	private Token() {}

	public Token(TokenType type, int line, int col) {
		text = type.toString();
		this.type = type;
		this.line = line;
		this.col = col;
	}
	
	public Token(Object text, TokenType type, int line, int col) {
		this.text = text.toString();
		this.type = type;
		this.line = line;
		this.col = col;
	}

	public String toString() {
		if (type.isKeyWord() || type.isPunctuation() || type.isOperator() || type.equals(TokenType.EOF) || type.equals(TokenType.EOL))
			return "<" + line + "," + col + ": " + type.name() + ">";
		return "<" + line + "," + col + ": " + type.name() + "> '" + text + "'";
	}

}
