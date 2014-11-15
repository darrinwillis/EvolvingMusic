import java.util.ArrayList;
import java.util.List;


public class Combine extends ParentNode {

	public Combine(MusicEvent data) {
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

}
