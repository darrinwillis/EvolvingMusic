import java.util.ArrayList;
import java.util.List;

public class LeafNode<T> extends Node<T>{
	
	public LeafNode(T data) {
		super(data);
	}

	public List<T> render() {
		List<T> l = new ArrayList<T>();
		l.add(this.data);
		return l;
	}
	
	

}
