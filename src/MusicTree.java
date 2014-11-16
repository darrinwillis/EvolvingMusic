import java.util.List;
import java.util.Random;


public class MusicTree {
	private Node root;
	private static final Random r = new Random();
	
	public static MusicTree RandomTree(long seed, int maxDepth)
	{
		r.setSeed(seed);
		MusicTree mt = new MusicTree();
		
		//Generate random nodes
		
		return mt;
	}
	
	public void mutate(double prob)
	{
		return;
	}
	
	public void crossOver(MusicTree mt)
	{
		return;
	}
	
	public List<MusicEvent> render()
	{
		return root.render(0);
	}
}
