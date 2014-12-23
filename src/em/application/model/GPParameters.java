package em.application.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GPParameters {

	private final IntegerProperty numGenerations;
	private final IntegerProperty populationSize;
	private final DoubleProperty mutationProb;

	public GPParameters() {
		this(0, 0, 0);
	}

	public GPParameters(int numGen, int popSize, double mutProb) {
		this.numGenerations = new SimpleIntegerProperty(numGen);
		this.populationSize = new SimpleIntegerProperty(popSize);
		this.mutationProb = new SimpleDoubleProperty(mutProb);
	}

	public void setNumGenerations(int ng)
	{
		this.numGenerations.set(ng);
	}

	public int getNumGenerations()
	{
		return this.numGenerations.get();
	}

	public IntegerProperty numGenerations()
	{
		return this.numGenerations;
	}

	public void setPopulationSize(int pop)
	{
		this.populationSize.set(pop);
	}

	public int getPopulationSize()
	{
		return this.populationSize.get();
	}

	public IntegerProperty populationSize()
	{
		return this.populationSize;
	}

	public void setMutationProbability(double mut)
	{
		this.mutationProb.set(mut);
	}

	public Double getMutationProbability()
	{
		return this.mutationProb.get();
	}

	public DoubleProperty mutationProbability()
	{
		return this.mutationProb;
	}
}
