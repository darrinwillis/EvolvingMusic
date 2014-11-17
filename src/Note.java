import java.util.concurrent.ThreadLocalRandom;


public class Note {

	public Pitch pitch;
	
	public int duration;
	
	Note(Pitch pitch, int duration)
	{
		this.pitch = pitch;
		this.duration = duration;
	}
	
	public int getMidiPitch()
	{
		return pitch.getMidiPitch();
	}
	
	public static Note randomNote()
	{
		Pitch p = Pitch.randomPitch();
		int duration = ThreadLocalRandom.current().nextInt(4) + 1;
		return new Note(p, duration);
	}
}
