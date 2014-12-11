package em.representation.music;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


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
		
		// Definitions of all chords
		public static final ArrayList<Set<PitchClass>> DIATONICS =
			new ArrayList<Set<PitchClass>>(
				Arrays.asList(
					new HashSet<PitchClass>(Arrays.asList(
							C,E,G,B)),
					new HashSet<PitchClass>(Arrays.asList(
							D,F,A)),
					new HashSet<PitchClass>(Arrays.asList(
							E,G,B)),
					new HashSet<PitchClass>(Arrays.asList(
							F,A,C)),
					new HashSet<PitchClass>(Arrays.asList(
							G,B,D,F)),
					new HashSet<PitchClass>(Arrays.asList(
							A,C,E)),	
					new HashSet<PitchClass>(Arrays.asList(
							B,D,F))));
		
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
	
	public enum Chord {
		I, ii, iii, IV, V, vi, vii, NC;
		
		public static Chord getBestChord(List<Pitch> pitches)
		{
			Chord bestChord = NC;
			int bestScore = Integer.MIN_VALUE;
			for (Chord c : Chord.values())
			{
				int score = chordMatch(c, pitches);
				if (score > bestScore)
				{
					bestChord = c;
					bestScore = score;
				}
			}
			return bestChord;
		}
		
		public static int chordMatch(Chord c, List<Pitch> pitches)
		{
			//NumMatched starts at 0, and incrememnts by 1 for every match, and
			//decrements by 1 for every not match
			if (c == NC)
				return 0;
			int numMatched = 0;
			int chordIndex = c.ordinal();
			Set<PitchClass> chordPitches = PitchClass.DIATONICS.get(chordIndex);
			for (Pitch p : pitches)
			{
				numMatched += chordPitches.contains(p.pitchClass) ? 1 : -1;
			}
			return numMatched;
		}
		
		public static boolean isCanonical(Chord a, Chord b)
		{
			switch(a){
			case I:
				return true;
			case ii:
				switch(b){
				case ii:
				case IV:
				case V:
					return true;
				default:
					return false;
				}
			case iii:
				switch(b){
				case iii:
				case IV:
				case V:
				case vi:
					return true;
				default:
					return false;
				}
			case IV:
				switch(b){
				case I:
				case ii:
				case IV:
				case V:
					return true;
				default:
					return false;
				}
			case V:
				switch(b){
				case I:
				case V:
					return true;
				default:
					return false;
				}
			case vi:
				switch(b){
				case ii:
				case IV:
				case V:
				case vi:
					return true;
				default:
					return false;
				}
			case vii:
				switch(b){
				case I:
					return true;
				default:
					return false;
				}
			case NC:
				switch(b){
				case I:
				case V:
					return true;
				default:
					return false;
				}
			default:
				return false;
			}
		}
	}
	
	public int octave;
	
	public PitchClass pitchClass;

	public static final int PERFECT_FOURTH = 5;
	
	private static final int HIGH_OCTAVE = 8;
	private static final int LOW_OCTAVE = 2;
	
	public static boolean isDiatonic(Set<Pitch> pitches)
	{
		Set<PitchClass> classes = (Set<PitchClass>) pitches
			.stream()
			.map(p -> p.pitchClass)
			.collect(Collectors.toSet());
		for (Set<PitchClass> chord : PitchClass.DIATONICS)
		{
			if (chord.containsAll(classes))
			{
				return true;
			}
		}
		return false;
	}
	
	public Pitch(PitchClass pc, int octave)
	{
		assert octave < HIGH_OCTAVE && octave >= LOW_OCTAVE;
		this.pitchClass = pc;
		this.octave = octave;
	}
	
	public static Pitch randomPitch()
	{
		PitchClass pc = PitchClass.randomPitchClass();

		int octave = ThreadLocalRandom.current().nextInt(HIGH_OCTAVE - LOW_OCTAVE) + LOW_OCTAVE;
		return new Pitch(pc, octave);
	}
	
	@Override
	public String toString() {
		return "" + pitchClass + octave;
	}

	public int getMidiPitch()
	{
		return octave * (PitchClass.SIZE) + pitchClass.ordinal();
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
