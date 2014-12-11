package em.representation;

import java.util.ArrayList;
import java.util.List;

import em.representation.music.MusicEvent;

abstract class LeafNode extends Node {

	protected LeafNode() {
		super();
		this.children = new ArrayList<Node>();
	}

	protected int arity()
	{
		return 0;
	}

	protected abstract List<MusicEvent> render(int time);
}
