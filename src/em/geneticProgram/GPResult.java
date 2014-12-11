package em.geneticProgram;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

import em.representation.Fitness;
import em.representation.MusicTree;

public class GPResult {
	public List<GPRoundResult> roundResults;
	public MusicTree bestTree = null;
	public double bestFitness = Double.MIN_VALUE;
	private int currentRound = 0;

	public GPResult() {
		this.roundResults = new ArrayList<GPRoundResult>();
	}

	public void addReport(List<MusicTree> children)
	{
		GPRoundResult roundReport = new GPRoundResult();

		// Find the best tree of the children
		MusicTree thisBestTree = children
				.stream()
				.max((mt1, mt2) -> Double.compare(mt1.getFitness(),
						mt2.getFitness())).get();

		// Analyze the children for basic fitness statistics
		DoubleSummaryStatistics dss = children.stream()
				.mapToDouble(MusicTree::getFitness).summaryStatistics();

		if (thisBestTree.getFitness() > bestFitness)
		{
			bestTree = thisBestTree;
			bestFitness = thisBestTree.getFitness();
		}
		roundReport.bestTree = thisBestTree;
		roundReport.roundFitnessStats = dss;
		roundReport.bestReport = Fitness.getReport(thisBestTree);

		this.roundResults.add(roundReport);
		System.out.printf("Finished Generation %d: stats were%n%s%n",
				currentRound, dss.toString());
		currentRound++;
		return;
	}
}
