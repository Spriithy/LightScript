import org.lightscript.compiler.Lexer;
import org.lightscript.compiler.Token;
import org.lightscript.compiler.TokenType;

public class Main {

	public static void main(String[] args) {
		try {
			Lexer lexer = new Lexer("test/source.ls");
			
			Token t;
			
			while ((t = lexer.lex()).type != TokenType.EOF)
				System.out.println(t.toString());
			System.out.println(t.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
