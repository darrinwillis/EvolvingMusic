import java.util.ArrayList;
import java.util.List;


public abstract class ParentNode extends Node {
	public List<Node> children;
	
	public ParentNode() {
		super();
		
		this.children = new ArrayList<Node>();
	}

}
