import java.util.Random;


public class Note {

	public Pitch pitch;
	
	public int duration;
	
	private static final Random rand = new Random();
	
	Note(Pitch pitch, int duration)
	{
		this.pitch = pitch;
		this.duration = duration;
	}
	
	public int getMidiPitch()
	{
		return pitch.getMidiPitch();
	}
	
	public static Note randomNote(long seed)
	{
		rand.setSeed(seed);
		Pitch p = Pitch.randomPitch(seed);
		int duration = rand.nextInt(4) + 1;
		return new Note(p, duration);
	}
}
