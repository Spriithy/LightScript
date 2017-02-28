package org.lightscript.compiler;

import static org.lightscript.compiler.TokenType.*;

import java.io.FileReader;

public class Lexer {

	private FileReader	in;
	private String		file;
	private int			line, col;

	private char ch;

	public Lexer(String path) throws Exception {
		in = new FileReader(path);
		file = path;
		line = 1;
		col = 0;
		next();
	}

	private void lexerror(String msg) {
		System.err.println(file + ":" + line + "," + col + ": " + msg);
		System.exit(1);
	}
	
	private boolean next() throws Exception {
		col++;
		return (ch = (char) in.read()) == (char) -1 ? false : true;
	}

	public Token lex() throws Exception {
		int start = col;
		switch (ch) {
		case (char) -1:
			return new Token(EOF, line, start);
		case '\n':
		case '\r':
			line++;
			col = 0;
			next();
			return new Token(EOL, line - 1, start);
		case '#': next(); return new Token(LEN, line, start);
		case '|': return follows2('|', OOR, '=', OR_EQ, OR);
		case '&': return follows2('&', AAND, '=', AND_EQ, AND);
		case '^': return follows1('=', XOR_EQ, XOR);
		case '!': return follows1('=', BANG_EQ, BANG);
		case '~': return follows1('=', TILDA_EQ, TILDA);
		case '-': return follows2('-', MMINUS, '=', MINUS_EQ, MINUS);
		case '+': return follows2('+', PPLUS, '=', PLUS_EQ, PLUS);
		case '*': return follows2('*', TTIMES, '=', TIMES_EQ, TIMES);
		case '%': return follows1('=', MOD_EQ, MOD);
		case '>': return follows2('>', R_SFT, '=', GTE, GT);
		case '<': return follows2('<', L_SFT, '=', LTE, LT);
		case '=': return follows1('=', EQUALS, ASSIGN);
		case ':': return follows1(':', BLOCK, COLON);
		case '(': next(); return new Token(L_PAREN, line, start);
		case ')': next(); return new Token(R_PAREN, line, start);
		case '[': next(); return new Token(L_BRAKT, line, start);
		case ']': next(); return new Token(R_BRAKT, line, start);
		case '{': next(); return new Token(L_CURLY, line, start);
		case '}': next(); return new Token(R_CURLY, line, start);
		case ',': next(); return new Token(COMMA, line, start);
		case ';': next(); return new Token(SEMI_COLON, line, start);
		case '.':
			if (next() && ch == '.')
					if (next() && ch == '.')
						return new Token(ELLIPSIS, line, start);
					else lexerror("unexpected character after '..'");
			else if (Character.isDigit(ch))
				return number(true);
			else next();
			return new Token(DOT, line, start);
		case '/':
			if (next()) {
				switch (ch) {
				case '/':
					while (ch != '\n' && next());
					col = 1;
					return lex();
				case '=': next(); return new Token(DIV_EQ, line, start);
				}
			}
			return new Token(DIVIDE, line, start);
		default:
			if (Character.isWhitespace(ch)) {
				next();
				return lex();
			}
			
			if (Character.isJavaIdentifierStart(ch))
				return name();
			if (Character.isDigit(ch))
				return number(false);
			if (checkIn("'\""))
				return string();
		}
		
		lexerror("unexpected lexer state (meeting '" + ch + "')");
		return null;
	}
	
	private Token string() throws Exception {
		String str = new String();
		char del = ch;
		int nesc = 0;
		
		next();
		while (true) {
			switch (ch) {
			case '\'': case '"':
				if (ch == del) {
					next();
					return new Token(str, STRING_LIT, line, col - nesc - str.length() - 2);
				}
				str += ch;
				break;
			case '\\':
				nesc++;
				next();
				switch (ch) {
				case '\'': case '\\': case '"':
					break;
				case '0': ch = '\0'; break;
				case 'a': ch = '\u0007'; break;
				case 'v': ch = '\u000B'; break;
				case 'b': ch = '\b'; break;
				case 'f': ch = '\f'; break;
				case 'n': ch = '\n'; break;
				case 't': ch = '\t'; break;
				case 'x': ch = hexEscape(); nesc++; break;
				case 'e': ch = 0x1b; break;
				case '?': ch = 0x3f; break;
				case 'u': case 'U':
					lexerror("unicode escape sequences not supported yet in string literals");
				case (char) -1:
					lexerror("unexpected end of file in string literal (within escape sequence)");
				case '\n': case '\r':
					lexerror("unterminated escape sequence in string literal");
				default:
					lexerror("unexpected escape character '\\" + ch + "' in string literal");
				}
				str += ch;
				break;
			case (char) -1:
				lexerror("unexpected end of file in string literal");
			case '\n': case '\r':
				lexerror("unterminated string literal");
			default:
				str += ch;
				break;
			}
			
			if (!next()) lexerror("unexpected end of input in string literal");
		}
	}
	
