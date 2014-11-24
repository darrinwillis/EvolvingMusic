import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Pitch {
	public enum PitchClass {
		C, C$, D, D$, E, F, F$, G, G$, A, A$, B;
		
		// All pitch classes
		private static final List<PitchClass> VALUES =
				Collections.unmodifiableList(Arrays.asList(values()));
		public static final int SIZE = VALUES.size();
		
		// All major diatonics
		public static final List<PitchClass> MAJORS = 
				new ArrayList<PitchClass>(Arrays.asList(
						C, D, E, F, G, A, B));
		public static final int MAJSIZE = MAJORS.size();
		
		// All minor diatonics
		public static final List<PitchClass> MINORS = 
				new ArrayList<PitchClass>(Arrays.asList(
						C, D, D$, F, G, G$, A$));
		public static final int MINSIZE = MINORS.size();
		
		
		// All pentatonics
		public static final List<PitchClass> PENTATONICS = 
				new ArrayList<PitchClass>(Arrays.asList(
						C, D, E, G, A));
		public static final int PSIZE = PENTATONICS.size();
		
		public static PitchClass randomPitchClass()
		{
			return VALUES.get(ThreadLocalRandom.current().nextInt(SIZE));
		}
		
		public static PitchClass randomPentatonic()
		{
			return PENTATONICS.get(ThreadLocalRandom.current().nextInt(PSIZE));
		}
		
		public static PitchClass randomMajor()
		{
			return MAJORS.get(ThreadLocalRandom.current().nextInt(MAJSIZE));
		}
		
		public static PitchClass randomMinor()
		{
			return MINORS.get(ThreadLocalRandom.current().nextInt(MINSIZE));
		}
	}
	
	public int octave;
	
	public PitchClass pitchClass;

	private static final int HIGH_OCTAVE = 8;
	private static final int LOW_OCTAVE = 2;
	
	
	public Pitch(PitchClass pc, int octave)
	{
		assert octave < HIGH_OCTAVE && octave >= LOW_OCTAVE;
		this.pitchClass = pc;
		this.octave = octave;
	}
	
	@Override
	public String toString() {
		return "" + pitchClass + octave;
	}

	public int getMidiPitch()
	{
		return octave * (PitchClass.SIZE) + pitchClass.ordinal();
	}
	
	public static Pitch randomPitch()
	{
		PitchClass pc = PitchClass.randomPitchClass();

		int octave = ThreadLocalRandom.current().nextInt(HIGH_OCTAVE - LOW_OCTAVE) + LOW_OCTAVE;
		return new Pitch(pc, octave);
	}
	
	public boolean isMajor()
	{
		return PitchClass.MAJORS.contains(pitchClass);
	}
	
	public boolean isMinor()
	{
		return PitchClass.MINORS.contains(pitchClass);
	}
	
	public boolean isPentatonic()
	{
		return PitchClass.PENTATONICS.contains(pitchClass);
	}
}
