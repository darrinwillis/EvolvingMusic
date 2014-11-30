import java.util.Scanner;


public class TestMusic {

	public static void main(String[] args) {
		new TestMusic().run();
	}

	public void run()
	{
		Player player = new Player();
		
		int maxDepth = 6;
		int populationSize = 1000;
		int generations = 10;
		
		GeneticProgram gp = new GeneticProgram
				.GeneticProgramBuilder(generations)
				.populationSize(populationSize)
				.initialMaxDepth(maxDepth)
				.doMutationProb(0)
				.createGP();
		
		GPResult runResult = gp.run();
		
		Analyzer.showAnalysis(runResult);
		
		do {
			player.playTree(runResult.bestTree);
		} while (!checkUserEnd());


		int rating = getUserRating();
		System.out.printf("the rating was %d%n", rating);
		player.close();
	}
	
	public int getUserRating()
	{
		System.out.printf("Rate what you just heard from 1-10%n");
		Scanner scan = new Scanner(System.in);
		int input = -1;
		while (!(input >= 1 && input <= 10))
		{
			while (!scan.hasNextInt()) {
				scan.next();
			}
			input = scan.nextInt();
		}
		scan.close();
		return input;
	}
	
	public boolean checkUserEnd()
	{
		System.out.printf("Input 'end' to quit, otherwise it will play again%n");
		String match = "end";
		Scanner scan = new Scanner(System.in);
		while (!scan.hasNextLine()) {
			Thread.yield();
		}
		String input = scan.nextLine();
		boolean done = input.contains(match);
		scan.close();
		return done;
	}
}
