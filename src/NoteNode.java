import java.util.ArrayList;
import java.util.List;


public class NoteNode extends LeafNode {

	Note n;
	
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
