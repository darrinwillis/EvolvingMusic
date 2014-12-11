package em.geneticProgram;

import java.util.DoubleSummaryStatistics;

import em.representation.Fitness;
import em.representation.MusicTree;

public class GPRoundResult {
	public DoubleSummaryStatistics roundFitnessStats;
	public MusicTree bestTree;
	public Fitness.Report bestReport;
}
