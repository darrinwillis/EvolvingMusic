import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class Analyzer {
	
	public static void showAnalysis(GPResult runResult)
	{
		XYDataset d = createDataset(runResult);
		
		String title = String.format("Fitness Change of %d Music Trees", runResult.roundResults.size());
		
        JFreeChart chart = ChartFactory.createScatterPlot(
                title, // chart title
                "Generation", // x axis label
                "Value", // y axis label
                d, // data  ***-----PROBLEM------***
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );

            // create and display a frame...
            ChartFrame frame = new ChartFrame("Evolving Music Fitness - dswillis", chart);
            frame.pack();
            frame.setVisible(true);
	}
	
	private static XYDataset createDataset(GPResult runResult) {
	    XYSeriesCollection result = new XYSeriesCollection();
	    XYSeries bestSeries = new XYSeries("Best Fitness");
	    XYSeries meanSeries = new XYSeries("Average Fitness");
	    int i = 0;
	    for (GPRoundResult round : runResult.roundResults)
	    {
	        bestSeries.add(i, round.roundFitnessStats.getMax());
	        meanSeries.add(i, round.roundFitnessStats.getAverage());
	        i++;
	    }
	    result.addSeries(bestSeries);
	    result.addSeries(meanSeries);
	    
	    return result;
	}
	
	public static void showFitnessAnalysis(GPResult runResult)
	{
		XYDataset d = createFitnessDataset(runResult);
		
		String title = String.format("Fitness Breakdown of %d Music Trees", runResult.roundResults.size());
		
        JFreeChart chart = ChartFactory.createScatterPlot(
                title, // chart title
                "Generation", // x axis label
                "Value", // y axis label
                d, // data  ***-----PROBLEM------***
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );

            // create and display a frame...
            ChartFrame frame = new ChartFrame("Evolving Music Fitness - dswillis", chart);
            frame.pack();
            frame.setVisible(true);
	}
	
	private static XYDataset createFitnessDataset(GPResult runResult) {
	    XYSeriesCollection result = new XYSeriesCollection();
	    XYSeries sizeSeries = new XYSeries("Size / 2");
	    XYSeries keySeries = new XYSeries("Key Score");
	    XYSeries rhythymSeries = new XYSeries("Rhythym Score");
	    XYSeries chordSeries = new XYSeries("Chord Score");
	    XYSeries leapSeries = new XYSeries("Leap Score");
	    XYSeries progSeries = new XYSeries("Progression Score");
	    XYSeries lengthSeries = new XYSeries("-(Length Score)");
	    int i = 0;
	    for (GPRoundResult round : runResult.roundResults)
	    {
	    	sizeSeries.add(i, round.bestReport.size);
	        keySeries.add(i, round.bestReport.keyScore);
	        rhythymSeries.add(i, round.bestReport.rhythymScore);
	        chordSeries.add(i, round.bestReport.chordScore);
	        leapSeries.add(i, round.bestReport.leapScore);
	        progSeries.add(i, round.bestReport.progressionScore);
	        lengthSeries.add(i, -round.bestReport.lengthScore);
	        i++;
	    }
	    result.addSeries(sizeSeries);
	    result.addSeries(keySeries);
	    result.addSeries(rhythymSeries);
	    result.addSeries(chordSeries);
	    result.addSeries(leapSeries);
	    result.addSeries(progSeries);
	    result.addSeries(lengthSeries);
	    
	    return result;
	}
	
	public static void showMultiRunAnalysis(List<GPResult> runResults)
	{
		XYDataset d = createMultiDataset(runResults);
		
		String title = String.format("Fitness Breakdown of %d Runs", runResults.size());
		
        JFreeChart chart = ChartFactory.createScatterPlot(
                title, // chart title
                "Generation", // x axis label
                "Value based on round best", // y axis label
                d, // data  ***-----PROBLEM------***
                PlotOrientation.VERTICAL,
                true, // include legend
                true, // tooltips
                false // urls
                );

            // create and display a frame...
            ChartFrame frame = new ChartFrame("Evolving Music Fitness - dswillis", chart);
            frame.pack();
            frame.setVisible(true);
	}
	
	private static XYDataset createMultiDataset(List<GPResult> runResults) {
	    XYSeriesCollection result = new XYSeriesCollection();
	    XYSeries maxSeries = new XYSeries("Max Fitness");
	    XYSeries upStdSeries = new XYSeries("Avg + StdDev");
	    XYSeries meanSeries = new XYSeries("Avg");
	    XYSeries downSeries = new XYSeries("Avg - StdDev");
	    XYSeries minSeries = new XYSeries("Min Fitness");
	    
	    int size = runResults.get(0).roundResults.size();
	    for (int i = 0; i < size; i++)
	    {
	    	List<GPRoundResult> currentRound = new ArrayList<GPRoundResult>();
	    	for (GPResult gpr : runResults)
	    	{
	    		currentRound.add(gpr.roundResults.get(i));
	    	}
	    	
	    	double max, up, mean, down, min;
	    	
	    	DoubleSummaryStatistics dss = currentRound.stream()
	    			.mapToDouble(gprr -> gprr.roundFitnessStats.getMax())
	    			.summaryStatistics();
	    	
	    	double stdDev = Util.stdDev(
	    			currentRound
	    				.stream()
	    				.map(gprr -> gprr.roundFitnessStats.getMax())
	    				.collect(Collectors.toList())
	    			);
	    	max = dss.getMax();
	    	mean = dss.getAverage();
	    	up = mean + stdDev;
	    	down = mean + stdDev;
	    	min = dss.getMin();
	    	
	    	maxSeries.add(i, max);
	    	upStdSeries.add(i, up);
	    	meanSeries.add(i, mean);
	    	downSeries.add(i, down);
	    	minSeries.add(i, min);
	    }
	    result.addSeries(maxSeries);
	    result.addSeries(upStdSeries);
	    result.addSeries(meanSeries);
	    result.addSeries(downSeries);
	    result.addSeries(minSeries);
	    
	    return result;
	}
}
