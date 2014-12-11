package em.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import em.representation.music.MusicEvent;

abstract class Node {
	protected Node parent;
	protected List<Node> children;
	private Integer size;

	// This is used in mutation, it directly replaced a node
	protected void replaceWith(Node n)
	{
		Node p = this.parent;
		// Remove this child
		p.children.remove(this);
		p.notifyChange();
		// Add the new child
		p.children.add(n);
		// Add the new parent
		n.parent = p;
	}

	@SuppressWarnings("unchecked")
	protected Node deepCopy()
	{
		Node copy = null;
		try
		{
			copy = this.getClass().newInstance();
			copy.size = this.size;
			copy.parent = this.parent;
			copy.children = this.children.getClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
			assert false;
			return null;
		}

		for (Node n : this.children)
		{
			Node child = n.deepCopy();
			child.parent = copy;
			copy.children.add(child);
		}

		return copy;
	}

	protected static void swapParents(Node n1, Node n2)
	{
		assert n1.parent != null && n2.parent != null;
		assert n1.getRoot() != n2.getRoot();

		Node oldN1Parent = n1.parent;
		Node oldN2Parent = n2.parent;

		// TODO: It might become pertinent to have the children be ordered at
		// some point, in which case
		// indexOf would need to be used to assure that the place in the list is
		// maintained
		// Remove n1 from its parent's children
		oldN1Parent.children.remove(n1);
		oldN1Parent.notifyChange();
		// Remove n2 from its parent's children
		oldN2Parent.children.remove(n2);
		oldN2Parent.notifyChange();
		// swap the children of n1 and n2's parents
		oldN1Parent.children.add(n2);
		oldN2Parent.children.add(n1);
		// Swap the parents of n1 and n2
		n2.parent = oldN1Parent;
		n1.parent = oldN2Parent;
	}

	protected abstract int arity();

	protected Node getRoot()
	{
		if (this.parent == null)
		{
			return this;
		}
		return this.parent.getRoot();
	}

	protected int getSize()
	{
		if (this.size == null)
		{
			if (this.arity() == 0)
			{
				size = 1;
			} else
			{
				size = this.children.stream().mapToInt(Node::getSize).sum() + 1;
			}
		}
		return size;
	}

	protected void notifyChange()
	{
		Node n = this;
		while (n != null)
		{
			n.size = null;
			n = n.parent;
		}
	}

	protected Node randomSubNode()
	{
		List<Node> list = this.toList();
		// We want a strict SUB node, not including this node
		list.remove(this);
		int size = list.size();
		int index = ThreadLocalRandom.current().nextInt(size);
		Node n = list.get(index);
		return n;
	}

	protected List<Node> toList()
	{
		// TODO: Maybe make this into a cached list instead?
		List<Node> allChildren = new ArrayList<Node>();
		allChildren.add(this);
		assert this.children != null;
		// for (Node c : children)
		// {
		// allChildren.addAll(c.toList());
		// }
		this.children.stream().forEach((n) -> allChildren.addAll(n.toList()));
		return allChildren;
	}

	protected abstract String toType();

	@Override
	public String toString()
	{
		String treeString = this.toType();
		treeString += String.format("%n");

		List<Node> currentLevel = new ArrayList<Node>();
		currentLevel.addAll(this.children);

		while (currentLevel.size() != 0)
		{
			List<Node> nextLevel = new ArrayList<Node>();
			for (Node n : currentLevel)
			{
				treeString += n.toType() + " ";
				nextLevel.addAll(n.children);
			}
			treeString += String.format("%n");
			currentLevel = nextLevel;
		}
		return treeString;
	}

	protected abstract List<MusicEvent> render(int time);
}
