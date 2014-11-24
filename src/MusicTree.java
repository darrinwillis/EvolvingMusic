import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


public class MusicTree {
	private double parentNodeProb = .2;
	private int newParentDepth = 3;
	private int maxSize = 80;
	
	private Node root;
	private List<MusicEvent> renderedEvents = null;
	private Double fitness = null;
	
	public static MusicTree RandomTree(int maxDepth)
	{
		assert maxDepth >= 1;
		MusicTree mt = new MusicTree();
		
		//Generate random nodes
		Node n = maxDepth > 1 ? RandomNodeGenerator.randomParentNode() :
			RandomNodeGenerator.randomLeafNode();
		fillNode(n, maxDepth - 1);
		mt.root = n;
		return mt;
	}
	
	public MusicTree deepCopy()
	{
		MusicTree copy = new MusicTree();
		copy.root = this.root.deepCopy();
		copy.renderedEvents = null;
		copy.fitness = this.fitness;
		copy.parentNodeProb = this.parentNodeProb;
		copy.newParentDepth = this.newParentDepth;
		assert copy.root != this.root;
		return copy;
	}
	
	private static void fillNode(Node n, int depth)
	{
		assert n.children.isEmpty();
		assert depth >= 0;
		int numChildren = n.arity();
		//fill up all children
		for (int i = 0; i < numChildren; i++)
		{
			assert depth > 0;
			Node child = (depth == 1) ? RandomNodeGenerator.randomLeafNode() :
				RandomNodeGenerator.randomParentNode();
			fillNode(child, depth - 1);
			n.children.add(child);
			child.parent = (ParentNode)n;
		}
	}
	
	public void mutate()
	{
		Node n = root.randomSubNode();
		Node randomNew;
		if (ThreadLocalRandom.current().nextDouble() <= this.parentNodeProb)
		{
			randomNew = RandomNodeGenerator.randomParentNode();
			fillNode(randomNew, newParentDepth);
		} else {
			randomNew = RandomNodeGenerator.randomLeafNode();
		}
		n.replaceWith(randomNew);
		return;
	}
	
	public static void crossOver(MusicTree mt1, MusicTree mt2)
	{
		assert mt1.root != null && mt2.root != null;
		assert mt1 != mt2;
		assert mt1.root != mt2.root;
		assert mt1.root.parent == null && mt2.root.parent == null;
		Node n1 = mt1.root.randomSubNode();
		Node n2 = mt2.root.randomSubNode();
		
		Node.swapParents(n1, n2);
		
		mt1.fitness = null;
		mt2.fitness = null;
		
		return;
	}
	
	public List<MusicEvent> getRender()
	{
		if (renderedEvents == null)
		{
			renderedEvents = render();
		}
		return renderedEvents;
	}
	
	private List<MusicEvent> render()
	{
		return root.render(0);
	}
	
	public Double getFitness()
	{
		if (fitness == null)
		{
			fitness = calcFitness();
		}
		return fitness;
	}
	
	private double calcFitness()
	{
//		double keyScore = evaluateKey();
//		double rhythymScore = evaluateRhythm();
//		return keyScore + rhythymScore;
		double score = 0;
		if (this.root.getSize() <= maxSize)
		{
			score = scoreHighNotes();
		}
		return score;
	}
	
	private double scoreHighNotes()
	{
		List<MusicEvent> events = getRender();
		
		return events
				.stream()
				.mapToDouble(me -> me.note.pitch.getMidiPitch())
				.sum();
	}
	
	private double evaluateKey()
	{
		List<MusicEvent> events = getRender();
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
		return (double)keyNum / (double)numPitches;
	}
	
	private double evaluateRhythm()
	{
		List<MusicEvent> events = getRender();
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
		int beatStrength = numDownBeats + eventsPerBeat.getOrDefault(4, 0) + eventsPerBeat.getOrDefault(8, 0) + eventsPerBeat.getOrDefault(12, 0);
		return (double)(numDownBeats + beatStrength) / (double)numEvents;
	}
	
	public String toString()
	{
		return this.root.toString();
	}
}
