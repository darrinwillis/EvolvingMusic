package em.application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import em.geneticProgram.GPRoundResult;
import em.representation.MusicTree;

public class RoundResult {

	private final IntegerProperty generation;
	private final DoubleProperty fitness;

	private final ObjectProperty<MusicTree> musicTree;

	public RoundResult() {
		this(0, null);
	}

	public RoundResult(GPRoundResult gprr) {
		this(gprr.generation, gprr.bestTree);
	}

	public RoundResult(int generation, MusicTree mt) {
		this.generation = new SimpleIntegerProperty(generation);
		this.musicTree = new SimpleObjectProperty<MusicTree>(mt);
		if (mt != null)
		{
			this.fitness = new SimpleDoubleProperty(mt.getFitness());
		} else
		{
			this.fitness = new SimpleDoubleProperty(0.0d);
		}
	}

	public Integer getGeneration()
	{
		return this.generation.get();
	}

	public void setGeneration(Integer gen)
	{
		this.generation.set(gen);
	}

	public IntegerProperty generationProperty()
	{
		return generation;
	}

	public Double getFitness()
	{
		return this.fitness.get();
	}

	public DoubleProperty fitnessProperty()
	{
		return fitness;
	}

	public void setTree(MusicTree mt)
	{
		this.musicTree.set(mt);
		this.fitness.set(mt.getFitness());
	}

	public MusicTree getTree()
	{
		return this.musicTree.get();
	}
}
