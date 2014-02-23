package bradleyross.playpen;
import java.lang.Cloneable;
/**
 * Test of cloning operation.
 * @author Bradley Ross
 * @see Object#clone()
 * @see Cloneable
 *
 */
public class CloneTest {

	private class Dummy implements Cloneable{
		protected int id;
		public Dummy(int value) {
			id = value;
		}
		public Dummy() { ; }
		public int getId() {
			return id;
		}
		public void setId(int value) {
			id = value;
		}
		public Dummy clone() throws CloneNotSupportedException {
			Dummy newItem = new Dummy();
			newItem.setId(this.getId());
			return newItem;
		}
	}
	Dummy first = null;
	Dummy second = null;
	Dummy third = null;
	protected void display(String value) {
		System.out.println(value);
		if (first == null) {
			System.out.println("     first is null");
		} else {
			System.out.println("     first: " + Integer.toString(first.getId()));
		}
		if (second == null) {
			System.out.println("     second is null");
		} else {
			System.out.println("     second: " + Integer.toString(second.getId()));
		}
		if (third == null) {
			System.out.println("     third is null");
		} else {
			System.out.println("     third: " + Integer.toString(third.getId()));
		}
	}
	public void test() {
		try {
			display("starting condition");
			first = new Dummy(5);
			display("first created with value of 5");
			second =  first.clone();
			display("second created as clone of first");
			third = first;
			display("third set equal to first");
			second.setId(4);
			display("second changed to 4");
			first.setId(3);
			display("first changed to 3");
			third.setId(6);
			display("third changed to 6");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloneTest instance = new CloneTest();
		instance.test();

	}

}
