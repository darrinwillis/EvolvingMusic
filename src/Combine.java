import java.util.ArrayList;
import java.util.List;


public class Combine extends ParentNode<MusicEvent> {

	public Combine(MusicEvent data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	public List<MusicEvent> render() {
		List<MusicEvent> list = new ArrayList<MusicEvent>();
		
		for (Node<MusicEvent> eachChild : this.children)
		{
			list.addAll(eachChild.render());
		}
		return list;
	}

}
