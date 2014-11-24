import java.util.ArrayList;
import java.util.List;


public class NoteNode extends LeafNode {

	private Note n;
	
	//This default constructor is a random one
	public NoteNode() {
		super();
		this.n = Note.randomNote();
	}
	
	public NoteNode deepCopy()
	{
		NoteNode nn = (NoteNode) super.deepCopy();
		nn.n = this.n;
		return nn;
	}
	
	public NoteNode(Note n) {
		super();
		this.n = n;
	}

	public List<MusicEvent> render(int time) {
		List<MusicEvent> l = new ArrayList<MusicEvent>();
		l.add(new MusicEvent(n, time));
		return l;
	}

	@Override
	String toType() {
		return this.n.pitch.toString();
	}
}
