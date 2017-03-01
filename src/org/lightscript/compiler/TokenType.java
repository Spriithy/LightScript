package org.lightscript.compiler;

public enum TokenType {
	NULL("null"),
	TRUE("true"),
	FALSE("false"),
	FUNC("func"),
	RETURN("return"),
	IF("if"),
	ELSE("else"),
	WHILE("while"),
	FOR("for"),
	IMPORT("import"),
	PUBLIC("public"),
	PRIVATE("private"),
	STATIC("static"),
	
	L_PAREN("("),
	R_PAREN(")"),
	L_BRAKT("["),
	R_BRAKT("]"),
	L_CURLY("{"),
	R_CURLY("}"),
	ESPILON(""),
	EOL(""),
	EOF(""),
	
	COMMA(","),
	SEMI_COLON(";"),
	COLON(":"),
	BLOCK("::"),
	ELLIPSIS("..."),
	
	INTEGER(""),
	DECIMAL(""),
	STRING_LIT(""),
	IDENTIFIER(""),
	
	DOT("."),
	LEN("#"),
	PLUS("+"),
	MINUS("-"),
	TIMES("*"),
	DIVIDE("/"),
	MOD("%"),
	OR("|"),
	AND("&"),
	XOR("^"),
	BANG("!"),
	TILDA("~"),
	LT("<"),
	GT(">"),
	LTE("<="),
	GTE(">="),
	L_SFT("<<"),
	R_SFT(">>"),
	ASSIGN("="),
	REF_ASSIGN(":="),
	EQUALS("=="),
	PLUS_EQ("+="),
	MINUS_EQ("-="),
	TIMES_EQ("*="),
	DIV_EQ("/="),
	MOD_EQ("%="),
	OR_EQ("|="),
	AND_EQ("&="),
	XOR_EQ("^="),
	BANG_EQ("!="),
	TILDA_EQ("~="),
	OOR("||"),
	AAND("&&"),
	PPLUS("++"),
	MMINUS("--"),
	TTIMES("**"),
	
	;

	String name;
	TokenType(String name) {
		this.name = name;
	}
	
	public boolean isKeyWord() {
		return ordinal() <= STATIC.ordinal();
	}
	
	public boolean isPunctuation() {
		return ordinal() >= L_PAREN.ordinal() && ordinal() <= ELLIPSIS.ordinal();
	}
	
	public boolean isLitteral() {
		return ordinal() >= INTEGER.ordinal() && ordinal() <= IDENTIFIER.ordinal();
	}
	
	public boolean isOperator() {
		return ordinal() >= DOT.ordinal();
	}
	
	public String toString() {
		return name;
	}

	public static Token KeywordOrIdentifier(String text, int line, int col) {
		for (TokenType tt : values())
			if (tt.isKeyWord())
				if (tt.toString().equals(text)) return new Token(text, tt, line, col);
		return new Token(text, IDENTIFIER, line, col);
	}
}
