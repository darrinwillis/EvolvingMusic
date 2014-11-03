
public class Pitch {
	public enum PitchClass {
		C, C$, D, D$, E, F, F$, G, G$, A, A$, B;
	}
	
	public int octave;
	
	public PitchClass pitchClass;
	
	public Pitch(PitchClass pc, int octave)
	{
		assert octave < 8 && octave > 2;
		this.pitchClass = pc;
		this.octave = octave;
	}
}