	private Token number(boolean dec) throws Exception {
		String num = new String() + (dec ? "." : (ch == '0' ? ch : ""));
		boolean exp = false;

		// Check Hexadecimal inputs
		if (ch == '0' && !dec) {
			next();
			if (checkIn("xX")) {
				while (next() && isXDigit(ch))
					num += ch;
				
				if (num.length() == 0)
					lexerror("expected hexadecimal digits after '0x' prefix");
				
				try {
					return new Token(Long.decode("0x" + num), INTEGER, line, col - 1 - num.length());
				} catch (NumberFormatException e) {
					lexerror("invalid hexadecimal input '0x" + num + "'");
				}
			}
			
			if (!checkIn("0123456789.")) return new Token(0, INTEGER, line, col - 1);
		}
		
		num += ch;
		
		if (ch == '.') dec = true;
		
		while (next() && checkIn("0123456789.eE")) {
			if (ch == '.')
				if (dec)
					lexerror("unexpected '.' in decimal litteral (" + num + ")"); 
				else dec = true;
			
			if (checkIn("eE")) {
				if (exp) lexerror("unexpected '" + ch + "' in decimal litteral (" + num + ")");
				num += ch;
				next();
				if (checkIn("+-"))
					num += ch;
				exp = true;
				dec = true;
				continue;
			}
			num += ch;
		}
		
		if (!checkIn("0123456789.", num.charAt(num.length() - 1)))
			lexerror("malformed numeric litteral '" + num + "' (unexpected '" + ch + "')");
		
		try {
			if (dec)
				return new Token(Double.valueOf(num), DECIMAL, line, col - num.length());		
			return new Token(Long.valueOf(num), INTEGER, line, col - num.length());
		} catch (NumberFormatException e) {
			lexerror("'" + (dec ? Double.valueOf(num) : Long.decode(num)) + "' overflows builtin integer type (64 bits)");
		}
		
		return null;
	}
	
	private Token name() throws Exception {
		String text = new String();
		
		while (Character.isJavaIdentifierPart(ch)) {
			text += ch;
			next();
		}
		
		return KeywordOrIdentifier(text, line, col - text.length());
	}
	
	private Token follows1(char c1, TokenType t1, TokenType t2) throws Exception {
		if (next())
			if (ch == c1) {
				next();
				return new Token(t1, line, col - 2);
			}
		return new Token(t2, line, col - 1);
	}
	
	private Token follows2(char c1, TokenType t1, char c2, TokenType t2, TokenType t3) throws Exception {
		if (next()) {
			if (ch == c1) {
				next();
				return new Token(t1, line, col - 2);
			}
			
			if (ch == c2) {
				next();
				return new Token(t2, line, col - 2);
			}
		}
		return new Token(t3, line, col - 1);
	}
	
	private char hexEscape() throws Exception {
		char hi, lo;
		next();
		hi = ch;
		next();
		lo = ch;
		if (hi == (char)-1 || lo == (char)-1)
			lexerror("unexpected end of file in hexadecimal escape sequence");
		if (hi == '\n' || lo == '\n' || hi == '\r' || lo == '\r')
			lexerror("unexpected line feed in hexadecimal escape sequence");
		if (!isXDigit(hi) || !isXDigit(lo))
			lexerror("unexpected character in hexadecimal escape sequence '\\x" + hi + "" + lo + "'");
		return (char) ((hexValueOf(hi) << 4) | hexValueOf(lo));
	}

	private boolean checkIn(String s) {
		return checkIn(s, ch);
	}
	
	static boolean checkIn(String s, char ch) {
		return s.indexOf(ch) >= 0;
	}

	static int hexValueOf(char ch) {
		if (!isXDigit(ch)) return -1;
		return ch <= '9'? ch - '0': ch <= 'F'? ch + 10 - 'A': ch + 10 - 'a';
	}

	static boolean isXDigit(char ch) {
		return Character.isDigit(ch) || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
	}

}
