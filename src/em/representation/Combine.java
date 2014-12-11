package em.representation;

import java.util.ArrayList;
import java.util.List;

import em.representation.music.MusicEvent;

class Combine extends ParentNode {

	Combine() {
		super();
	}

	protected List<MusicEvent> render(int time)
	{
		List<MusicEvent> list = new ArrayList<MusicEvent>();

		for (Node eachChild : this.children)
		{
			list.addAll(eachChild.render(time));
		}
		return list;
	}

	protected int arity()
	{
		return 2;
	}

	@Override
	protected String toType()
	{
		return "COM";
	}

}
