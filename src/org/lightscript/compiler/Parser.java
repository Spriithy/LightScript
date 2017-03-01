package org.lightscript.compiler;

import java.util.LinkedList;

import org.lightscript.compiler.ast.AST;

public class Parser {
	
	private String file;
	private Lexer lexer;
	private LinkedList<Token> tokens;
	
	public Parser(String path) throws Exception {
		file = path;
		lexer = new Lexer(path);
		tokens = new LinkedList<>();
	}
	
	private void parseerror(Token t, String msg) {
		System.out.println(file + ":" + t.line + "," + t.col + ": " + msg);
	}
	
	public void prepare() throws Exception {
		Token t;
		
		try {
			while ((t = lexer.lex()).type != TokenType.EOF)
				tokens.add(t);
			tokens.add(t);
		} catch (NullPointerException e) {
			parseerror(tokens.element(), "null token");
		}
	}
	
	public AST parse() {
		return null;
	}

}
