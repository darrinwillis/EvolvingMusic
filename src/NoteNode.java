import java.util.ArrayList;
import java.util.List;


public class NoteNode extends LeafNode {

	private Note n;
	
	//This default constructor is a random one
	public NoteNode() {
		super();
		this.n = Note.randomNote();
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
}
