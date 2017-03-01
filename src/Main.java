import org.lightscript.rt.LSNumber;
import org.lightscript.rt.LSVector;

public class Main {

	public static void main(String[] args) {
		LSNumber x = new LSNumber("1e+10");
		LSVector vector = new LSVector(x.type(), 5);
		vector.set(3, x);
		System.out.println(vector);
		LSVector vector2 = new LSVector(vector.type(), 5);
		vector2.append(vector);
		vector.set(0, x);
		System.out.println(vector2);
	}

}
