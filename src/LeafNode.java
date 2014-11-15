import java.util.List;

public abstract class LeafNode extends Node{
	
	public LeafNode() {
		super();
	}

	public abstract List<MusicEvent> render(int time);
}
