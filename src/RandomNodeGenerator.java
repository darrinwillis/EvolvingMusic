import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class RandomNodeGenerator {

	//Function classes
	private static final List<Class<? extends ParentNode>> pnClasses = new ArrayList<Class<? extends ParentNode>>(
			Arrays.asList(
					Combine.class,
					Follow.class
					)
			);
	private static final int numPNClasses = pnClasses.size();
	
	//Leaf classes
	private static final List<Class<? extends LeafNode>> leafClasses = new ArrayList<Class<? extends LeafNode>>(
			Arrays.asList(
					NoteNode.class
					)
			);
	private static final int numLeafClasses = leafClasses.size();
	
	private static final Random r = new Random();
	
	public static ParentNode randomParentNode(long seed)
	{
		r.setSeed(seed);
		Class<? extends ParentNode> pnclass = pnClasses.get(r.nextInt(numPNClasses));
		ParentNode newNode = null;
		try {
			newNode = pnclass.newInstance();
		} catch (Exception e) {
			System.out.println("Class " + pnclass + " is lacking a no arg constructor, or is abstract");
			e.printStackTrace();
		}
		assert newNode != null;
		return newNode;
	}
	
	public static LeafNode randomLeafNode(long seed)
	{
		r.setSeed(seed);
		Class<? extends LeafNode> leafClass = leafClasses.get(r.nextInt(numLeafClasses));
		LeafNode newNode = null;
		try {
			newNode = leafClass.newInstance();
		} catch (Exception e) {
			System.out.println("Class " + leafClass + " is lacking a no arg constructor, or is abstract");
			e.printStackTrace();
		}
		assert newNode != null;
		return newNode;
	}
}
