
import org.lightscript.rt.LSNumber;
import org.lightscript.rt.LSString;
import org.lightscript.rt.LSVector;

public class Main {

	public static void main(String[] args) {
		// Vectors tests
		
		LSNumber x = new LSNumber("1.1783e+3");
		System.out.println(x.hashString());
		
		LSVector vector = new LSVector(x.type(), 5);
		vector.set(3, x);
		System.out.println(vector);
		LSVector vector2 = new LSVector(vector.type(), 5);
		vector2.appendCopy(vector);
		vector.set(0, x);
		System.out.println(vector2);
		
		System.out.println();
		LSVector vector3 = (LSVector) vector2.get(5);
		vector3.set(0, x.toInteger().toDecimal());
		System.out.println(vector);
		System.out.println(vector2.get(5));
		
		// String tests
		
		LSString str = new LSString("Hey\nthere !");
		System.out.println(str.capacity());
		System.out.println(str.get(3));
		str.set(3, new LSNumber((long) 0x1b));
		System.out.println(str.printString());
	}

}
