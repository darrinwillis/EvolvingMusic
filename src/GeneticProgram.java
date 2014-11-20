import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class GeneticProgram {
	private final int numGenerations;
	private final int populationSize;
	private final int initialMaxDepth;
	private final double doMutationProb;
	
	private List<MusicTree> population;
	
	public GeneticProgram(
			final int generations,
			final int populationSize,
			final int initialMaxDepth,
			final double doMutationProb)
	{
		this.numGenerations = generations;
		this.populationSize = populationSize;
		this.initialMaxDepth = initialMaxDepth;
		this.doMutationProb = doMutationProb;
		return;
	}
	
	public MusicTree run()
	{
		MusicTree bestTree = null;
		double bestFitness = Double.MIN_VALUE;
		generatePopulation();
		
		for (int i = 0; i < this.numGenerations; i++)
		{
			List<MusicTree> children = generateChildren();
			MusicTree thisBestTree = children
					.stream()
					.max(
							(mt1, mt2) -> 
							Double.compare(mt1.getFitness(), mt2.getFitness())).get();
			if (thisBestTree.getFitness() > bestFitness)
			{
				bestTree = thisBestTree;
				bestFitness = thisBestTree.getFitness();
			}
			this.population = children;
			System.out.printf("Finished Generation %d: Best fitness was %f%n", i, bestFitness);
		}
		
		return bestTree;
	}
	
	private void generatePopulation()
	{
		this.population = new ArrayList<MusicTree>();
		for (int i = 0; i < populationSize; i++)
		{
			MusicTree mt = MusicTree.RandomTree(this.initialMaxDepth);
			this.population.add(mt);
		}
		return;
	}
	
	private List<MusicTree> generateChildren()
	{
		List<MusicTree> children = new ArrayList<MusicTree>();
		List<MusicTree> matingPool = selectMatingPool();
		assert matingPool.size() == populationSize;
		//Perform genetic operations on all of the mating pool, until it is empty
		
		while (matingPool.size() != 0)
		{
			if (matingPool.size() == 1 || ThreadLocalRandom.current().nextDouble() <= doMutationProb)
			{
				//We do mutation for now
				MusicTree mt = matingPool.remove(0);
				mt.mutate();
				children.add(mt);
			} else {
				//We do crossover instead of mutation
				MusicTree mt1 = matingPool.remove(0);
				MusicTree mt2 = matingPool.remove(0);
				MusicTree.crossOver(mt1, mt2);
				children.add(mt1);
				children.add(mt2);
			}
		}
		assert children.size() == populationSize;
		return children;
	}
	
	//This code has been repurposed from HW1: OneMax
	private List<MusicTree> selectMatingPool()
    {
            double[] cdf = new double[populationSize];
           
            List<MusicTree> matingPool = new ArrayList<MusicTree>();
           
            int totalFitness = 0;
            for (MusicTree mt : population)
            {
                    totalFitness += mt.getFitness();
            }
           
            int currentFitness = totalFitness;
           
            //This sets up the CDF for the whole population
            for(int i = populationSize-1; i >= 0; i--)
            {
                    cdf[i] = ((double)currentFitness) / ((double)totalFitness);
                    currentFitness -= population.get(i).getFitness();
            }
           
            //We now need to probabilistically select the correct individuals
            for(int i = 0; i < populationSize; i++)
            {
                    //Select one random individual
                    double r = ThreadLocalRandom.current().nextDouble();
                    int j = 0;
                    while (cdf[j] < r)
                    {
                            j++;
                    }
                    //We now have an element that fits in this probabilistic bucket
                   
                    matingPool.add(population.get(j));
            }
           
            return matingPool;
    }
	
	public static class GeneticProgramBuilder
	{
		private final int numGenerations;
		private int populationSize;
		private int initialMaxDepth;
		private double doMutationProb;
		
		public GeneticProgramBuilder(final int numGenerations)
		{
			this.numGenerations = numGenerations;
		}
		
		public GeneticProgramBuilder populationSize(final int populationSize)
		{
			this.populationSize = populationSize;
			return this;
		}
		
		public GeneticProgramBuilder initialMaxDepth(final int maxDepth)
		{
			this.initialMaxDepth = maxDepth;
			return this;
		}
		
		public GeneticProgramBuilder doMutationProb(final double doMutationProb)
		{
			this.doMutationProb = doMutationProb;
			return this;
		}
		
		public GeneticProgram createGP()
		{
			return new GeneticProgram(
					numGenerations,
					populationSize,
					initialMaxDepth,
					doMutationProb);
		}
	}
}
