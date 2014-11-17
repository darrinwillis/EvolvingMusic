import java.util.ArrayList;

public abstract class ParentNode extends Node {
	
	public ParentNode() {
		super();
		
		this.children = new ArrayList<Node>();
	}

}
