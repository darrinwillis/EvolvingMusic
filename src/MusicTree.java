import java.util.List;


public class MusicTree {
	private Node root;
	
	public static MusicTree RandomTree(int maxDepth)
	{
		assert maxDepth >= 1;
		MusicTree mt = new MusicTree();
		
		//Generate random nodes
		Node n = maxDepth > 1 ? RandomNodeGenerator.randomParentNode() :
			RandomNodeGenerator.randomLeafNode();
		fillNode(n, maxDepth - 1);
		mt.root = n;
		return mt;
	}
	
	private static void fillNode(Node n, int depth)
	{
		assert n.children.isEmpty();
		assert depth >= 0;
		int numChildren = n.arity();
		//fill up all children
		for (int i = 0; i < numChildren; i++)
		{
			assert depth > 0;
			Node child = (depth == 1) ? RandomNodeGenerator.randomLeafNode() :
				RandomNodeGenerator.randomParentNode();
			fillNode(child, depth - 1);
			n.children.add(child);
		}
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
