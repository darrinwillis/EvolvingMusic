import java.util.List;


public abstract class Node<T> {
	public T data;
	public ParentNode<T> parent;
	
	public Node(T data) {
		this.data = data;
	}
	
	public void removeFromParent() {
		this.parent.children.remove(this);
	}
	
	public abstract List<T> render();
}
