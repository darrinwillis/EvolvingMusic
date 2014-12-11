package em.representation.music;

public class MusicEvent {
	public Note note;
	public int time;

	public MusicEvent(Note n, int t) {
		this.note = n;
		this.time = t;
	}
}
