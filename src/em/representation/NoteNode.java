package em.representation;

import java.util.ArrayList;
import java.util.List;

import em.representation.music.MusicEvent;
import em.representation.music.Note;

class NoteNode extends LeafNode {

	private Note n;

	// This default constructor is a random one
	protected NoteNode() {
		super();
		this.n = Note.randomNote();
	}

	protected NoteNode deepCopy()
	{
		NoteNode nn = (NoteNode) super.deepCopy();
		nn.n = this.n;
		return nn;
	}

	protected NoteNode(Note n) {
		super();
		this.n = n;
	}

	protected List<MusicEvent> render(int time)
	{
		List<MusicEvent> l = new ArrayList<MusicEvent>();
		l.add(new MusicEvent(n, time));
		return l;
	}

	@Override
	protected String toType()
	{
		return this.n.pitch.toString();
	}
}
