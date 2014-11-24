import java.util.ArrayList;
import java.util.List;


public class Combine extends ParentNode {

	public Combine() {
		super();
	}

	public List<MusicEvent> render(int time) {
		List<MusicEvent> list = new ArrayList<MusicEvent>();
		
		for (Node eachChild : this.children)
		{
			list.addAll(eachChild.render(time));
		}
		return list;
	}
	
	public int arity() {
		return 2;
	}

	@Override
	String toType() {
		return "COM";
	}

}
