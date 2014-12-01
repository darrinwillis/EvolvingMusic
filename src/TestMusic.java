import java.util.Scanner;


public class TestMusic {

	Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		new TestMusic().run();
	}

	public void run()
	{
		Player player = new Player();
		
		int maxDepth = 6;
		int populationSize = 1000;
		int generations = 1000;
		
		GeneticProgram gp = new GeneticProgram
				.GeneticProgramBuilder(generations)
				.populationSize(populationSize)
				.initialMaxDepth(maxDepth)
				.doMutationProb(0.01)
				.createGP();
		
		long startTime = System.currentTimeMillis();
		GPResult runResult = gp.run();
		long stopTime = System.currentTimeMillis();
		long runTime = stopTime - startTime;
		System.out.printf("The run time for %d generations was %ds, which is %fms per generation%n,",
				generations,
				runTime / 1000,
				((double)runTime / (double)generations));
		
		Analyzer.showAnalysis(runResult);
		Analyzer.showFitnessAnalysis(runResult);
		
		do {
			player.playTree(runResult.bestTree);
		} while (!checkUserEnd());

		System.out.println("Thanks for using Evolving Music!");
		scan.close();
		player.close();
	}
	
	public int getUserRating()
	{
		System.out.printf("Rate what you just heard from 1-10%n");
		int input = -1;
		while (!(input >= 1 && input <= 10))
		{
			while (!scan.hasNextInt()) {
				scan.next();
			}
			input = scan.nextInt();
		}
		return input;
	}
	
	public boolean checkUserEnd()
	{
		System.out.printf("Input 'end' to quit, otherwise it will play again%n");
		String match = "end";
		while (!scan.hasNextLine()) {
			Thread.yield();
		}
		String input = scan.nextLine();
		boolean done = input.contains(match);
		return done;
	}
}
