package em.representation;

import java.util.ArrayList;

abstract class ParentNode extends Node {

	protected ParentNode() {
		super();

		this.children = new ArrayList<Node>();
	}

}
