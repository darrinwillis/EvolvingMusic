import java.util.List;


public class Follow extends ParentNode {

	public int arity() {
		return 2;
	}

	public List<MusicEvent> render(int time) {
		assert this.children.size() == 2;
		Node firstChild = this.children.get(0);
		Node secondChild = this.children.get(1);
		List<MusicEvent> totalEvents = firstChild.render(time);
		int endingTime = getEndingTime(totalEvents);
		totalEvents.addAll(secondChild.render(endingTime + 1));
		return totalEvents;
	}
	
	private int getEndingTime(List<MusicEvent> list)
	{
		int lastTime = 0;
		for (MusicEvent me : list)
		{
			lastTime = Integer.max(me.note.duration + me.time, lastTime);
		}
		return lastTime;
	}

}
