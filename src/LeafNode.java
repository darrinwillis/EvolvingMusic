import java.util.List;

public abstract class LeafNode extends Node{
	
	public LeafNode() {
		super();
	}
	
	public int arity() {
		return 0;
	}

	public abstract List<MusicEvent> render(int time);
}
