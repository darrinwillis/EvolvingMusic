import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Fitness {
	public static class Report{
		double size;
		double keyScore;
		double rhythymScore;
		double chordScore;
		double leapScore;
		double progressionScore;
		double lengthScore;
	}
	
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
		double leapScore = evaluateLeaps(mt);
		double progressionScore = evaluateProgression(mt);
		score += keyScore/2;
		score += rhythymScore;
		score += chordScore;
		score += leapScore;
		score += progressionScore * 3;
		//Progression score
		score -= Math.min(Math.exp(Math.abs(getLength(mt) - idealLength) / 2), score);
		return score;
	}
	
	public static Report getReport(MusicTree mt)
	{
		Report fitRep = new Report();
		
		fitRep.size = mt.root.getSize();
		fitRep.keyScore = evaluateKey(mt)/2;
		fitRep.rhythymScore = evaluateRhythm(mt);
		fitRep.chordScore = evaluateChords(mt);
		fitRep.leapScore = evaluateLeaps(mt);
		fitRep.progressionScore = evaluateProgression(mt);
		fitRep.lengthScore = Math.exp(Math.abs(getLength(mt) - idealLength) / 2);
		return fitRep;
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
		long numPents = events
				.stream()
				.filter((me) -> me.note.pitch.isPentatonic())
				.count();
		
		//keynum is the number of notes in the key which is closest to this collection of notes
		long keyNum = numMajors + numPents;
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
		double numOnBeats =
				2 * eventsPerBeat.getOrDefault(0, 0) +
				eventsPerBeat.getOrDefault(4, 0) + 
				1.5 * eventsPerBeat.getOrDefault(8, 0) + 
				eventsPerBeat.getOrDefault(12, 0);

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
	
	private static double evaluateProgression(MusicTree mt)
	{
		Map<Integer, List<MusicEvent>> timedEvents = getChords(mt);
		List<Pitch.Chord> chords = timedEvents.values()
				.stream()
				.map(l -> l
						.stream()
						.map(me -> me.note.pitch)
						.collect(Collectors.toList()))
				.map(l -> Pitch.Chord.getBestChord(l))
				.collect(Collectors.toList());
		
		int matchedChords = 0;
		Pitch.Chord lastChord = Pitch.Chord.NC;
		for (Pitch.Chord c : chords)
		{
			if (Pitch.Chord.isCanonical(lastChord, c))
			{
				matchedChords++;
			}
			lastChord = c;
		}
		
		return matchedChords;
	}
	
	private static double evaluateLeaps(MusicTree mt)
	{
		Map<Integer, List<MusicEvent>> timedEvents = getChords(mt);
		Iterator<List<MusicEvent>> iter = timedEvents.values().iterator();
		
		if (!iter.hasNext())
		{
			return 0;
		}
		
		int numLeaps = 0;
		
		int lastLow = 0;
		int lastHigh = 0;
		while (iter.hasNext())
		{
			List<MusicEvent> events = iter.next();
			int currentLow = 0;
			int currentHigh = 0;
			for (MusicEvent me : events)
			{
				int midiPitch = me.note.getMidiPitch();
				currentLow = Math.min(midiPitch, lastLow);
				currentHigh = Math.max(midiPitch, lastHigh);
			}
			
			if (currentLow <= lastLow - Pitch.PERFECT_FOURTH)
			{
				numLeaps++;
			}
			
			if (currentHigh >= lastHigh + Pitch.PERFECT_FOURTH)
			{
				numLeaps++;
			}
			lastLow = currentLow;
			lastHigh = currentHigh;
		}
		return timedEvents.size() - numLeaps;
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
