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
		int generations = 100;
		//MusicTree mt = MusicTree.RandomTree(maxDepth);
		
		GeneticProgram gp = new GeneticProgram
				.GeneticProgramBuilder(generations)
				.populationSize(populationSize)
				.initialMaxDepth(maxDepth)
				.doMutationProb(.1)
				.createGP();
		
		MusicTree evolvedTree = gp.run();
		
		player.playTree(evolvedTree);
				
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
	
}
