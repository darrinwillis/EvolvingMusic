import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Pitch {
	public enum PitchClass {
		C, C$, D, D$, E, F, F$, G, G$, A, A$, B;
		
		private static final Random RANDOM = new Random();
		private static final List<PitchClass> VALUES =
				Collections.unmodifiableList(Arrays.asList(values()));
		public static final int SIZE = VALUES.size();
		
		public static PitchClass randomPitchClass(long seed)
		{
			RANDOM.setSeed(seed);
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
	
	public int octave;
	
	public PitchClass pitchClass;
	
	private static final Random r = new Random();
	private static final int HIGH_OCTAVE = 8;
	private static final int LOW_OCTAVE = 2;
	
	
	public Pitch(PitchClass pc, int octave)
	{
		assert octave < HIGH_OCTAVE && octave >= LOW_OCTAVE;
		this.pitchClass = pc;
		this.octave = octave;
	}
	
	public int getMidiPitch()
	{
		return octave * (PitchClass.SIZE) + pitchClass.ordinal();
	}
	
	public static Pitch randomPitch(long seed)
	{
		r.setSeed(seed);
		PitchClass pc = PitchClass.randomPitchClass(r.nextInt());
		int octave = r.nextInt(HIGH_OCTAVE - LOW_OCTAVE) + LOW_OCTAVE;
		return new Pitch(pc, octave);
	}
}
