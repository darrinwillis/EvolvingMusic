import java.util.List;


public abstract class Node {
	public ParentNode parent;
	
	public void removeFromParent() {
		this.parent.children.remove(this);
	}
	
	public abstract int arity();
	
	public abstract List<MusicEvent> render(int time);
}
