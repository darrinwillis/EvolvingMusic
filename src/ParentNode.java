import java.util.List;


public abstract class ParentNode<T> extends Node<T> {
	public List<Node<T>> children;
	
	public ParentNode(T data) {
		super(data);
	}

}
