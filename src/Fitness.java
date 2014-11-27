import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Fitness {
	//4 sixteenths per beat, 4 beats per bar, 2 bars
	private static int idealLength = 4 * 4 * 3;
	public static double calcFitness(MusicTree mt)
	{
		double score = 0;
		if (mt.root.getSize() > MusicTree.maxSize)
		{
			return score;
		}
		
		double keyScore = evaluateKey(mt);
		double rhythymScore = evaluateRhythm(mt);
		double chordScore = evaluateChords(mt);
		score += keyScore;
		score += rhythymScore;
		score += chordScore;
		score -= Math.min(Math.exp(Math.abs(getLength(mt) - idealLength) / 2), score);
		return score;
	}
	
	//Returns a value between 0 and 1 which represents how closely this
	//tree represents a key
	private static double evaluateKey(MusicTree mt)
	{
		List<MusicEvent> events = mt.getRender();
		long numMajors = events
				.stream()
				.filter((me) -> me.note.pitch.isMajor())
				.count();
		long numMinors = events
				.stream()
				.filter((me) -> me.note.pitch.isMinor())
				.count();
		long numPitches = events.size();
		
		//keynum is the number of notes in the key which is closest to this collection of notes
		long keyNum = Long.max(numMajors, numMinors);
		return (double)keyNum;
	}
	
	//Returns a value between 
	private static double evaluateRhythm(MusicTree mt)
	{
		List<MusicEvent> events = mt.getRender();
		int numEvents = events.size();
		Map<Integer, Integer> eventsPerBeat = events
				.stream()
				.collect(
						Collectors.groupingBy(
								me -> me.time % 16,
								Collectors.summingInt(me -> 1)));
		assert eventsPerBeat.size() <= 16;
		assert eventsPerBeat.values()
			.stream()
			.mapToInt(Integer::intValue)
			.sum() == numEvents;
		int numDownBeats = eventsPerBeat.getOrDefault(0, 0);
		int numOnBeats = numDownBeats + eventsPerBeat.getOrDefault(4, 0) + eventsPerBeat.getOrDefault(8, 0) + eventsPerBeat.getOrDefault(12, 0);
		double avgNotesPerBeat = (double)numOnBeats / 4;

//		return (double)(numDownBeats + beatStrength) / (double)numEvents;
		return numOnBeats;
	}
	
	private static double evaluateChords(MusicTree mt)
	{
		Map<Integer, List<MusicEvent>> timedEvents = getChords(mt);
		
		int numChords = 0;
		for (List<MusicEvent> events : timedEvents.values())
		{
			Set<Pitch> pitches = events
					.stream()
					.map(me -> me.note.pitch)
					.distinct()
					.collect(Collectors.toSet());
			if (Pitch.isDiatonic(pitches))
			{
				numChords++;
			}
		}
		return (double)numChords;
	}
	
	private static Map<Integer, List<MusicEvent>> getChords(MusicTree mt)
	{
		List<MusicEvent> events = mt.getRender();
		Map<Integer, List<MusicEvent>> timedEvents = events
			.stream()
			.collect(
					Collectors.groupingBy(
							me -> me.time));
		return timedEvents;
	}
	
	private static int getLength(MusicTree mt)
	{
		int lastTime = 0;
		for (MusicEvent me : mt.getRender())
		{
			lastTime = Integer.max(me.note.duration + me.time, lastTime);
		}
		return lastTime;
	}
}
