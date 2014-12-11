package em.representation;

import java.util.List;

import em.representation.music.MusicEvent;

public class MusicTree {
	private double parentNodeProb = .2;
	private int newParentDepth = 3;
	protected static int maxSize = 160;

	protected Node root;
	private List<MusicEvent> renderedEvents = null;
	private Double fitness = null;

	public static MusicTree RandomTree(int maxDepth)
	{
		assert maxDepth >= 1;
		MusicTree mt = new MusicTree();

		// Generate random nodes
		Node n = maxDepth > 1 ? RandomNodeGenerator.randomParentNode()
				: RandomNodeGenerator.randomLeafNode();
		fillNode(n, maxDepth - 1);
		mt.root = n;
		return mt;
	}

	public MusicTree deepCopy()
	{
		MusicTree copy = new MusicTree();
		copy.root = this.root.deepCopy();
		copy.renderedEvents = null;
		copy.fitness = this.fitness;
		copy.parentNodeProb = this.parentNodeProb;
		copy.newParentDepth = this.newParentDepth;
		assert copy.root != this.root;
		return copy;
	}

	private static void fillNode(Node n, int depth)
	{
		assert n.children.isEmpty();
		assert depth >= 0;
		int numChildren = n.arity();
		// fill up all children
		for (int i = 0; i < numChildren; i++)
		{
			assert depth > 0;
			Node child = (depth == 1) ? RandomNodeGenerator.randomLeafNode()
					: RandomNodeGenerator.randomParentNode();
			fillNode(child, depth - 1);
			n.children.add(child);
			child.parent = (ParentNode) n;
		}
	}

	public void mutate()
	{
		Node n = root.randomSubNode();
		if (n instanceof ParentNode)
		{
			Node newNode = RandomNodeGenerator.randomParentNode();
			newNode.children = n.children;
			n.replaceWith(newNode);
		} else
		{
			assert n instanceof NoteNode;
			NoteNode newNote = new NoteNode();
			n.replaceWith(newNote);
		}
		return;
	}

	public static void crossOver(MusicTree mt1, MusicTree mt2)
	{
		assert mt1.root != null && mt2.root != null;
		assert mt1 != mt2;
		assert mt1.root != mt2.root;
		assert mt1.root.parent == null && mt2.root.parent == null;
		Node n1 = mt1.root.randomSubNode();
		Node n2 = mt2.root.randomSubNode();

		Node.swapParents(n1, n2);

		mt1.fitness = null;
		mt2.fitness = null;

		return;
	}

	public List<MusicEvent> getRender()
	{
		if (renderedEvents == null)
		{
			renderedEvents = render();
		}
		return renderedEvents;
	}

	private List<MusicEvent> render()
	{
		return root.render(0);
	}

	public Double getFitness()
	{
		if (fitness == null)
		{
			fitness = Fitness.calcFitness(this);
		}
		return fitness;
	}

	@SuppressWarnings("unused")
	private double scoreHighNotes()
	{
		List<MusicEvent> events = getRender();

		return events.stream().mapToDouble(me -> me.note.pitch.getMidiPitch())
				.sum();
	}

	public String toString()
	{
		return this.root.toString();
	}
}
