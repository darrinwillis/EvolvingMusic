import java.util.ArrayList;
import java.util.List;

public abstract class LeafNode extends Node{
	
	public LeafNode() {
		super();
		this.children = new ArrayList<Node>();
	}
	
	public int arity() {
		return 0;
	}

	public abstract List<MusicEvent> render(int time);
}
